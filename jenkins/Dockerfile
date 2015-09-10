FROM jenkins:1.609.2
MAINTAINER svanoort

# Workflow demo, jenkins host setup

USER root
ENV JENKINS_UC http://jenkins-updates.cloudbees.com

# Keys for gerrit user, passphrase is "demouserpassword"
COPY demo_key_rsa /tmp/
COPY workflow-version.txt /tmp/
COPY plugins.txt /tmp/
RUN sed -i "s/@VERSION@/`cat /tmp/workflow-version.txt`/g" /tmp/plugins.txt
RUN chown jenkins:jenkins /tmp/demo_key_rsa

# Set up jenkins home folder for starting point
#ADD JENKINS_HOME /usr/share/jenkins/ref
#RUN chown -R jenkins.jenkins /tmp/files
# TODO do I need a workdir with /tmp/files?

USER jenkins
RUN /usr/local/bin/plugins.sh /tmp/plugins.txt
CMD /usr/local/bin/jenkins.sh

# COPY KEYS FOR USER TO JENKINS DIR
EXPOSE 8080