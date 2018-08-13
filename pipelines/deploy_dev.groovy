node {


  def host="ban9appnav01d.gbcdev.local"
  timestamps {
    // stage("prepare env") {
    //   def userInput = input(
    //    id: 'userInput', message: 'choose target server', parameters: [
    //    choice(choices: "ban9appnav01d.gbcdev.local", description: 'Hostname', name: 'host')
    //    ])
    //   echo ("Host: "+userInput)

    //   host=userInput
    // }

    stage ("stop tomcat") {
      slackSend (channel: 'jenkins', message: ":black_square_button: Start deploy Admin Page to " + host)
      sh "ssh $host \"sudo -u tomcat /u01/app/tomcat/bin/shutdown.sh\""
    }

    stage ("upload war file") {
      withCredentials([usernamePassword(credentialsId: 'artifactory', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        def curl_login="curl -u $USERNAME:$PASSWORD"
        sh "$curl_login -sSLO https://artifactory.georgebrown.ca/artifactory/generic-local/build-gbc/latest/wrksp.war"
        sh "$curl_login -sSLO https://artifactory.georgebrown.ca/artifactory/generic-local/build-gbc/latest/wrksp.ws.war"
        sh "$curl_login -sSLO https://artifactory.georgebrown.ca/artifactory/generic-local/build-gbc/latest/devl.tar.gz"
      }
      sh "ssh $host \"rm -rf /u01/app/tomcat/webapps/wrksp\""
      sh "ssh $host \"rm -rf /u01/app/tomcat/webapps/wrksp.ws\""
      sh "ssh $host \"rm -rf /u01/app/tomcat/temp\""
      sh "scp wrksp.war $host:/u01/app/tomcat/webapps"
      sh "scp wrksp.ws.war $host:/u01/app/tomcat/webapps"
      sh "scp devl.tar.gz $host:/tmp"
      sh "ssh $host \"chown -R tomcat:tomcat /u01/app/tomcat/webapps/\""
    }

    stage ("start tomcat") {
      sh "ssh $host \"sudo -u tomcat /u01/app/tomcat/bin/startup.sh\""
      slackSend (channel: 'jenkins', message: ":ballot_box_with_check: Finish deploy Admin Page to " + host)
    }
  }
}
