# resource "gitlab_group" "ellucian_banner_pages" {
#   name             = "Ellucian banner_pages"
#   path             = "banner_pages"
#   visibility_level = "internal"
#   description      = "The source repo from banner_pages"
#   parent_id        = "${gitlab_group.ellucian.id}"
# }

# # module "banner_pages_alumni" {
# #   source       = "module/project"
# #   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
# #   name         = "alumni"
# # }

# module "banner_pages_arsys" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "arsys"
# }

# module "banner_pages_base" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "base"
# }

# module "banner_pages_bdr" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "bdr"
# }

# module "banner_pages_build" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "build"
# }

# module "banner_pages_common" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "common"
# }

# module "banner_pages_extsol" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "extsol"
# }

# # module "banner_pages_finaid" {
# #   source       = "module/project"
# #   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
# #   name         = "finaid"
# # }

# module "banner_pages_finance" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "finance"
# }

# module "banner_pages_general" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "general"
# }

# module "banner_pages_payroll" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "payroll"
# }

# module "banner_pages_positioncontrol" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "positioncontrol"
# }

# module "banner_pages_reports" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "reports"
# }

# module "banner_pages_student" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "student"
# }

# module "banner_pages_studentaid" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "studentaid"
# }

# module "banner_pages_workspace" {
#   source       = "module/project"
#   namespace_id = "${gitlab_group.ellucian_banner_pages.id}"
#   name         = "workspace"
# }
