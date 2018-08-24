folder('maintenance')

pipelineJob('maintenance/cleanup_artifactory') {
    description('delete build result in build-gbc folder old than 60 days in artifactory')
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/cleanup_artifactory.groovy")
        }
    }
}
