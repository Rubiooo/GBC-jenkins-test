folder('db')

job("db/poc-migrate") {
    scm {
        git {
            remote {
                name('origin')
                url('ssh://git@gitrepo.georgebrown.ca:2222/ellucian/gbc/db-migration.git')
            }
            branch('master')
        }
    }


    parameters {
        choiceParam('env',["dev","qa","prod"])
        booleanParam('dryrun', true)
    }

    steps {
        shell('''
            docker run -t --rm -e FLYWAY_CONFIG_FILES=${env}.conf -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${flyway} info
            if [[ $dryrun == false ]]; then
                docker run -t --rm -e FLYWAY_CONFIG_FILES=${env}.conf -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${flyway} migrate
            fi

        ''')

    }

}
