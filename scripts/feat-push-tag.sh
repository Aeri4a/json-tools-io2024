#!/bin/sh

cd ..
proj_ver="v$(./mvnw -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec)"
prev_tag=$(git describe --abbrev=0)
if ! [ "$proj_ver" = "$prev_tag" ]; then
  git tag -a "$proj_ver" -m "$proj_ver"
  git push --tags -q
fi