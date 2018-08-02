node {
  def curl_login="curl -u usernamexxx:passwordxxx"
  def host=""
  // host="ban9appnav01d.gbcdev.local"

  timestamps {
    stage("prepare env") {
      def userInput = input(
       id: 'userInput', message: 'choose target server', parameters: [
       choice(choices: "ban9appnav01d.gbcdev.local", description: 'Hostname', name: 'host')
       ])
      echo ("Host: "+userInput)
      host=userInput
    }

    stage ("stop tomcat") {
      sh "ssh $host \"sudo -u tomcat /u01/app/tomcat/bin/shutdown.sh\""
    }

    stage ("upload war file") {
      sh "$curl_login -sSLO https://artifactory.georgebrown.ca/artifactory/generic-local/build-gbc/wrksp.war"
      sh "$curl_login -sSLO https://artifactory.georgebrown.ca/artifactory/generic-local/build-gbc/wrksp.ws.war"
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
