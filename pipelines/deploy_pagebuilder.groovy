def virtFolder
def cssFolder
def tomcatPath

timestamps {
  stage("Choose env") {
    userInput = input(
     id: 'userInput', message: 'choose target environment', parameters: [
     choice(choices: environment, description: '', name: 'target environment')
     ])
    targetEnv = userInput
    targetServer = targetServers[targetEnv]
    targetHost = targetHosts[targetEnv]
    pagePath = pagePaths[targetEnv]
    pageFolder = folderNames[targetEnv].split("/")[0]
    virtFolder = folderNames[targetEnv].split("/")[1]
    cssFolder = folderNames[targetEnv].split("/")[2]
    tomcatPath =tomcatPaths[targetEnv]
  }


  stage("Stop Web Application") {
    if ('devl'.equals(targetEnv)) {
      sh "ssh $targetHost \"sudo -u tomcat ${tomcatPath}/shutdown.sh\""
    } else {
      sh "ssh $targetHost \"java -cp /u01/app/oracle/Middleware/wlserver/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://gbcxe01u:7001 -username weblogic -password password1 -name BannerExtensibility -undeploy -timeout 300\""
    }
  }

  try {
    stage("Choose Pages") {
      wrap([$class: 'BuildUser']) {
        slackSend (channel: 'jenkins', message: ":black_square_button: "+  env.BUILD_USER +" start deploy PageBuilder in "+ targetServer +" - "+targetHost)
      }
      scm=checkout([
      $class: 'GitSCM',
      branches: [[name: "master"]],
      userRemoteConfigs: [[url: "ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_extensibility.git"]]
      ])

      sh "rm -f fileList"
      sh "find . -type f -name '*.json' > listJson;"
      def list = readFile( "listJson" ).split( "\\r?\\n" )
      sh "rm -f listJson"
      for (file in list) {
        def newline = file.toString()
              sh "scp $fileName $targetHost:${pagePath}/${virtFolder}/"
              break
            case "css":
              sh "scp $fileName $targetHost:${pagePath}/${cssFolder}/"
              break
            default:
              throw "unknow file category"
          }
          sh "rm -rf $fileName"
        }
      }
      if (!'devl'.equals(targetEnv)) {
        sh "ssh $targetHost \"chown -R banner:banner ${pagePath}/*\""
      }
      slackSend (channel: 'jenkins', message: ":ballot_box_with_check: Successfully deploy PageBuilder in " + targetServer +" "+BUILD_URL)
    }
  } catch(error) {
    currentBuild.result = 'FAILURE'
    slackSend (channel: 'jenkins', message: ":warning: deploy PageBuilder failed, please check logs -> ${BUILD_URL}")
  }

  stage("Start Web Application") {
    if ('devl'.equals(targetEnv)) {
      sh "ssh $targetHost \"sudo -u tomcat ${tomcatPath}/startup.sh\""
    } else {
      sh "ssh $targetHost \"java -cp /u01/app/oracle/Middleware/wlserver/server/lib/weblogic.jar weblogic.Deployer -adminurl t3://gbcxe01u:7001 -username weblogic -password password1 -targets Banner9-App -deploy /u02/banapp/staging/ban9app/banner_extensibility_9.2/current/dist/BannerExtensibility.war -timeout 300\""
    }
  }
}
}
