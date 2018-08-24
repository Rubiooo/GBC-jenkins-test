folder('scheduled_job')
pipelineJob('scheduled_job/devl_nightly') {
    description('''load script to devl and deploy war to devl every morning at 2am
''')
    triggers {
        cron('H 2 * * *')
    }
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
            }
            scriptPath("pipelines/scheduled/auto_devl.groovy")
        }
    }
}
