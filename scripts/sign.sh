#!/usr/bin/env bash
#
# Copyright (c) 2022 chr_56
#

INPUT_APK_FILE="$1"

KS_SIGNING_KEY_FILE="$2"
KS_PASSWORD="$3"
KEY_ALIAS="$4"
KEY_PASSWORD="$5"

OUTPUT_APK_FILE="$6"

BUILD_TOOL_VERSION="33.0.0"

##
## Util
##
is_windows() {
  UNAME="$(uname -o)"
  if [ "$UNAME" == "Msys" ] || [ "$UNAME" == "Cygwin" ]; then
    echo 1
    return 1
  else
    echo 0
    return 0
  fi
}

##
## Zipalign
##
zip_align_apk() {

  R=$(is_windows)
  if [ $R -eq 1 ]; then
    ZIPALIGN_NAME="zipalign.exe"
  else
    ZIPALIGN_NAME="zipalign"
  fi
  ZIPALIGN_PATH="$ANDROID_SDK_ROOT/build-tools/$BUILD_TOOL_VERSION/$ZIPALIGN_NAME"
  echo "### Apksigner path: $ZIPALIGN_PATH"

  echo "Start Zipalign..."
  $ZIPALIGN_PATH -f -v 4 "$1" "$2"
  echo "Zipalign Completed"
}

##
## Signing
##
sign_apk() {

  R=$(is_windows)
  if [ $R -eq 1 ]; then
    APKSIGNER_NAME="apksigner.bat"
  else
    APKSIGNER_NAME="apksigner"
  fi
  APKSIGNER_PATH="$ANDROID_SDK_ROOT/build-tools/$BUILD_TOOL_VERSION/$APKSIGNER_NAME"

  echo "### Apksigner path: $APKSIGNER_PATH"

  echo "Start Signing..."
  $APKSIGNER_PATH \
    sign --verbose \
    --ks "$KS_SIGNING_KEY_FILE" \
    --ks-pass pass:"$KS_PASSWORD" \
    --ks-key-alias "$KEY_ALIAS" \
    --key-pass pass:"$KEY_PASSWORD" \
    --out "$2" \
    "$1"
  echo "Signing Completed!"
}

TMP="_tmp.apk"
zip_align_apk "$INPUT_APK_FILE" "$TMP"
sign_apk "$TMP" "$OUTPUT_APK_FILE"
rm $TMP
