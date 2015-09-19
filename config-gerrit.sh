#!/bin/bash
USERNAME="`git config user.name`"
EMAIL="`git config user.email`"
USERNAME="$USER"
HTTP_PASS=goober
FULLNAME="`git config user.name`"

cat ~/.ssh/id_rsa.pub | ssh -i jenkins/demo_key_rsa -p 29418 demouser@localhost \
  gerrit create-account --group Workers --group \"Non-Interactive Users\" --full-name \"$FULLNAME\" --email \"$EMAIL\" --http-password \"$HTTP_PASS\" --ssh-key - $USERNAME