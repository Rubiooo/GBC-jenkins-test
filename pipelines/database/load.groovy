    import groovy.io.FileType

    node {
      def environment="devl\ntest\nprod\nprds"
      def dburl=""
      def dburls = ['devl':'@//gbcoprx01u.gbcuat.local:1523/DEVL',
      'test':'@//gbcoprx01u.gbcuat.local:1521/TEST',
      'prod':'@//gbcoprx01p.gbc.local:1521/PROD',
      'prds':'@//gbcoprx01u.gbcuat.local:1522/PRDS']
      def jssList = ['devl':'banadmin@gbcjss02.gbc.local',
      'test':'banadmin@gbcjss02.gbc.local',
      'prod':'banadmin@gbcjss02.gbc.local',
      'prds':'banadmin@gbcjss02.gbc.local']
      def jssPathList = ['devl':'/u02/gbc/banner/DEVL',
      'test':'/u02/gbc/banner/TEST',
      'prod':'/u02/gbc/banner/PROD',
      'prds':'/u02/gbc/banner/PRDS']
      def cmd=""

      def userInput
      def slackMessage=""
      timestamps {
        stage("choose env") {

          userInput = input(
           id: 'userInput', message: 'choose Env', parameters: [
           choice(choices: environment, description: '', name: 'env'),
           string(defaultValue: 'jenkins', description: 'db username', name: 'username', trim: true),
           password(defaultValue: '', description: 'db password', name: 'password'),
           booleanParam(defaultValue: true, description: 'Dry Run', name: 'dryrun')
           ])
          dburl= userInput['username']+"/"+userInput['password']+ dburls[userInput['env']]
          scm=checkout([
            $class: 'GitSCM',
            branches: [[name: 'master']],
            userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/gbcbanner.git']]
          ])
        }

        stage("load sql file") {
          wrap([$class: 'BuildUser']) {
            slackMessage = "${BUILD_USER} is loading DB script on database ${dburls[userInput['env']]}\n"
            slackMessage += "Dry run: "+ userInput['dryrun']
            slackSend (channel: 'db-promotion', message: slackMessage)
          }
          ROOTFOLDER = env.WORKSPACE+"/"+userInput["env"]
          def jssServer= jssList[userInput['env']]
          def jssPath= jssPathList[userInput['env']]
          String[] install = new File("${ROOTFOLDER}/deployment/install.txt").readLines()

          try{
            for (line in install) {

              if (fileExists("${ROOTFOLDER}/deployment/+line+".csv")) {
                slackMessage="processing ticket: " +line+"\n"
                String[]csvfiles = new File("${ROOTFOLDER}/deployment/"+line+".csv").readLines()

                withCredentials([usernamePassword(credentialsId: 'artifactory', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                   def curl_login="curl -u $USERNAME:$PASSWORD"
                   cmd = "${curl_login} -s -X GET -H \"Content-Type: application/json\" 'https://jira.georgebrown.ca/rest/api/2/search?jql=status%20%3D%20"Code%20Release%20Promotion%20(PROD)"&fields=key'|jq -r .issues[].key"
                 }
                 prodlist = sh (returnStdout: true, script: cmd)
                 println (prodlist)

                for (lines in csvfiles) {
                  def (actionType, filename) = lines.split(',')
                  filename=filename.trim()
                  foldername=filename.substring(0, filename.lastIndexOf("/"))
                  if (filename.contains(" ")) {
                      slackMessage += "space in filename: "+ filename + "\n"
                      throw "space in filename"
                  }
                  if (filename.contains("\\")) {
                      slackMessage += "backslash in filename: "+ filename + "\n"
                      throw "backslash in filename"
                  }
                  if (! fileExists ( "${ROOTFOLDER}/${filename}")) {
                      slackMessage += "file not exist: "+ filename + "\n"
                      throw "file not exist"
                  }
                  switch (actionType.trim()) {
                    case "db":
                      slackMessage += " >> loading sqlfile: " + filename +"\n"
                      cmd = "docker run -v ${ROOTFOLDER}:/sql -t --rm -e URL=$dburl nextlink/sqlplus @/sql/$filename"
                      println (cmd)
                      if (userInput['dryrun']==false) {
                        sh (cmd)
                        def output = sh (returnStdout: true, script: cmd)
                        println output
                        if (output.contains("ERROR")) {
                          slackMessage += "ERROR in output: "+ filename + "\n"
                          throw "ERROR in output"
                        }
                    }
                      }
                      break
                    case "jss":
                      slackMessage += " >> copy scripts to jobs server: " + filename + "\n"
                      cmd="scp ${ROOTFOLDER}/${filename} ${jssServer}:${jssPath}/${filename}"
                      println (cmd)
                      if (userInput['dryrun']==false) {
                        sh "ssh ${jssServer} mkdir -p ${jssPath}/${foldername}"
                        sh (cmd)
                        sh "ssh ${jssServer} chmod 755 ${jssPath}/${filename}"
                      }
                      break
                    case "lnk":
                      slackMessage += " >> copy scripts to jobs server and create symbol link: " + filename + "\n"
                      cmd="scp ${ROOTFOLDER}/${filename} ${jssServer}:${jssPath}/${filename}"
                      cmd2= "ln -f -s ${jssPath}/${filename} ${jssPath}/gbclinks"
                      println (cmd)
                      println (cmd2)
                      if (userInput['dryrun']==false) {
                        sh "ssh ${jssServer} mkdir -p ${jssPath}/${foldername}"
                        sh (cmd)
                        sh "ssh ${jssServer} chmod 755 ${jssPath}/${filename}"
                        sh "ssh ${jssServer} ${cmd2}"
                      }
                      break
                    case "file":
                      slackMessage = " >> ignore file: " + filename + "\n"
                      break
                    default:
                      slackMessage += "unknow category "+ actionType + "\n"
                      throw "unkonw category"
                  }
                }
                slackSend (channel: 'db-promotion', message: slackMessage)
              }
            }
            slackSend (channel: 'db-promotion', message: ":white_check_mark: Job complete, please check logs -> ${BUILD_URL}")
          } catch(error){
            currentBuild.result = 'FAILURE'
            slackSend (channel: 'db-promotion', message: slackMessage)
            slackSend (channel: 'db-promotion', message: ":warning: Job failure, please check logs -> ${BUILD_URL}")
          }
        }
      }
    }
