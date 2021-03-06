node {

  def environment="devl\ntest\nprod\nprds"
  def serverList = ['devl':'DEVL', 'test':'TEST', 'prod':'PROD', 'prds':'PRDS']
  def promotionPath ="devl->devl\ndevl->test\ntest->prds\nprds->prod"
  def jasperList = ['devl':'gbcdev.local',
  'test':'gbcuat.local',
  'prod':'gbcuat.local',
  'prds':'gbcuat.local']
  def tomcatList = ['devl':'/u01/app/tomcat/instances/tomcat1/bin',
  'test':'/opt/tomcat/instances/tomcat1/bin',
  'prod':'/opt/tomcat/instances/tomcat1/bin',
  'prds':'/opt/tomcat/instances/tomcat1/bin']
  def rootFolders = ['devl':'/home/sysadmin/jasperreports-server-6.1.1-bin/buildomatic',
  'test':'/home/sysadmin/jasperreports-server-6.1.1-bin/buildomatic',
  'prod':'/home/sysadmin/jasperreports-server-6.1.1-bin/buildomatic',
  'prds':'/home/sysadmin/jasperreports-server-6.1.1-bin/buildomatic']

  def jrxmlPath = './gbc-*-reports/src/main/java/net/hedtech/banner/gbc/*/reports'

  def userInput
  def targetEnv
  def sourceEnv
  def targetHost
  def sourceHost
  def targetServer
  def sourceServer
  def targetTomcat
  def sourceTomcat
  def targetRootFolder
  def sourceRootFolder
  def sourceReportPath
  def list
  def newline
  def nameList = []
  def fileList = []
  def folderName
  def fileName
  def reportNames = []
  def reportList = []

  timestamps {
    stage("Choose env") {
      userInput = input(
       id: 'userInput', message: 'choose source and target environment', parameters: [
       choice(choices: promotionPath, description: '', name: 'Promotion Path')
       ])
      sourceEnv = userInput.split("->")[0]
      targetEnv = userInput.split("->")[1]
    }

    stage("Choose Jasper reports") {
      scm=checkout([
        $class: 'GitSCM',
        branches: [[name: "master"]],
        userRemoteConfigs: [[url: "ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git"]]
        ])

      sh "rm -f fileList"
      sh "find . -type f -name '*.jrxml' > listJrxml;"
      list = readFile( "listJrxml" ).split( "\\r?\\n" )
      sh "rm -f listJrxml"
      for (file in list) {
        newline = file.toString()
        fileList.add(newline)
      }
      HashSet<String> set = new HashSet<>();
      for (line in fileList) {
        folderName = line.trim().substring(line.lastIndexOf("/", line.lastIndexOf("/") - 1 ) + 1, line.lastIndexOf("/"))
        fileName = folderName.toUpperCase()
        if (!set.contains(fileName)) {
          nameList.add(fileName)
          set.add(fileName)
        }
      }
      reportNames = nameList.sort()
      for (reportName in reportNames) {
        reportList.add(booleanParam(defaultValue: false, description: '', name: reportName))
      }

      targetHost = jasperList[targetEnv]
      sourceHost = jasperList[sourceEnv]
      targetServer = serverList[targetEnv]
      sourceServer = serverList[sourceEnv]
      targetTomcat = tomcatList[targetEnv]
      sourceTomcat = tomcatList[sourceEnv]
      targetRootFolder = rootFolders[targetEnv]
      sourceRootFolder = rootFolders[sourceEnv]
      sourceReportPath = "${sourceRootFolder}/resources/organizations/ellucian/Reports"

      userInput= input (message: 'Choose Jasper reports', parameters: reportList)
      wrap([$class: 'BuildUser']) {
        slackSend (channel: 'jenkins', message: ":black_square_button: "+  env.BUILD_USER +" start deploy Jasper report from "+ sourceServer +" to "+ targetServer)
      }
    }

    stage("Stop Tomcat") {
      sh "ssh $sourceHost \"sudo -u tomcat ${sourceTomcat}/shutdown.sh\""
    }
    try {
          stage("Export files and Update Jasper reports") {
            sh "ssh $sourceHost \"sudo rm -rf ${sourceRootFolder}/gbc-jasper-reports.zip\""
            sh "ssh $sourceHost \"sudo rm -rf ${sourceRootFolder}/resources\""

            sh "ssh $sourceHost \"cd $sourceRootFolder && sudo ./js-export.sh --uris /organizations/ellucian/Reports --output-zip gbc-jasper-reports.zip\""
            sh "ssh $sourceHost \"cd $sourceRootFolder && unzip -o gbc-jasper-reports.zip\""
            sh "ssh $sourceHost \"cd $sourceRootFolder && chmod -R 755 resources\""

            sh "scp $sourceHost:${sourceRootFolder}/resources/organizations/ellucian/Reports/.folder.xml ."
            sh "sed -i '/<\\/folder>/d' .folder.xml"
            println "old folder.xml is below: "
            sh "cat .folder.xml"

            for ( reportName in reportNames) {
              if (userInput[reportName]) {
                folderName = reportName.toLowerCase()
                String LINE1 = "    <folder>${folderName}</folder>"
                sh "grep -q -F '$LINE1' .folder.xml || echo '$LINE1' >> .folder.xml"
                sh "rm -f fileList"
                sh "find ${jrxmlPath}/${folderName} -type f -name '*.jrxml' > listJrxml;"
                list = readFile( "listJrxml" ).split( "\\r?\\n" )
                sh "rm -f listJrxml"
                for (file in list) {
                  def jasperFile = file.toString()
                  fileName = jasperFile.trim().substring(jasperFile.lastIndexOf("/") + 1, jasperFile.lastIndexOf("."))
                  println "fileName: $fileName"
                  println "folderName: $folderName"
                  def ROOTFOLDER = env.WORKSPACE
                  if (fileName.toLowerCase().equals(folderName)) {
                    def folder = folderName.toUpperCase()
                    def dataFolder = "${folder}_files"
                    print "dataFolder: $dataFolder"
                    sh "cp ${jasperFile} main_jrxml.data"
                    sh "scp main_jrxml.data $sourceHost:${sourceReportPath}/${folderName}/${dataFolder}"
                    sh "ssh $sourceHost \"cd $sourceRootFolder && sudo zip gbc-jasper-reports.zip resources/organizations/ellucian/Reports/${folderName}/${dataFolder}/main_jrxml.data\""
                    sh "rm -f main_jrxml.data"
                  } else {
                    folder = folderName.toUpperCase()
                    dataFolder = "${folder}_files"
                    print "dataFolder: $dataFolder"
                    def subFile = "${fileName}.jrxml.data"

                    sh "cp ${jasperFile} ${subFile}"
                    sh "scp ${ROOTFOLDER}/${subFile} $sourceHost:${sourceReportPath}/${folderName}/${dataFolder}"
                    sh "ssh $sourceHost \"cd $sourceRootFolder && sudo zip gbc-jasper-reports.zip resources/organizations/ellucian/Reports/${folderName}/${dataFolder}/${subFile}\""
                    sh "rm -f ${subFile}"
                  }
                }
              }
            }
            String LINE2 = "</folder>"
            sh "grep -q -F 'EXAMPLE-NONEXIST' .folder.xml || echo '$LINE2' >> .folder.xml"
            println "new folder.xml is below: "
            sh "cat .folder.xml"
            sh "scp .folder.xml $sourceHost:${sourceRootFolder}/resources/organizations/ellucian/Reports"
            sh "ssh $sourceHost \"cd $sourceRootFolder && sudo zip gbc-jasper-reports.zip resources/organizations/ellucian/Reports/.folder.xml\""
            sh "rm -rf .folder.xml"

            if (targetEnv == 'devl') {
              sh "ssh $targetHost \"cd $targetRootFolder && sudo ./js-import.sh --input-zip gbc-jasper-reports.zip --update\""
              slackSend (channel: 'jenkins', message: ":ballot_box_with_check: Successfully deploy Jasper report from " + sourceServer + ' to ' + targetServer+"  "+BUILD_URL)
            } else {
              sh "ssh $targetHost \"sudo -u tomcat ${targetTomcat}/shutdown.sh\""
              try {
                  sh "scp $sourceHost:/home/sysadmin/jasperreports-server-6.1.1-bin/buildomatic/gbc-jasper-reports.zip ."
                  sh "scp gbc-jasper-reports.zip $targetHost:${targetRootFolder}"
                  sh "ssh $targetHost \"cd $targetRootFolder && sudo ./js-import.sh --input-zip gbc-jasper-reports.zip --update\""
                  slackSend (channel: 'jenkins', message: ":ballot_box_with_check: Successfully deploy Jasper report from " + sourceServer + ' to ' + targetServer+"  "+BUILD_URL)
              } catch(error) {
                currentBuild.result = 'FAILURE'
                slackSend (channel: 'jenkins', message: ":warning: import Jasper to $targetServer failed, please check logs -> ${BUILD_URL}")
              }
              sh "ssh $targetHost \"sudo -u tomcat ${targetTomcat}/startup.sh\""
            }
          }
    } catch(error) {
      currentBuild.result = 'FAILURE'
      slackSend (channel: 'jenkins', message: ":warning: deploy Jasper failed, please check logs -> ${BUILD_URL}")
    }
    stage("Start Tomcat") {
      sh "ssh $sourceHost \"sudo -u tomcat ${sourceTomcat}/startup.sh\""
    }
  }
}
