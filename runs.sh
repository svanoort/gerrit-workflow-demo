# Jenkins/workflow plugin/Gerrit/repo demo
docker build -t jenkins-gerrit-wf-demo:1.0 .
docker run -d -p 8080:8080 -p 29418:29418 jenkins-gerrit-wf-demo:1.0