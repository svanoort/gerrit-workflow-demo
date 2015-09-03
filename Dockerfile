# Partially borrowed from gerritt-installer plus jenkins workflow demo
# Source for ubuntu gerrit installer: https://gerrit.googlesource.com/gerrit-installer/+/refs/heads/master/docker/ubuntu15.04/Makefile
FROM ubuntu:15.04
MAINTAINER svanoort

# Add Gerrit packages repository
RUN echo "deb mirror://mirrorlist.gerritforge.com/deb gerrit contrib" > /etc/apt/sources.list.d/GerritForge.list
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 1871F775

# Core packages, note that ssh client is for remote access, curl/python are for repo
# Must install gerritt after java because it depends on it
RUN apt-get update && \
    apt-key update && \
    apt-get -y install openssh-client sudo openjdk-7-jdk curl python && \
	apt-get -y install gerrit=2.11.3-1 && \
    rm -rf /var/lib/apt/lists/*

# Repo installation
RUN curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo \
	&& chmod a+x ~/bin/repo

# Jenkins installation
ENV JENKINS_UC http://jenkins-updates.cloudbees.com
ENV JENKINS_VERSION 1.609.3
ENV JENKINS_SHA f5ad5f749c759da7e1a18b96be5db974f126b71e
ENV JENKINS_HOME /var/jenkins_home

# Home directory & inital refs
RUN mkdir -p /usr/share/jenkins/ref/init.groovy.d
RUN useradd -d "$JENKINS_HOME" -m -s /bin/bash jenkins && chown -R jenkins "$JENKINS_HOME" /usr/share/jenkins/ref
RUN curl -fL http://mirrors.jenkins-ci.org/war-stable/$JENKINS_VERSION/jenkins.war -o /usr/share/jenkins/jenkins.war \
  && echo "$JENKINS_SHA /usr/share/jenkins/jenkins.war" | sha1sum -c -

# Gerrit setup
USER gerrit
RUN java -jar /var/gerrit/bin/gerrit.war init --batch -d /var/gerrit && \
    java -jar /var/gerrit/bin/gerrit.war reindex -d /var/gerrit

# Mount point for jenkins dir
VOLUME /var/jenkins_home

# TODO Mount volume to Add/configure git repos
# TODO modify gerrit configuration
# TODO Install & configure jenkins host for workflow, etc

# VOLUME for git repos for gerrit

# Allow incoming traffic, 8080 is gerrit, 29418 is gerrit ssh, 8081 is Jenkins
EXPOSE 29418 8080 8081

# Start Gerrit and Jenkins TODO add jenkins startup
#CMD /var/gerrit/bin/gerrit.sh start && tail -f /var/gerrit/logs/error_log

ENV COPY_REFERENCE_FILE_LOG $JENKINS_HOME/copy_reference_file.log

USER jenkins
# Run scripts
COPY jenkins.sh /usr/local/bin/jenkins.sh