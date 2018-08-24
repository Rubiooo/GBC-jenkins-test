folder('banner_page_build')

pipelineJob('banner_page_build/build_baseline') {
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/banner_baseline.groovy")
        }
    }
}

pipelineJob('banner_page_build/build_gbc') {
  description('''Build the war files and upload to artifactory.
     <br/>
     scheduled to run everyday afternoon at 4:30pm.
     ''')
    triggers {
        corn('30 16 * * *')
    }
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/banner_gbc.groovy")
        }
    }
}

pipelineJob('banner_page_build/deploy_dev') {
    description('Deploy latest war files to tomcat in DEVL')
    definition {
        cpsScm {
            scm {
                git ('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/deploy_dev.groovy")
        }
    }
}

pipelineJob('banner_page_build/deploy_test') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath('pipelines/deploy_test.groovy')
        }
    }
}

pipelineJob('banner_page_build/deploy_esm') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/deploy_esm.groovy")
        }
    }
}
