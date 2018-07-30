# resource "gitlab_group" "ellucian_banner_apps" {
#   name             = "Ellucian banner_apps"
#   path             = "banner_apps"
#   visibility_level = "internal"
#   description      = "The source repo from banner_apps"
#   parent_id        = "${gitlab_group.ellucian.id}"
# }

# module "banner_apps_application_navigator_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "application_navigator_app"
# }

# module "banner_apps_banner_communication_management_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_communication_management_ssb_app"
# }

# module "banner_apps_banner_document_management_api_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_document_management_api_app"
# }

# module "banner_apps_banner_employee_profile_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_employee_profile_ssb_app"
# }

# module "banner_apps_banner_employee_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_employee_ssb_app"
# }

# module "banner_apps_banner_extensibility" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_extensibility"
# }

# module "banner_apps_banner_faculty_attendance_tracking_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_faculty_attendance_tracking_ssb_app"
# }

# module "banner_apps_banner_finance_procurement_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_finance_procurement_ssb_app"
# }

# module "banner_apps_banner_finance_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_finance_ssb_app"
# }

# module "banner_apps_banner_general_events_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_general_events_app"
# }

# module "banner_apps_banner_general_events_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_general_events_ssb_app"
# }

# module "banner_apps_banner_general_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_general_ssb_app"
# }

# module "banner_apps_banner_integration_api_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_integration_api_app"
# }

# module "banner_apps_banner_position_desc_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_position_desc_ssb_app"
# }

# module "banner_apps_banner_student_advisor_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_advisor_ssb_app"
# }

# module "banner_apps_banner_student_api_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_api_app"
# }

# module "banner_apps_banner_student_attendance_tracking_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_attendance_tracking_app"
# }

# module "banner_apps_banner_student_attendance_tracking_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_attendance_tracking_ssb_app"
# }

# module "banner_apps_banner_student_catalog_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_catalog_app"
# }

# module "banner_apps_banner_student_etranscript_api" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_etranscript_api"
# }

# module "banner_apps_banner_student_faculty_grade_entry_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_faculty_grade_entry_app"
# }

# module "banner_apps_banner_student_generalstudent_history_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_generalstudent_history_app"
# }

# module "banner_apps_banner_student_overall_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_overall_app"
# }

# module "banner_apps_banner_student_registration_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_registration_app"
# }

# module "banner_apps_banner_student_registration_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_registration_ssb_app"
# }

# module "banner_apps_banner_student_schedule_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_schedule_app"
# }

# module "banner_apps_banner_student_ssb_app" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
#   name         = "banner_student_ssb_app"
# }

# # module "banner_apps_banner_student_success_api" {
# #   source       = "module/project"
# #   namespace_id = "${gitlab_group.ellucian_banner_apps.id}"
# #   name         = "banner_student_success_api"
# # }
