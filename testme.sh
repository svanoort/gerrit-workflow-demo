# Debugging use, runs removable containers in interactive mode if needed

sudo docker run  -h gerrit -i -t --rm=true --name gerrit -p 8080:8080 -p 29418:29418 jenkins-gerrit-wfdemo-gerrit:1.0 /bin/bash

# Note when you run jenkins this way, something in the volumes means you have to run /usr/local/bin/jenkins.sh 
# Twice (killing java in between)
sudo docker run  -h jenkins -i -t --rm=true -v `pwd`/jenkins/jenkins_home:/var/jenkins_home -p 8081:8080 --name jenkins --link gerrit:gerrit jenkins-gerrit-wfdemo-jenkins:1.0 /bin/bash
