FROM gerritforge/gerrit-ubuntu15.04:2.11.3
MAINTAINER svanoort

USER root
RUN apt-get install -y curl python && \
    rm -rf /var/lib/apt/lists/*
RUN curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo \
    && chmod a+x ~/bin/repo

COPY repos /tmp/repos
RUN chown -R gerrit:gerrit /tmp/repos
RUN git config --global user.email "you@example.com" && \
    git config --global user.name "Demo User"

USER gerrit

COPY run.sh /usr/local/bin/
CMD /usr/local/bin/run.sh