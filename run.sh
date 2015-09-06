# Git repo access, which repo will use
# git daemon --verbose --enable=receive-pack --base-path=/tmp/files --export-all &

sudo -u jenkins /usr/local/bin/jenkins.sh &
sudo -u gerrit /var/gerrit/bin/gerrit.sh start && tail -f /var/gerrit/logs/error_log

# WORKS IF RUN directly not if run from Docker though