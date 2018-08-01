node {

    timestamps {
        stage('prepare env') {
     checkout([
        $class: 'GitSCM'
        branches: [[name: "master"]]
        userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages.git']]        
     ])
        }

        stage('update pom') {
            sh "ls -l"
        }
    }
}
