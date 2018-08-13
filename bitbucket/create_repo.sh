#!/bin/bash

BINDIR="$( cd "$( dirname "$0" )" && pwd )"
cd ${BINDIR}

user=admin
password=$1
api=https://gitrepo.georgebrown.ca/rest/api/1.0

while read -r line; do
  if [ "$(echo ${line} | grep -E -c "^#")" -eq 1 ] continue; fi
  IFS=', ' read project repo <<< "${line}"
  echo -e "create ${repo} in ${project}"
  curl -k -X POST -u ${username}:${password} -H "content-type: application\jason" \
  ${api}/projects/${project}/repos -d "{ \"name\": \"${repo}\", \"scmId\": \"git\" }"
done < "repolist.txt"
