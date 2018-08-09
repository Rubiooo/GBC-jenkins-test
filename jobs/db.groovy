folder('db')

pipelineJob('db/load_script') {
    clogRotator(2, 10, -1, -1)
    authorization {
       permissionAll('M180xxx') //Max
       permissionAll('50xxx') //Ibrah
   }


    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')

            }
            scriptPath("pipelines/database/load.groovy")
        }
    }
}

pipelineJob('db/promote_script') {
    logRotator(2, 10, -1, -1)
    authorization {
      permissionAll('M180xxx') //Max
      permissionAll('50xxx') //Ibrah
    }
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')

            }
            scriptPath("pipelines/database/promote.groovy")
        }
    }
}
