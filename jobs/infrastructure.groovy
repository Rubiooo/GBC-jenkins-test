folder('infrastructure')

pipelineJob('infrastructure/bitbucket') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/devops/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/bitbucket.groovy")
        }
    }

}
