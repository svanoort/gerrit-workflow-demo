# gerrit-workflow-demo
Demo of dockerized gerrit/jenkins workflow functionality

To run me, pull or build both images:

To build: run build.sh (using sudo if needed) - it will need a couple minutes to start up!
To run: either pull or build images, then run start.sh

This demonstrates a fairly complex real-world use case, where workflow builds may be run for 

This comprises 3 parts:
* Jenkins server using workflow and the gerrit trigger plugin to work with gerrit patchsets
* Gerrit server for code review
* Git server, with a pair of local git repos and changes submitted by repo (to allow multi-repo patchsets)


In fitting with Docker best practices, this is split into two Docker containers:

* jenkins-gerrit-wfdemo-jenkins - the jenkins host, complete with its own worfklow directory (runs on port 8081)
* jenkins-gerrit-wfdemo-gerrit - gerrit server on port 8080, with local git repos and repo running in server mode 

# Troubleshooting

1. Verifying Gerrit access by SSH for jenkins
* go to the gerrit-workflow-demo/jenkins
* run the following: `ssh -i demo_key_rsa -p 29418 demouser@localhost`
* passphrase is: "demouserpassword" (without quotes around it)
* you should see the following:
```
Enter passphrase for key 'demo_key_rsa': 

  ****    Welcome to Gerrit Code Review    ****

  Hi demouser, you have successfully connected over SSH.

  Unfortunately, interactive shells are disabled.
  To clone a hosted Git repository, use:

  git clone ssh://demouser@25680d00e8b3:29418/REPOSITORY_NAME.git

Connection to localhost closed.
```