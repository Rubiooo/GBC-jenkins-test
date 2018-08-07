node {
  timestamps {
    stage("prepare env") {
      dir ("_gbc") {
        checkout([
          $class: 'GitSCM',
          branches: [[name: "master"]],
          userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git']]
          ])
      }
    }


    stage ("clean up") {
      def now = new Date().plus(-60)
      def timestamp=now.format("yyyy-MM-dd")
      print (timestamp)
      sh "ls -l"
      sh "pwd"
    }

  }
}
