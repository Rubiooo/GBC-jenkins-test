import groovy.io.FileType

node {
  def sourceEnv
  def targetEnv
  def promotionPath ="devl->test\ndevl->prds\ntest->prds\ntest->prod\nprds->prod"
  def rootFolder
  def userInput
  def install=[]
  timestamps {
    stage("choose env") {

      userInput = input(
       id: 'userInput', message: 'choose source and target environment', parameters: [
       choice(choices: promotionPath, description: '', name: 'promotion Path')
       ])
      sourceEnv = userInput.split("->")[0]
      targetEnv = userInput.split("->")[1]
    }

    stage("choose Jira issues"){
      sh "rm -rf gbcbanner"
      sh "git clone ssh://git@gitrepo.georgebrown.ca:7999/gbc/gbcbanner.git"

      rootFolder = env.WORKSPACE+"/gbcbanner/"+sourceEnv
      install = new File("${rootFolder}/deployment/install.txt").readLines()
      def packageList=[]
      for (line in install) {
        packageList.add(booleanParam(defaultValue: false, description: '', name: line))
      }
      packageList.add(string(defaultValue: '', description: 'Pull Request description', name: 'description', trim: true))
      userInput= input (message: 'choose Jira issues', parameters: packageList)
    }
    stage("promote scripts") {
      def sourceBranch= "${sourceEnv}-${targetEnv}"
      def now = new Date()
      def TIMESTAMP=now.format("yyyyMMdd-HHmm")
      dir ("gbcbanner"){
        sh "git checkout -B ${sourceBranch}"
        sh "cp ${sourceEnv}/deployment/install.txt ${targetEnv}/deployment/"
        sh "git add ${targetEnv}/deployment/install.txt"

        for (line in install) {
          if (userInput[line]) {
            sh "cp ${sourceEnv}/deployment/${line}.csv ${targetEnv}/deployment/"
            sh "git add ${targetEnv}/deployment/${line}.csv"
            String[] csvfiles = new File("${rootFolder}/deployment/"+line+".csv").readLines()
            for (lines in csvfiles) {
              def (actionType, filename) = lines.split(',')
              filename=filename.trim()
              foldername=filename.substring(0, filename.lastIndexOf("/"))
              sh "mkdir -p ${targetEnv}/${foldername}"
              sh "cp ${sourceEnv}/${filename} ${targetEnv}/${foldername}"
              sh "git add ${targetEnv}/${filename}"
            }
          }
        }
        description=userInput["description"].replaceAll("#"," ")
        wrap([$class: 'BuildUser']) {
          sh "git config user.name \"${BUILD_USER}\""
          sh "git commit -m \"${BUILD_USER} promote ${sourceEnv} to ${targetEnv} on ${TIMESTAMP}\""
          sh "git push origin HEAD --force"
          sh "sed -i \"s#BUILD_USER#${BUILD_USER}#\" pr.json"
        }
        sh "sed -i s#SOURCEBRANCH#${sourceBranch}# pr.json"
        sh "sed -i s#SOURCEENV#${sourceEnv}# pr.json"
        sh "sed -i s#TARGETENV"#${targetEnv}# pr.json"
        sh "sed -i s#DESCRIPTION#\"${description}\"# pr.json"
        withCredentials([usernamePassword(credentialsId: 'artifactory', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          def curl_login="curl -u $USERNAME:$PASSWORD"
          sh "${curl_login} -X POST -H \"Content-Type: application/json\" -d @pr.json https://gitrepo.georgebrown.ca/rest/api/1.0/projects/GBC/repos/gbcbanner/pull-requests"
        }
      }
      wrap([$class: 'BuildUser']) {
        slackSend (channel: 'db-promotion', message: "${BUILD_USER} Promote DB scripts from ${sourceEnv} to ${targetEnv}")
      }
    }

  }
}
