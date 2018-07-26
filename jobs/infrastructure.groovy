folder('infrastructure')

pipelineJob('infrastructure/gitlab') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:2222/devops/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/gitlab.groovy")
        }
    }
    configure {
        it / definition / lightweight(true)
    }
}
