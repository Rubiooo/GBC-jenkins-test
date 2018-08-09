folder('db')

pipelineJob('db/load_script') {
    configure { project ->
        project << logRotator {
            daysToKeep(-1)
            numToKeep(10)
        }
    }

    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/gbc/gbcbanner.git', 'master')
            }
            scriptPath("build.groovy")
        }
    }
}

pipelineJob('db/promote_scripts') {
    configure { project ->
        project << logRotator {
            daysToKeep(-1)
            numToKeep(10)
        }
    }
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/gbc/gbcbanner.git', 'master')
            }
            scriptPath("promote.groovy")
        }
    }
}
