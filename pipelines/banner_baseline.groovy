node {

    timestamps {
        stage('prepare env') {
            checkout scm
        }

        stage('update pom') {
            sh "ls -l"
        }
    }
}
