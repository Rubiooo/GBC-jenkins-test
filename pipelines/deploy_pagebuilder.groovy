node {

  def environment="devl\ntest\nprod\nprds"
  def targetHosts = ['devl':'Ban9pb01d.gbcdev.local',
  'test':'Ban9pb01d.gbcdev.local',
  'prod':'Ban9pb01d.gbcdev.local',
  'prds':'Ban9pb01d.gbcdev.local']

  def fileList = []
  def list
  def pageName
  def fileName
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
    }


    stage("Choose Pages") {
      scm=checkout([
      $class: 'GitSCM',
      branches: [[name: "master"]],
      userRemoteConfigs: [[url: "ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_extensibility.git"]]
      ])

      sh "rm -f fileList"
      sh "find . -type f -name '*.json' > listJson;"
      list = readFile( "listJson" ).split( "\\r?\\n" )
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
          fileName = line.trim().substring(line.lastIndexOf("/") + 1)
          println "fileName: $fileName"
          prefix = fileName.trim().substring(0, fileName.indexOf("."))
          println "prefix: $prefix"
        }
      }
    }


  }
}
