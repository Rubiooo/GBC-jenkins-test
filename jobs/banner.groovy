folder('banner_page_build')

pipelineJob('banner_page_build/build_baseline') {
  description('''Build banner_baseline
   - purpose: build war files of banner_baseline and upload to artifactory
   step 1: checkout banner_pages to current root
   step 2: checkout banner_pages_fix and copy its files to overwrite current root
   step 3: build war files and upload to artifactory''')
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
    description('''Build banner_gbc scheduled to run everyday afternoon at 4:30pm.
       - purpose: build jar file of banner_gbc and upload to artifcatory
       step 1: checkout banner_pages to current root
       step 2: checkout banner_pages_gbc and copy its files to overwrite current root
       step 3: unzip devl/wrksp.ws and copy to dist/devl.wrksp.ws.tar.gz
       step 4: build jar files and upload to artifactory
       step 5: scheduled to run at 4:30pm everyday
''')
    triggers {
        cron('30 16 * * *')
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
    description('''Deploy latest war files to tomcat in DEVL
    - step 1: stop tomcat
    - step 2: unzip devl.wrksp.ws.tar.gz and update wrksp.ws.war WEB-INF
    - step 3: clean tomcat folders
    - step 4: copy the latest war files to tomcat folders
    - step 5: start tomcat''')
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
    description("")
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
