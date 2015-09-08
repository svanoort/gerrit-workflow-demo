# Expose git servers and start up gerrit
git daemon --verbose --enable=receive-pack --base-path=/tmp/repos --export-all &
/var/gerrit/bin/gerrit.sh start && tail -f /var/gerrit/logs/error_log