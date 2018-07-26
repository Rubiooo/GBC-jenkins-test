
Add new repo to sync from ellucian

1. update terraform to create new repo

under `terraform` folder, create a file for the new repo.

```
resource "gitlab_group" "ellucian_mobile" {
  name             = "Ellucian mobile"
  path             = "mobile"
  visibility_level = "internal"
  description      = "The source repo from mobile"
  parent_id        = "${gitlab_group.ellucian.id}"
}

module "mobile_EllucianGO" {
  source       = "module/project"
  namespace_id = "${gitlab_group.ellucian_mobile.id}"
  name         = "EllucianGO"
}
```

2. update jenkins job to create the sync job

under `jobs` folder, create afile for the new job

```
import common.BuildJob

BuildJob.sync(this, 'mobile', 'EllucianGO')
```
