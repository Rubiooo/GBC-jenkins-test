import common.GlobalVars

folder('repo-sync')

BuildFramework.sync(this, 'banner/apps', 'application_navigator_app')

class BuildFramework {
  static sync(dslFactory, repofoler, reponame) {
    dslFactory.job("repo-sync/${reponame}") {
      triggers {
        cron(GlobalVars.scheduleEveryDay)
      }
      steps {
        shell(""" echo ${reponame}
          rm -rf ${reponame}
          git clone ssh://git@gitrepo.georgebrown.ca:2222/ellucian/${reponame}.git
          cd ${reponame}
          git remote add upstream ssh://git@source.ellucian.com/${repofolder}/${reponame}.git
          git fetch upstream
          git merge upstream/master
          git push origin master
        """)
      }
    }
  }
}
