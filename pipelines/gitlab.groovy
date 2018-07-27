node {

    def terraformImage  = "georgebrown/terraform"

    timestamps {
        stage("prepare env") {
            checkout scm

            sh "docker pull ${terraformImage}"
        }

        stage ("run terraform") {

            sh "docker run -t --rm -v ${WORKSPACE}/terraform:/app ${terraformImage} terraform init"
            sh "docker run -t --rm -v ${WORKSPACE}/terraform:/app ${terraformImage} terraform plan -no-color"
            sh "docker run -t --rm -v ${WORKSPACE}/terraform:/app ${terraformImage} terraform apply -auto-approve -no-color"
        }

    }
}
