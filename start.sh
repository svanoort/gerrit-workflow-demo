# Starts both containers in  daemon mode
docker run -d -p 8081:8080 jenkins-gerrit-wfdemo-jenkins:1.0
docker run -d -p 8080:8080 -p 29418:29418 jenkins-gerrit-wfdemo-gerrit:1.0