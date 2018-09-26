node {

  def environment="devl\ntest\nprod\nprds"
  def targetHosts = ['devl':'gbcdev.local',
  'test':'gbcdev.local',
  'prod':'gbcdev.local',
  'prds':'gbcdev.local']
  def targetServers=['devl':'DEVL',
  'test':'TEST',
  'prod':'PROD',
  'prds':'PRDS']

  def fileList = []
  def targetServer
  def pageName
  def jasonFile
  def prefix = ''
  def nameList = []
  def pages = []
  def pageList = []

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
       tomcatPath =tomcatPaths[targetEnv]
    }

    stage("Stop Tomcat") {
          sh "ssh $targetHost \"sudo -u tomcat ${tomcatPath}/shutdown.sh\""
        }

    try {
        stage("Choose Pages") {
          scm=checkout([
          $class: 'GitSCM',
          branches: [[name: "master"]],
          userRemoteConfigs: [[url: "ssh://gitrepo/banner_extensibility.git"]]
          ])

          sh "rm -f fileList"
          sh "find . -type f -name '*.json' > listJson;"
          def list = readFile( "listJson" ).split( "\\r?\\n" )
          sh "rm -f listJson"
          for (file in list) {
            def newline = file.toString()
            fileList.add(newline)
          }
          HashSet<String> set = new HashSet<>();
          for (line in fileList) {
            pageName = line.trim().substring(line.lastIndexOf("/", line.lastIndexOf("/") - 1 ) + 1, line.lastIndexOf("/"))
            if (!set.contains(pageName)) {
              nameList.add(pageName)
              set.add(pageName)
            }
          }
          pages = nameList.sort()
          for (page in pages) {
            pageList.add(booleanParam(defaultValue: false, description: '', name: page))
          }
          userInput= input (message: 'Choose Pages', parameters: pageList)
        }

        stage("Update Pages") {
          for (line in fileList) {
            pageName = line.trim().substring(line.lastIndexOf("/", line.lastIndexOf("/") - 1 ) + 1, line.lastIndexOf("/"))

            if (userInput[pageName]) {
              println pageName
              jsonFile = line.trim().substring(line.lastIndexOf("/") + 1)
              println "jsonFile: $jsonFile"
              prefix = jsonFile.trim().substring(0, fileName.indexOf("."))
              def fileName = line.trim().substring(line.lastIndexOf("/") + 1)
              println "jsonFile: $jsonFile"
              prefix = jsonFile.trim().substring(0, jsonFile.indexOf("."))
              println "prefix: $prefix"
              def fileName = jsonFile.trim().substring(jsonFile.indexOf(".") + 1)
              println "fileName: $fileName"
              sh "cp $line $fileName"
              switch (prefix) {
                case "pages":
                  sh "scp $fileName $targetHost:${pagePath}/pbTemp.page/"
                  break
                  case "virtualDomains":
                    sh "scp $fileName $targetHost:${pagePath}/pbTemp.virtualDomain/"
                    break
                    case "css":
                      sh "scp $fileName $targetHost:${pagePath}/pbTemp.css/"
                      break
                      default:
                      //slackMessage += "unknow category "+ prefix + "\n"
                      throw "unknow category"
                    }
                    sh "rm -rf $fileName"
                  }
                }
              }
    } catch (e) {
      print e
    }
    stage("Start Tomcat") {
      sh "ssh $targetHost \"sudo -u tomcat ${tomcatPath}/startup.sh\""
    }
  }
}
