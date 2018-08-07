node {
  timestamps {
    stage("prepare env") {
      checkout scm
    }


    stage ("clean up") {
      def now = new Date().plus(-60)
      def timestamp=now.format("yyyy-MM-dd")
      print (timestamp)
      sh "ls -l"
      sh "sed -i s/DATE/${timestamp}/ aql/aql.json"
    }

  }
}
