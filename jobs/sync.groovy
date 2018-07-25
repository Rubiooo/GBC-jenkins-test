import common.GlobalVars

folder('repo-sync')

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

BuildFramework.sync(this, 'banner/apps', 'application_navigator_app')
BuildFramework.sync(this, 'banner/apps', 'banner_communication_management_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_document_management_api_app')
BuildFramework.sync(this, 'banner/apps', 'banner_employee_profile_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_employee_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_extensibility')
BuildFramework.sync(this, 'banner/apps', 'banner_faculty_attendance_tracking_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_finance_procurement_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_finance_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_general_events_app')
BuildFramework.sync(this, 'banner/apps', 'banner_general_events_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_general_ssb_app')
BuildFramework.sync(this, 'banner/apps', 'banner_integration_api_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_position_desc_ssb_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_advisor_ssb_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_api_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_attendance_tracking_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_attendance_tracking_ssb_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_catalog_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_etranscript_api')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_faculty_grade_entry_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_generalstudent_history_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_overall_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_registration_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_registration_ssb_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_schedule_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_ssb_app')
// BuildFramework.sync(this, 'banner/apps', 'banner_student_success_api')
