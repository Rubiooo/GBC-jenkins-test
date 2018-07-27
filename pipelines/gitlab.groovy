node {

    def terraformImage  = "georgebrown/terraform"

    timestamps {
        stage("prepare env") {
            //checkout scm
            checkout([$class: 'GitSCM',
            branches: [[name: 'master']],
            userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:2222/devops/jenkins-dsl.git']]
            ])

            sh "docker pull ${terraformImage}"
        }

        stage ("run terraform") {
            sh "docker run -t --rm -v ${WORKSPACE}:/app ${terraformImage} terraform init"
        }

    }
}
