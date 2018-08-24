node {

  def repo = 'banner_pages_gbc'
  def directory = new File(repo)
  def filePath
  def fileName
  def list = []
  def fileList = []
  def userInput

  def environment="devl\ntest\nprod\nprds"
  def jasperList = ['devl':'http://ban9appnav01d.georgebrown.ca:7081/jasperserver-pro',
  'test':'https://ban9pb01u.georgebrown.ca:9443/jasperserver-pro',
  'prod':'ban9appnav01d.gbcdev.local',
  'prds':'ban9appnav01d.gbcdev.local']
  def jasperPathList = ['devl':'organizations/ellucian/Reports/',
  'test':'/tmp/TEST',
  'prod':'/tmp/PROD',
  'prds':'/tmp/PRDS']
  def dataSourcePathList = ['devl':'organizations/ellucian/',
  'test':'/tmp/TEST',
  'prod':'/tmp/PROD',
  'prds':'/tmp/PRDS']
  def dataSource = 'bannerDB'

  def jasperHost
  def jasperPath
  def url

  timestamps {
    stage("checkout JRXML") {
      sh "rm -rf banner_pages_gbc"
      // scm=checkout([
      //   $class: 'GitSCM',
      //   branches: [[name: "master"]],
      //   userRemoteConfigs: [[url: "ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git"]]
      // ])
      sh "git clone ssh://git@gitrepo.georgebrown.ca:7999/gbc/${repo}.git"
      if (!directory.isDirectory())
      {
        println "The provided directory name ${repo} is NOT a directory."
      }
      println "Searching for jrxml files in directory ${repo}"
    }
    stage("upload JRXML files") {
      sh "rm -rf list"
      directory.eachDirRecurse() { dir ->
          dir.eachFileMatch(~/.*.jrxml/) { file ->
              filePath = file.toString()
              list.add(filePath)
          }
      }
      for (line in list) {
          fileName=line.trim().substring(line.lastIndexOf("/") + 1, line.lastIndexOf("."))
          fileList.add(booleanParam(defaultValue: false, description: '', name: fileName))
      }

      userInput = input(
       id: 'userInput', message: 'upload JRXML', parameters: [
        choice(choices: environment, description: '', name: 'env'),
        choice(choices: fileList, description: 'JRXML files', name: 'file'),
        string(defaultValue: 'superuser', description: 'jasper username', name: 'username', trim: true),
        password(defaultValue: '', description: 'jasper password', name: 'password'),
        ])

      for (name in fileList) {
        if (userInput[file]) {
          //println(name)
          String jrxml_data
          def foldername
          def dataSourcePath
          directory.eachDirRecurse() { dir ->
              dir.eachFileMatch(~/${name}.jrxml/) { file ->
                  filePath = file.toString()
                  folderName = name.toLowerCase()
                  // upload each jrxml to jasper server
                  jrxml_data = new File(filePath).text


                  jasperHost=jasperList[userInput['env']]
                  jasperPath=jasperPathList[userInput['env']]+"${folderName}"
                  url="${jasperHost}"+'/rest_v2/resources/'+"${jasperPath}"
                  dataSourcePath=dataSourcePathList[userInput['env']]

                  try {
                    sh "curl  -X POST \"${url}\" \
                        -H \"Content-Type:application/jrxml\" \
                        -H \"Content-Disposition:attachment; filename=${name}\" \
                        -d \"${jrxml_data}\" \
                        --user userInput['username']:userInput['password']"
                  } catch(ERROR) {
                    throw "file already exist"
                  }

              }
          }


          // sh "curl -X DELETE ${JasperHost}/rest_v2/resources/${JasperPath} \
          //     --user userInput['username']:userInput['password']"

          // sh "curl  -X POST ${JasperHost}/rest_v2/resources/${JasperPath} \
          //     -H \"Content-Type:application/repository.reportUnit+json\" \
          //     -d \"{"uri": "/${foldername}/${fileName}.toUpperCase()","label": "${fileName}.toUpperCase()","description": "",  "permissionMask": "0", "version": "0" , "alwaysPromptControls": "true","controlsLayout": "popupScreen", "jrxml": {"jrxmlFileReference": { "uri": "/${foldername}/${fileName}"} },"dataSource": {"dataSourceReference": { "uri": "${dataSourcePath}/${dataSource}"}}}\" \
          //     --user userInput['username']:userInput['password']"
        }
      }
    }
  }
}



//banner_pages_gbc/gbc-student-reports/src/main/java/net/hedtech/banner/gbc/student/reports/szrregblcky/SZRREGBLCKY.jrxml
// curl -X POST http://ban9appnav01d.georgebrown.ca:7081/jasperserver-pro/rest_v2/resources/organizations/ellucian/Reports/test1 -H "Content-Type:application/jrxml" -H "Content-Disposition:attachment; fileName=test1" -d "" --user superuser:superuser
