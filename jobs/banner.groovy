folder('banner_build')

pipelineJob('banner_build/baseline') {
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages.git', 'master')
            }
            scriptPath("pipelines/banner_baseline.groovy")
        }
    }
}
