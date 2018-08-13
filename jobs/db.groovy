folder('scripts_promotion')

pipelineJob('scripts_promotion/load_script') {
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

pipelineJob('scripts_promotion/git_promote') {
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
