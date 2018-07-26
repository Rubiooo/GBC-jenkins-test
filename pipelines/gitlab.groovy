node {

    def terraformImage  = "georgebrown/terraform"

    timestamps {
        stage("prepare env") {
            checkout scm
            sh "docker pull ${terraformImage}"
        }

        stage ("run terraform") {
            sh "docker run -t --rm -v ${WORKSPACE}:/app ${terraformImage} terraform init"
        }

    }
}
