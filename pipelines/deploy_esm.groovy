node {

  def release
  def prefix
  def suffix
  def packageName
  def command
  def userInput
  def host="ban9appnav01d.gbcdev.local"

  timestamps {
    stage("choose release") {
      prefix="generic-local/build-gbc/"
      suffix="/wrksp.war"
      command = "jfrog rt s ${prefix}*${suffix}|jq -r '[sort_by(.path)[].path|ltrimstr(\"${prefix}\")|rtrimstr(\"${suffix}\")] - [\"latest\"]|reverse|.[]'"
      def releases = sh(returnStdout: true, script: command)

      userInput = input(
       id: 'userInput', message: 'choose releases version', parameters: [

       string(defaultValue: '9.3.11', description: '', name: 'version', trim: false),
       choice(choices: releases, description: '', name: 'release')
       ])
       release=userInput['release']
       packageName="release-Admin-gbc-"+userInput['version']+".zip"
    }

    stage("choose jars") {
      prefix="generic-local/build-gbc/"+release+"/"
      command = "jfrog rt s ${prefix}*.jar|jq -r '.[].path|ltrimstr(\"${prefix}\")'"
      def jars = sh(returnStdout: true, script: command)
      def jarlist=[]
      for (jar in jars.split('\n')){
        jarlist.add(booleanParam(defaultValue: false, description: '', name: jar))
      }
      userInput= input (message: 'choose jars', parameters: jarlist)
      print(userInput)
      sh "rm -rf BannerAdmin"
      def targetFolder="BannerAdmin/ext/WEB-INF/lib"
      sh "mkdir -p ${targetFolder}"

      withCredentials([usernamePassword(credentialsId: 'artifactory', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        def curl_login="curl -u $USERNAME:$PASSWORD"
        for (jar in jars.split('\n')){
          if (userInput[jar]) {
            print (jar)
            command="${curl_login} -sSL https://artifactory.georgebrown.ca/artifactory/${prefix}${jar} -o ${targetFolder}/${jar}"
            sh(returnStdout: true, script: command)
          }
        }
      }
    }

    stage ("package zip file") {
      sh "rm -f $packageName"
      sh "zip -r $packageName BannerAdmin"
      sh "scp $packageName $host:/tmp"
    }
  }
}
