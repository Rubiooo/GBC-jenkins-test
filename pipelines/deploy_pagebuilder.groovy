node {

  def environment="devl\ntest\nprod\nprds"
  def targetServers=['devl':'DEVL',
  'test':'TEST',
  'prod':'PROD',
  'prds':'PRDS']
  def targetHosts = ['devl':'gbcdev.local',
  'test':'Gbcuat.local',
  'prod':'tbd',
  'prds':'tbd']
  def pagePaths = ['devl': '/opt/tomcat/temp',
  'test':'/u02/banapp/temp/pb',
  'prod':'tbd',
  'prds':'tbd']
  def folderNames = ['devl': 'pbTemp.page/pbTemp.virtualDomain/pbTemp.css',
  'test':'page/virtdom/css',
  'prod':'page/virtdom/css',
  'prds':'page/virtdom/css']
  def webServerPaths = ['devl': '/opt/tomcat/bin',
  'test':'/u01/app/oracle/Middleware/wlserver/server/lib/weblogic.jar',
  'prod':'tbd',
  'prds':'tbd']
  def adminurls = ['devl': 'na',
  'test':'t3://gbcxe01u:7001',
  'prod':'tbd',
  'prds':'tbd']
  def applicationNames = ['devl': 'na',
  'test':'BannerExtensibility',
  'prod':'tbd',
  'prds':'tbd']
  def applicationServers = ['devl': 'na',
  'test':'Banner9-App',
  'prod':'tbd',
  'prds':'tbd']
  def sourcePaths = ['devl': 'na',
  'test':'/u02/banapp/staging/ban9app/banner_extensibility_9.2/current/dist',
  'prod':'tbd',
  'prds':'tbd']

  def userInput
  def targetEnv
  def targetServer
  def targetHost
  def fileList = []
  def pageName
  def jsonFile
  def prefix = ''
  def nameList = []
  def pages = []
  def pageList = []
  def pagePath
  def pageFolder
  def virtFolder
  def cssFolder
  def webServerPath
  def adminurl
  def credential
  def applicationName
  def applicationServer
  def sourcePath

  timestamps {
    stage("Choose env") {
      userInput = input(
        id: 'userInput', message: 'choose target environment', parameters: [
        choice(choices: environment, description: 'target enviorment', name: 'env'),
        string(defaultValue: '', description: 'weblogic username, if applicable', name: 'username', trim: true),
        password(defaultValue: '', description: 'webloigc password, if applicable', name: 'password')
        ])

      targetEnv = userInput['env']
      targetServer = targetServers[targetEnv]
      targetHost = targetHosts[targetEnv]
      pagePath = pagePaths[targetEnv]
      pageFolder = folderNames[targetEnv].split("/")[0]
      virtFolder = folderNames[targetEnv].split("/")[1]
      cssFolder = folderNames[targetEnv].split("/")[2]
      webServerPath = webServerPaths[targetEnv]
      adminurl = adminurls[targetEnv]
      def username = userInput['username']
      def password = userInput['password']
      credential = "-username $username -password $password"
      applicationName = applicationNames[targetEnv]
      applicationServer = applicationServers[targetEnv]
      sourcePath = sourcePaths[targetEnv]
    }

    stage("Stop Web Application") {
      if ('devl'.equals(targetEnv)) {
        sh "ssh $targetHost \"sudo -u tomcat ${webServerPath}/shutdown.sh\""
      } else {
        sh "set +x; ssh $targetHost \"java -cp $webServerPath weblogic.Deployer -adminurl $adminurl $credential -name $applicationName -undeploy -timeout 300\""
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
          // delete files that are older than 30 days
          sh "ssh $targetHost \"find ${pagePath}/${pageFolder} -type f -mtime +30 -exec rm -rf {} \\;\""
          sh "ssh $targetHost \"find ${pagePath}/${virtFolder} -type f -mtime +30 -exec rm -rf {} \\;\""
          sh "ssh $targetHost \"find ${pagePath}/${cssFolder} -type f -mtime +30 -exec rm -rf {} \\;\""

          pageName = line.trim().substring(line.lastIndexOf("/", line.lastIndexOf("/") - 1 ) + 1, line.lastIndexOf("/"))
          if (userInput[pageName]) {
            println pageName
            jsonFile = line.trim().substring(line.lastIndexOf("/") + 1)
            println "jsonFile: $jsonFile"
            prefix = jsonFile.trim().substring(0, jsonFile.indexOf("."))
            println "prefix: $prefix"
            def fileName = jsonFile.trim().substring(jsonFile.indexOf(".") + 1)
            println "fileName: $fileName"
            sh "cp $line $fileName"

            switch (prefix) {
              case "pages":
                sh "scp $fileName $targetHost:${pagePath}/${pageFolder}/"
                break
              case "virtualDomains":
                sh "scp $fileName $targetHost:${pagePath}/${virtFolder}/"
                break
              case "css":
                sh "scp $fileName $targetHost:${pagePath}/${cssFolder}/"
                break
              default:
                throw "Unknown file category"
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
        sh "ssh $targetHost \"sudo -u tomcat ${webServerPath}/startup.sh\""
      } else {
        sh "set +x; ssh $targetHost \"java -cp $webServerPath weblogic.Deployer -adminurl $adminurl $credential -targets $applicationServer -deploy ${sourcePath}/${applicationName}.war -timeout 300\""
      }
    }
  }
}
