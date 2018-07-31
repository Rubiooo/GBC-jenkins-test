package common

class BuildJob {
  static sync(dslFactory, repofolder, reponame) {
    def foldername=repofolder.replaceAll('/','_')
    dslFactory.folder("repo-sync/${foldername}")
    dslFactory.job("repo-sync/${foldername}/${reponame}") {
      triggers {
        cron(GlobalVars.scheduleEveryDay)
      }
      steps {
        shell(""" echo ${reponame}
rm -rf ${reponame}
git clone -b master ssh://git@source.ellucian.com/${repofolder}/${reponame}.git
cd ${reponame}
git remote add local ssh://git@gitrepo.georgebrown.ca:7999/el/${foldername}_${reponame}.git
git push local master --force
""")
      }
    }

  }
}
