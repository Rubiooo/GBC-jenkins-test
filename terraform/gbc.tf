# resource "gitlab_group" "gbc" {
#   name             = "GBC customization"
#   path             = "gbc"
#   visibility_level = "internal"
#   description      = "The source repo from GBC customization"
#   parent_id        = "${gitlab_group.ellucian.id}"
# }

# resource "gitlab_project" "db-migration" {
#   name             = "db-migration"
#   description      = "The source repo from GBC DB migration"
#   namespace_id     = "${gitlab_group.gbc.id}"
#   issues_enabled   = false
#   wiki_enabled     = false
#   snippets_enabled = false
#   visibility_level = "internal"

#   lifecycle {
#     ignore_changes = "default_branch"
#   }
# }
