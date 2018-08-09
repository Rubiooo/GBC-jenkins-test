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
       id: 'userInput', message: 'choose sql script', parameters: [
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
      String[] install = new File("${ROOTFOLDER}/deployment/install.txt").readLines()
      try{
        for (line in install) {
          slackMessage="processing ticket: " +line+"\n"
          String[] csvfiles = new File("${ROOTFOLDER}/deployment/"+line+".csv").readLines()

          for (lines in csvfiles) {
            def (actionType, filename) = lines.split(',')
            filename=filename.trim()
            switch (actionType.trim()) {
              case "db":
                slackMessage += " >> loading sqlfile: " + filename +"\n"
                cmd = "docker run -v ${ROOTFOLDER}:/sql -t --rm -e URL=$dburl nextlink/sqlplus @/sql/$filename"
                println (cmd)
                if (userInput['dryrun']==false) {
                  sh (cmd)
                }
                break
              case "jss":
                slackMessage += " >> copy scripts to jobs server: " + filename + "\n"
                def jssServer= jssList[userInput['env']]
                def jssPath= jssPathList[userInput['env']]
                cmd="scp ${ROOTFOLDER}/${filename} ${jssServer}:${jssPath}/${filename}"
                foldername=filename.substring(0, filename.lastIndexOf("/"))
                println (cmd)
                if (userInput['dryrun']==false) {
                  sh "ssh ${jssServer} mkdir -p ${jssPath}/${foldername}"
                  sh (cmd)
                  sh "ssh ${jssServer} chmod 755 ${jssPath}/${filename}"
                }
                break
              default:
                slackMessage += "unknow category "+ actionType + "\n"
            }
          }
          slackSend (channel: 'db-promotion', message: slackMessage)
        }
        slackSend (channel: 'db-promotion', message: ":white_check_mark: Job complete, please check logs -> ${BUILD_URL}")
      } catch(error){
        currentBuild.result = 'FAILURE'
        slackSend (channel: 'db-promotion', message: ":warning: Job failure, please check logs -> ${BUILD_URL}")
      }
    }
  }
}
