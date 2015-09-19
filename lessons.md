# Lessons:

Some of these are docker-specific, some of these are tool specific

* The JDK image layer is fat (nearly 500 MB!), plan accordingly! (skinnier images exist)
* Docker is not a magic bullet for integration problems between systems, in fact it makes it harder because you need to worry about linking containers & exposing ports
    - Both linked containers and other hosts need to have visibility and recognize hostnames
* Know where and how your configs are stored; a lot of Java applications use XML and this is wonderful for building up Docker configs, because it's friendly to committing in git
    - While building your dockerfile, you can mount a local folder to the config directory
    - Use ADD/COPY in the dockerfile to add this content
    - Once you're done playing with configs, you can stop using volumes
    - If you only want to use single files (example: the H2 DB file in Gerrit) it is very easy to "docker cp" to snag a known, good state
    - Binaries are a problem. 
* Java plays very nicely with Docker, though it does come with a memory footprint

Iterative Development:

* Building up your Dockerized config in a git repo is wonderful, because it lets you jump back and forth between different builds
* Docker builds are repeatable, and Docker images 

# Specific Technical Issues
* Gerrit specific: code-review label verified breaks gerrit trigger:
*  http://stackoverflow.com/questions/20019195/cant-find-label-verified-permission-in-gerrit-2-7
*  Can either add verified label or set just to submit code review by changing gerrit trigger config

* Repo/Gerrit Permissions Issues on Upload:
    - http://stackoverflow.com/questions/11804469/debugging-repo-upload-problems-permission-denied-publickey
    - Repo does something dumb in finding the username for SSH, fixed by explicitly setting a git config value in config-gerrit.sh

# Repo use:
Reset: 
repo sync -d primary secondary