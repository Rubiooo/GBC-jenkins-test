folder('banner_build')

pipelineJob('banner_build/baseline') {
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/banner_baseline.groovy")
        }
    }
}
