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
git clone ssh://git@gitrepo.georgebrown.ca:2222/ellucian/${foldername}/${reponame}.git
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
