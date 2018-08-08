node {
  def curl_login="curl -u admin:admin@gbc"
  def release
  def prefix
  def suffix
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
       choice(choices: releases, description: '', name: 'release')
       ])
      echo ("release: "+userInput)
      release=userInput
    }

    stage("choose jars") {
      prefix="generic-local/build-gbc/"+release+"/"
      command = "jfrog rt s ${prefix}*.jar|jq -r '.[].path|ltrimstr(\"${prefix}\")'"
      def jars = sh(returnStdout: true, script: command)
      print (jars)
      for (jar in jars){
        print "jar-> " + jar
      }
    input message: 'choose jars', parameters: [
    booleanParam(defaultValue: false, description: '', name: 'j1'),
    booleanParam(defaultValue: false, description: '', name: 'j2')
    ]
    }

    stage ("package zip file") {
    }

  }
}
