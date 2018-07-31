
Add new repo to sync from ellucian

1. update repolist.txt to create new repo


```
# ./bitbucket/create_repo.sh
```



2. update jenkins job to create the sync job

under `jobs` folder, create a file for the new job

```
import common.BuildJob

BuildJob.sync(this, 'mobile', 'EllucianGO')
```
