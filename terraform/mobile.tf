resource "gitlab_group" "ellucian_mobile" {
  name             = "Ellucian mobile"
  path             = "ellucian_mobile"
  visibility_level = "internal"
  description      = "The source repo from ellucian_mobile"
}

module "mobile_EllucianGO" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_mobile.id}"
  name         = "EllucianGO"
}

module "mobile_MobileServer" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_mobile.id}"
  name         = "MobileServer"
}

module "mobile_ms-configuration-core" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_mobile.id}"
  name         = "ms-configuration-core"
}

module "mobile_ms-groovy" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_mobile.id}"
  name         = "ms-groovy"
}

module "mobile_ms-notification-core" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_mobile.id}"
  name         = "ms-notification-core"
}
