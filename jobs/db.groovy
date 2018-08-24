folder('scripts_promotion')

pipelineJob('scripts_promotion/load_script') {
    description('load scripts to environment')
    clogRotator(2, 10, -1, -1)
    authorization {
       permissionAll('M180xxx') //Max
       permissionAll('50xxx') //Ibrah
       blocksInheritance()
   }
   parameters {
        choiceParam('Environment', ['devl', 'test', 'prod', 'prds'])
        stringParam('Username', 'jenkins', 'db username')
        nonStoredPasswordParam('Password', 'db password')
        booleanParam('DryRun', true)
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
    description('promotion scripts, create pull request for review.')
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
