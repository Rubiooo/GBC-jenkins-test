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
        choiceParam('env',["dev","qa","prod"], "environment name?")
        booleanParam('dryrun', true, "dry run?")
    }

    steps {
        shell('''
export FLYWAYIMAGE=boxfuse/flyway

if [[ $dryrun == true ]]; then
    docker run -t --rm -e FLYWAY_CONFIG_FILES=conf/${env}.conf -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${FLYWAYIMAGE} info

else
    docker run -t --rm -e FLYWAY_CONFIG_FILES=conf/${env}.conf -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${FLYWAYIMAGE} info
    docker run -t --rm -e FLYWAY_CONFIG_FILES=conf/${env}.conf -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${FLYWAYIMAGE} migrate
    docker run -t --rm -e FLYWAY_CONFIG_FILES=conf/${env}.conf -v ${WORKSPACE}/sql:/flyway/sql -v ${WORKSPACE}/conf:/flyway/conf ${FLYWAYIMAGE} info
fi
        ''')

    }

}
