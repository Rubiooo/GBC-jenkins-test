node {
  def curl_login="curl -u usernamexxx:passwordxxx"
  def release="latest"
  def host="ban9appnav01d.gbcdev.local"
  timestamps {
    stage("choose release") {
      def prefix="generic-local/build-gbc/"
      def suffix="/wrksp.war"
      def command = "jfrog rt s ${prefix}*${suffix}|jq '[sort_by(.path)[].path|ltrimstr(\"${prefix}\")|rtrimstr(\"${suffix}\")] - ["latest"]|.[]'"
      def releases = sh(returnStdout: true, script: command)

      def userInput = input(
       id: 'userInput', message: 'choose releases version', parameters: [
       choice(choices: releases, description: '', name: 'release')
       ])
      echo ("release: "+userInput)
      release=userInput
    }

    stage ("stop tomcat") {
      sh "ssh $host \"sudo -u tomcat /u01/app/tomcat/bin/shutdown.sh\""
    }

    stage ("upload war file") {
      sh "$curl_login -sSLO https://artifactory.georgebrown.ca/artifactory/generic-local/build-gbc/${release}/wrksp.war"
      sh "$curl_login -sSLO https://artifactory.georgebrown.ca/artifactory/generic-local/build-gbc/${release}/wrksp.ws.war"
      sh "ssh $host \"rm -rf /u01/app/tomcat/webapps/wrksp\""
      sh "ssh $host \"rm -rf /u01/app/tomcat/webapps/wrksp.ws\""
      sh "scp wrksp.war $host:/u01/app/tomcat/webapps"
      sh "scp wrksp.ws.war $host:/u01/app/tomcat/webapps"
      sh "ssh $host \"chown tomcat:tomcat /u01/app/tomcat/webapps/*.war\""
    }

    stage ("start tomcat") {
      sh "ssh $host \"sudo -u tomcat /u01/app/tomcat/bin/startup.sh\""
    }
  }
}
