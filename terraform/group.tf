# resource "gitlab_group" "ellucian" {
#   name             = "ellucian"
#   path             = "ellucian"
#   visibility_level = "internal"
#   description      = "An ellucian group"
# }

resource "bitbucket_repository" "infrastructure" {
  owner = "admin"
  name  = "terraform-code"
}
