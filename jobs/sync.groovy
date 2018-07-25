import common.GlobalVars

folder('repo-sync')

job('repo-sync/test-1') {
  steps {
    shell('
    rm -rf banner_student_faculty_grade_entry_app
    git clone ssh://git@gitrepo.georgebrown.ca:2222/ellucian/banner_student_faculty_grade_entry_app.git
    cd banner_student_faculty_grade_entry_app
    git remote add upstream ssh://git@source.ellucian.com/banner/apps/banner_student_faculty_grade_entry_app.git
    git fetch upstream
    git merge upstream/master
    git push origin master
    ')
  }
}
