node {

    def flyway  = "boxfuse/flyway"

    timestamps {
        stage("prepare env") {
            checkout([
            $class: 'GitSCM',
            branches: [[name: "master"]],
            userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:2222/ellucian/gbc/db-migration.git']]
            ])
            sh "docker pull ${flyway}"
            def conf = input(
             id: 'conf', message: 'environment Name', parameters: [
                choice(choices: "dev.conf\nqa.conf\nprod.conf", description: '', name: 'environment')
             ])

        }

        stage ("run terraform") {
            sh "docker run -t --rm -e FLYWAY_CONFIG_FILES=${conf} -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${flyway} info"
            input message: 'migrate DB?'
            sh "docker run -t --rm -e FLYWAY_CONFIG_FILES=${conf} -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${flyway} migrate"
        }

    }
}
