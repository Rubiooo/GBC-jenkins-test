node {

    timestamps {
        stage ("run create repos") {

            def repos = [
'banner_apps_application_navigator_app',
'banner_apps_banner_communication_management_ssb_app',
'banner_apps_banner_document_management_api_app',
'banner_apps_banner_employee_profile_ssb_app',
'banner_apps_banner_employee_ssb_app',
'banner_apps_banner_extensibility',
'banner_apps_banner_faculty_attendance_tracking_ssb_app',
'banner_apps_banner_finance_procurement_ssb_app',
'banner_apps_banner_finance_ssb_app',
'banner_apps_banner_general_events_app',
'banner_apps_banner_general_events_ssb_app',
'banner_apps_banner_general_ssb_app',
'banner_apps_banner_integration_api_app',
'banner_apps_banner_position_desc_ssb_app',
'banner_apps_banner_student_advisor_ssb_app',
'banner_apps_banner_student_api_app',
'banner_apps_banner_student_attendance_tracking_app',
'banner_apps_banner_student_attendance_tracking_ssb_app',
'banner_apps_banner_student_catalog_app',
'banner_apps_banner_student_etranscript_api',
'banner_apps_banner_student_faculty_grade_entry_app',
'banner_apps_banner_student_generalstudent_history_app',
'banner_apps_banner_student_overall_app',
'banner_apps_banner_student_registration_app',
'banner_apps_banner_student_registration_ssb_app',
'banner_apps_banner_student_schedule_app',
'banner_apps_banner_student_ssb_app',
]
            repos.each{ repo ->
                def command = /curl -k -X POST -v -u usernameXXX:passwordXXX -H \"Content-Type: application\/json\" https://gitrepo.georgebrown.ca/rest/api/1.0/projects/EL/repos  -d \"{\"name\": \"$repo\",\"scmId\": \"git\" }\"/
                print $command
                sh "$command"

            }
        }
    }

}
