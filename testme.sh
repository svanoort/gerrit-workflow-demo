# Jenkins/workflow plugin/Gerrit/repo demo
docker build -t jenkins-gerrit-wf-demo:1.0 .

# Debugging use
# sudo docker run  -i -t --rm=true -p 8080:8080 -p 29418:29418 jenkins-gerrit-wf-demo:1.0 /bin/bash
docker run -d -p 8080:8080 -p 8081:8081 -p 29418:29418 jenkins-gerrit-wf-demo:1.0

#sudo docker run  -i -t --rm=true -p 8080:8080 -p 29418:29418 -p 8081:8081 jenkins-gerrit-wf-demo:1.0