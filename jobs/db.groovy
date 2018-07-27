folder('db')

pipelineJob('db/poc-migrate') {
  definition {
    cpsScm {
      scm {
        git('ssh://git@gitrepo.georgebrown.ca:2222/devops/jenkins-dsl.git', 'master')
      }
      scriptPath("pipelines/dev_migrate.groovy")
    }
  }
}
