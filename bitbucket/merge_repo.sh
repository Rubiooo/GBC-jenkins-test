#!/bin/bash

REPO_ROOT="ssh://git@gitrepo.georgebrown.ca:7999/el/"
PROJECT="banner_pages"

rm -rf ${PROJECT}

while read -r line; do
  if [ "$(echo ${line} |grep -E -c "^#")" -eq 1 ]; then continue; fi
  IFS=', ' read repo tag <<< "${line}"
  rm -rf ${PROJECT}/${repo}
  git clone --branch ${tag} ${REPO_ROOT}${PROJECT}_${repo}.git ${PROJECT}/${repo}
  rm -rf ${PROJECT}/${repo}/.git
done < "${PROJECT}.csv"

cd ${PROJECT}
rm -rf .git
git init
git add --all
git commit -m "Merge Commit"
git remote add origin ssh://git@gitrepo.georgebrown.ca:7999/gbc/${PROJECT}.git
git push -u origin master --force
