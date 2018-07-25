resource "gitlab_group" "ellucian_banner_plugins" {
  name             = "Ellucian banner_plugins"
  path             = "ellucian_banner_plugins"
  visibility_level = "internal"
  description      = "The source repo from ellucian_banner_plugins"
}

module "banner_plugins_banner-api" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner-api"
}

module "banner_plugins_banner-restful-api-support" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner-restful-api-support"
}

module "banner_plugins_banner_accountsreceivable_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_accountsreceivable_common"
}

module "banner_plugins_banner_accountsreceivable_validation_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_accountsreceivable_validation_common"
}

module "banner_plugins_banner_aurora" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_aurora"
}

module "banner_plugins_banner_baseline_dbupgrade" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_baseline_dbupgrade"
}

module "banner_plugins_banner_codenarc" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_codenarc"
}

module "banner_plugins_banner_common_api" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_common_api"
}

module "banner_plugins_banner_core" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_core"
}

module "banner_plugins_banner_db_main" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_db_main"
}

module "banner_plugins_banner_document_management" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_document_management"
}

module "banner_plugins_banner_finaid_validation" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finaid_validation"
}

module "banner_plugins_banner_finance_api" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_api"
}

module "banner_plugins_banner_finance_budget_availability" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_budget_availability"
}

module "banner_plugins_banner_finance_budget_availability_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_budget_availability_ui"
}

module "banner_plugins_banner_finance_budget_availability_validation" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_budget_availability_validation"
}

module "banner_plugins_banner_finance_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_common"
}

module "banner_plugins_banner_finance_general_ledger" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_general_ledger"
}

module "banner_plugins_banner_finance_procurement" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_procurement"
}

module "banner_plugins_banner_finance_procurement_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_procurement_ui"
}

module "banner_plugins_banner_finance_validation" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_validation"
}

module "banner_plugins_banner_finance_validation_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_finance_validation_common"
}

module "banner_plugins_banner_general_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_common"
}

module "banner_plugins_banner_general_common_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_common_ui"
}

module "banner_plugins_banner_general_common_ui_ss" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_common_ui_ss"
}

module "banner_plugins_banner_general_direct_deposit_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_direct_deposit_ui"
}

module "banner_plugins_banner_general_events" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_events"
}

module "banner_plugins_banner_general_person" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_person"
}

module "banner_plugins_banner_general_person_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_person_ui"
}

module "banner_plugins_banner_general_personal_information_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_personal_information_ui"
}

module "banner_plugins_banner_general_utility" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_utility"
}

module "banner_plugins_banner_general_validation_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_validation_common"
}

module "banner_plugins_banner_general_validation_common_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_general_validation_common_ui"
}

module "banner_plugins_banner_hr_effort_reporting_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_hr_effort_reporting_ui"
}

module "banner_plugins_banner_hr_employee_profile_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_hr_employee_profile_ui"
}

module "banner_plugins_banner_hr_labor_redistribution_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_hr_labor_redistribution_ui"
}

module "banner_plugins_banner_hr_position_description_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_hr_position_description_ui"
}

module "banner_plugins_banner_packaging" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_packaging"
}

module "banner_plugins_banner_payroll_api" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_payroll_api"
}

module "banner_plugins_banner_payroll_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_payroll_common"
}

module "banner_plugins_banner_posnctl_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_posnctl_common"
}

module "banner_plugins_banner_seeddata_catalog" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_seeddata_catalog"
}

module "banner_plugins_banner_selenium_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_selenium_common"
}

module "banner_plugins_banner_spring_security_cas" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_spring_security_cas"
}

module "banner_plugins_banner_spring_security_saml" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_spring_security_saml"
}

module "banner_plugins_banner_student_admissions" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_admissions"
}

module "banner_plugins_banner_student_advising_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_advising_ui"
}

module "banner_plugins_banner_student_api" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_api"
}

module "banner_plugins_banner_student_attendance_tracking" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_attendance_tracking"
}

module "banner_plugins_banner_student_attr_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_attr_ui"
}

module "banner_plugins_banner_student_capp" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_capp"
}

module "banner_plugins_banner_student_catalog" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_catalog"
}

module "banner_plugins_banner_student_common" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_common"
}

module "banner_plugins_banner_student_common_ss_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_common_ss_ui"
}

module "banner_plugins_banner_student_common_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_common_ui"
}

module "banner_plugins_banner_student_faculty" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_faculty"
}

module "banner_plugins_banner_student_faculty_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_faculty_ui"
}

module "banner_plugins_banner_student_generalstudent" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_generalstudent"
}

module "banner_plugins_banner_student_history" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_history"
}

module "banner_plugins_banner_student_recruiting" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_recruiting"
}

module "banner_plugins_banner_student_registration" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_registration"
}

module "banner_plugins_banner_student_schedule" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_schedule"
}

module "banner_plugins_banner_student_ssb_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_ssb_ui"
}

module "banner_plugins_banner_student_validation" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_student_validation"
}

module "banner_plugins_banner_ui" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_ui"
}

module "banner_plugins_banner_ui_ss" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_ui_ss"
}

module "banner_plugins_banner_xe_dbupgrade" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "banner_xe_dbupgrade"
}

module "banner_plugins_domain_extension" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "domain_extension"
}

module "banner_plugins_grails-constraints" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "grails-constraints"
}

module "banner_plugins_i18n_core" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "i18n_core"
}

module "banner_plugins_restful-api" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "restful-api"
}

module "banner_plugins_seamless" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "seamless"
}

module "banner_plugins_sghe_aurora" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "sghe_aurora"
}

module "banner_plugins_sghe_zk_core" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "sghe_zk_core"
}

module "banner_plugins_spring_security_cas" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "spring_security_cas"
}

module "banner_plugins_spring_security_saml" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "spring_security_saml"
}

module "banner_plugins_web-app-extensibility" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "web-app-extensibility"
}

module "banner_plugins_webify" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_banner_plugins.id}"
  name         = "webify"
}
