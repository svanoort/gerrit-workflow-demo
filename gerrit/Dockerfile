FROM gerritforge/gerrit-ubuntu15.04:2.11.3
MAINTAINER svanoort

USER root
RUN apt-get install -y curl python net-tools && \
    rm -rf /var/lib/apt/lists/*
RUN curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo \
    && chmod a+x ~/bin/repo

COPY repos /tmp/repos
RUN chown -R gerrit:gerrit /tmp/repos
RUN git config --global user.email "demouser@example.com" && \
    git config --global user.name "Demo User"

# Git access to repo
EXPOSE 9418
USER gerrit

# Initialize the actual repos
RUN cd /tmp/repos \
    && cd primary && git init && git add . && git commit -am "Initial commit" && cd .. \
    && cd secondary && git init && git add . && git commit -am "Initial commit" && cd .. \
    && cd umbrella && git init && git add . && git commit -am "Initial repo commit"

COPY run.sh /usr/local/bin/
CMD /usr/local/bin/run.sh