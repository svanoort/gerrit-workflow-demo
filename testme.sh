# Debugging use, runs removable containers in interactive mode if needed

sudo docker run  -i -t --rm=true -p 8081:8080 jenkins-gerrit-wfdemo-jenkins:1.0
sudo docker run  -i -t --rm=true -p 8080:8080 -p 29418:29418 jenkins-gerrit-wfdemo-gerrit:1.0
