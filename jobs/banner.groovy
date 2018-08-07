folder('banner_build')

pipelineJob('banner_build/build_baseline') {
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/banner_baseline.groovy")
        }
    }
}

pipelineJob('banner_build/build_gbc') {
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/banner_gbc.groovy")
        }
    }
}

pipelineJob('banner_build/deploy_dev') {
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/deploy_dev.groovy")
        }
    }
}

pipelineJob('banner_build/deploy_test') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath('pipelines/deploy_test.groovy')
        }
    }
}
