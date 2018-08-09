folder('db')

pipelineJob('db/load_script') {
    clogRotator(2, 10, -1, -1)

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
    logRotator(2, 10, -1, -1)
    
    definition {
        cpsScm {
            scm {
                git('ssh://git@gitrepo.georgebrown.ca:7999/gbc/gbcbanner.git', 'master')
            }
            scriptPath("promote.groovy")
        }
    }
}
