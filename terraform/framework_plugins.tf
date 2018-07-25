resource "gitlab_group" "ellucian_framework_plugins" {
  name             = "Ellucian framework_plugins"
  path             = "ellucian_framework_plugins"
  visibility_level = "internal"
  description      = "The source repo from ellucian_framework_plugins"
}

module "framework_plugins_banner-sspb" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_framework_plugins.id}"
  name         = "banner-sspb"
}

module "framework_plugins_seamless" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_framework_plugins.id}"
  name         = "seamless"
}

module "framework_plugins_webify" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_framework_plugins.id}"
  name         = "webify"
}
