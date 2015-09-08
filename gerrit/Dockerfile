FROM gerritforge/gerrit-ubuntu15.04:2.11.3
MAINTAINER svanoort

USER root
RUN apt-get install -y curl python net-tools && \
    rm -rf /var/lib/apt/lists/*
RUN curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo \
    && chmod a+x ~/bin/repo


COPY repos /tmp/repos-wc
RUN chown -R gerrit:gerrit /tmp/repos-wc
RUN git config --global user.email "demouser@example.com" && \
    git config --global user.name "Demo User"

# Git access to repo
EXPOSE 9418
USER gerrit

# Create repo working copies
RUN cd /tmp/repos-wc && \
    for r in primary secondary umbrella; do ( \
        cd $r; git init && git add . && git commit -am "Initial commit"; \
    ); done;


# Initialize the actual repos
RUN mkdir /tmp/repos && cd /tmp/repos && \
    for r in primary secondary umbrella; do git clone --bare /tmp/repos-wc/$r /tmp/repos/$r; done
COPY run.sh /usr/local/bin/
CMD /usr/local/bin/run.sh