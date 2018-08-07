node {
  timestamps {
    stage("checkout repo") {
      checkout scm
    }


    stage ("delete older than 60 days") {
      def now = new Date().plus(-60)
      def timestamp=now.format("yyyy-MM-dd")
      print (timestamp)

      sh "sed -i s/DATE/${timestamp}/ aql/aql.json"
      sh "jfrog rt del --spec=aql/aql.json --quiet"
    }

  }
}
