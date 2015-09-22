# Specific Technical Issues

## Problem: repo behaves eratically if the old .repo folder is not removed
* Solution: rm and recreate the folder every time (using sh rm and the workflow dir step, or shell)

## Problem: repo needs a git user configured to init a repo
* Solution: configure a local git user for jenkins user, in the Dockerfile

## Problem: SSH keyfile RSA encryption (default now) was not supported by a library gerrit trigger used, so use of a passphrase would make it impossible to use a key to speak to gerrit
* Solution: Fixed with patch, released in gerrit trigger 2.15.1

## Problem: gerrit needs to have a user configured for jenkins & baked into the image in order for the jenkins gerrit trigger to work
* Solution: create the user in a Gerrit container and copy in the DB into your container
  - This is conveniently set up in: util/copy_docker_db.sh - see gerrit/Dockerfile for the COPY

## Problem: Jenkins Gerrit Trigger Needs a User With Stream Event Permissions, which are not on by default
This is somewhat Docker-specific:
* For an unDockerized Gerrit config, you'd simply create a new group "Streaming Events Users", add the jenkins user to it, and add give that group permission for streaming events in Projects > All Projects > Access within Gerrit UI
* For Dockerized configs, the "All Projects" access configuration is in a git repo in gerrit, and landing the configuration for this repo within your gerrit container is a problem

* Solution: Add the jenkins user to group "Non-Interactive Users" which has the Stream Events permission by default

## Problem: Modern Versions of Gerrit do not include the 'verified' review label, which the Gerrit Trigger uses for voting by default

* Solution 1: add the label to projects http://stackoverflow.com/questions/20019195/cant-find-label-verified-permission-in-gerrit-2-7
* Solution 2: remove the verified label from gerrit trigger response in jenkins
  - Manage Jenkins > Gerrit Trigger > edit next to server name > Advanced button at bottom 
  - Under "Gerrit Verified Commands" remove the "--verified <VERIFIED>" section from each and it won't send this (just normal code review vote)

## Problem: By default repo sets the SSH user name used to upload to gerrit using committer email and not username
* Solution: Explicitly Set the Reviewer username for the review URL
  - 'git config --global review.http://reviewhost:8080/.username $USER'
  - http://stackoverflow.com/questions/11804469/debugging-repo-upload-problems-permission-denied-publickey
  - See: config-gerrit.sh

## Problem: Gerrit Trigger will send a SEVERE warning on Jenkins Startup that it cannot identify Gerrit version
* Solution: safe to ignore, it still works, but see JENKINS-18391

## Problem: On Init, repo will try to fetch content from googlesource if this fails it will not work
* Solution: ensure you have external connectivity to https://gerrit.googlesource.com/git-repo/clone.bundle 
* Possible other solution: use the --repo-url argument to point it at a local repo, and clone from there
  - See: http://stackoverflow.com/questions/18895382/how-to-repo-init-on-a-disconnected-system

## Problem: How do I preload git repos into my gerrit container?   It has its own git stores under $GERRIT_BASE/git/
* Solution: you can simply do `git clone --bare` for your repos while gerrit isn't running, when gerrit starts it'll pick them up

## Problem: Jenkins needsa consistent hostname for the gerrit container to speak to it
* Solution: Explicitly set names for containers