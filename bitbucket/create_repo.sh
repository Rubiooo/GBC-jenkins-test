#!/bin/bash

user=admin
password=XXXX
api=https://gitrepo.georgebrown.ca/rest/api/1.0

while read -r line; do
  if [ "$(echo ${line} |egrep -c "^#")" -eq 1]; then continue; fi
  IFS=', ' read project repo <<< "${line}"
  echo -e "create ${repo} in ${project}"
  curl -k -X POST -u ${user}:${password} -H "Content-Type: application/json" \
    ${api}/projects/${project}/repos  -d "{\"name\": \"${repo}\",\"scmId\": \"git\" }"
done < "repolist.txt"
