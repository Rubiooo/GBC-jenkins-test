folder('maintenance')

pipelineJob('maintenance/cleanup_artifactory') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/cleanup_artifactory.groovy")
        }
    }
}
