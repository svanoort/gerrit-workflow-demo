# Starts both containers in  daemon mode
docker rm -f gerrit && docker rm -f jenkins
docker run -d -p 8080:8080 -p 29418:29418 -p 9418:9418 --name gerrit jenkins-gerrit-wfdemo-gerrit:1.0
docker run -d -p 8081:8080 --name jenkins --link gerrit:gerrit jenkins-gerrit-wfdemo-jenkins:1.0
