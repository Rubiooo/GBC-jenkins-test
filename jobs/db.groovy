folder('db')

pipelineJob('db/load_script') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/gbc/gbcbanner.git', 'master')
            }
            scriptPath(pipelines/build.groovy)
        }
    }
}

pipelineJob('db/promote_scripts') {
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/gbc/gbcbanner.git', 'master')
            }
            scriptPath("promote.groovy")
        }
    }
}
