# Partially borrowed from gerritt-installer plus jenkins workflow demo
# Source for ubuntu gerrit installer: https://gerrit.googlesource.com/gerrit-installer/+/refs/heads/master/docker/ubuntu15.04/Makefile
FROM ubuntu:15.04
MAINTAINER svanoort

# Add Gerrit packages repository
RUN echo "deb mirror://mirrorlist.gerritforge.com/deb gerrit contrib" > /etc/apt/sources.list.d/GerritForge.list
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 1871F775

# Allow remote connectivity and sudo
RUN apt-get update
RUN apt-key update
RUN apt-get -y install openssh-client sudo

# Install OpenJDK and Gerrit in two subsequent transactions
# (pre-trans Gerrit script needs to have access to the Java command)
RUN apt-get -y install openjdk-7-jdk
RUN apt-get -y install gerrit=2.11.3-1

USER gerrit
RUN java -jar /var/gerrit/bin/gerrit.war init --batch -d /var/gerrit
RUN java -jar /var/gerrit/bin/gerrit.war reindex -d /var/gerrit

# TODO Add/configure git repos
# TODO modify gerrit configuration
# TODO Install & configure jenkins host for workflow, etc

# Allow incoming traffic
EXPOSE 29418 8080

# Start Gerrit
CMD /var/gerrit/bin/gerrit.sh start && tail -f /var/gerrit/logs/error_log