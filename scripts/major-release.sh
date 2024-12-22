#!/bin/bash

function get_mvn_project_version() {
  ./mvnw -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec
}

if ! [[ "$(git rev-parse --abbrev-ref HEAD)" = "main" ]]; then
  printf "You can only run the script in main!"
  exit 1
fi

if [[ -n "$(git status --porcelain)" ]]; then
  echo "There are uncommited/untracked files in the repository"
  git status -s
  echo "Please save and commit changes before continuing"
  exit 1
fi

cd ..

proj_ver=$(get_mvn_project_version)

old_maj_ver="$(cut -d '.' -f 1 <<< "$proj_ver")"
new_proj_ver="$((old_maj_ver+1)).0"

read -rp "Current project version is $proj_ver, update to $new_proj_ver? [y/N] " response
if ! [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
  echo "Operation cancelled"
  exit 1
fi

./mvnw versions:set -q -Dexec.executable="echo" -DnewVersion="$new_proj_ver"
rm pom.xml.versionsBackup
echo "Successfully bumped version to $new_proj_ver!"

git add pom.xml
git commit -qm "release: :bookmark: with new version $new_proj_ver"
git push -q