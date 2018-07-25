variable "namespace_id" {}

variable "name" {}

resource "gitlab_project" "example" {
  name             = "${var.name}"
  description      = "Ellucian sync repo for ${var.name}"
  namespace_id     = "${var.namespace_id}"
  issues_enabled   = false
  wiki_enabled     = false
  snippets_enabled = false
  visibility_level = "internal"

  # default_branch   = "master"
}
