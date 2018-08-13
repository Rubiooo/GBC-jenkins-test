freeStyleJob('repo-sync/merge_banner_pages') {
    logRotator(-1, 10)
    scm {
        git('ssh://git@gitrepo.georgebrown.ca:7999/dev/jenkins-dsl.git', 'master')
    }
    steps {
        shell('./bitbucket/merge_repo.sh')
    }
}
