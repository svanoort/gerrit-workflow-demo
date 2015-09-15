# Debugging use, runs removable containers in interactive mode if needed

sudo docker run  -i -t --rm=true --name gerrit -p 8080:8080 -p 29418:29418 jenkins-gerrit-wfdemo-gerrit:1.0 /bin/bash
sudo docker run  -i -t --rm=true -v ./jenkins/jenkins_home:/var/jenkins_home -p 8081:8080 --link gerrit:gerrit jenkins-gerrit-wfdemo-jenkins:1.0 /bin/bash
