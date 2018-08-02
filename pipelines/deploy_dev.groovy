node {
  def curl_login="curl -u usernamexxx:passwordxxx"
  def host=""
  timestamps {
    stage("prepare env") {
      host="ban9appnav01d.gbcdev.local"
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
