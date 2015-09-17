#!/bin/bash

# Assuming you're running docker natively, copy the review db to local copy
sudo docker cp gerrit:/var/gerrit/db/ReviewDB.h2.db gerrit/ReviewDB.h2.db
sudo chown "$USER":"$USER" gerrit/ReviewDB.h2.db