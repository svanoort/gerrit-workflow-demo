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

# COPY KEYS FOR USER TO JENKINS DIR
# Copy DB

COPY ReviewDB.h2.db /var/gerrit/db/ReviewDB.h2.db

# Copy over config file

# Create repo working copies & then do bare clones to expose for work
RUN cd /tmp/repos-wc && mkdir /tmp/repos && \
    for r in primary secondary umbrella; do ( \
        cd $r; git init && git add . && git commit -am "Initial commit"; \
        git clone --bare /tmp/repos-wc/$r /var/gerrit/git/$r.git; \
    ); done;

# Initialize the actual repos
COPY run.sh /usr/local/bin/
CMD /usr/local/bin/run.sh
ENV HOME /var/gerrit  # Docker doesn't set this to user home by default

# TODO checkme that gerrit handles this right!