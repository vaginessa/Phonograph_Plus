#!/usr/bin/env bash
#
# Copyright (c) 2022 chr_56
#

BUILD_VARIANT="$1"

##
## Build
##
assemble() {
  WORK_DIR=$(pwd)
  echo "Start Building"
  cd ..
  ./gradlew "assemble$BUILD_VARIANT" --stacktrace --parallel
  cd "$WORK_DIR" || exit
  echo "Building Completed!"
}

assemble
