# Starts both containers in  daemon mode
docker rm -f gerrit && docker rm -f jenkins
docker run -d -h gerrit -p 8080:8080 -p 29418:29418 --name gerrit jenkins-gerrit-wfdemo-gerrit:1.0
docker run -d -h jenkins -p 8081:8080 --name jenkins --link gerrit:gerrit jenkins-gerrit-wfdemo-jenkins:1.0
