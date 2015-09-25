# gerrit-workflow-demo
Demo of Dockerized gerrit/jenkins workflow functionality

# What?
* Dockerized demonstration of an integrated Jenkins/Gerrit environment, using Jenkins workflow to construct a build/test pipeline that is triggered when patchsets are submitted for review
* Mimics the work environment of a real world user doing mobile development

# Why?
* To demonstrate how Jenkins workflow facilitates complex build/testing schemes
* Show how one can construct a Dockerized code review/automation environment with full integration
* Demonstrate an integrated, containerized setup of Jenkins + Gerrit

This comprises 3 parts:
* Jenkins server using workflow and the gerrit trigger plugin to work with gerrit patchsets
* Gerrit server: acts as a central git repository and provides code review
* Installation of git-repo tool (in both Jenkins and Gerrit), to support projects spanning multiple repositories
* Code repos in gerrit for repo (umbrella manifest) and two sample Java projects

In fitting with Docker best practices, this is split into two Docker containers:

* jenkins-gerrit-wfdemo-jenkins - the jenkins host, complete with its own home directory (runs on port 8081)
* jenkins-gerrit-wfdemo-gerrit - gerrit server on port 8080, with local git repos and repo running in server mode 


# Setup
* Prerequisite: Linux or Mac with a working Docker installation (Boot2Docker or Docker machine will work), and *optionally* Docker Compose
* Prerequisite for use of repo tool: installation
  - Linux: `sudo curl https://storage.googleapis.com/git-repo-downloads/repo > /bin/repo && sudo chmod a+x /bin/repo`
  - Mac: `curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo && chmod a+x ~/bin/repo`
* Create Host Mappings (optional for linux, **required on Mac for Docker Machine / Boot2Docker** ):
  - Find your Docker host's IP and write it down
    - In linux on native docker, use 127.0.0.1
    - For boot2docker, run `boot2docker ip`
    - For Docker Machine, run `docker-machine ls` and finding the matching host
  - In your favorite text editor (in sudo mode) open /etc/hosts for editing, ex `sudo vi /etc/hosts`
  - Add two new lines mapping this IP to hostname 'jenkins' and 'gerrit' and save the file, ex:

```
192.168.99.100     gerrit
192.168.99.100     jenkins
```

# To run:

* Using Docker Compose:  simply run 'docker-compose up'
* Vanilla Docker: 
	- To build: run build.sh (using sudo if needed) - it will need a couple minutes
	- To run: run start.sh

# Using it:
* Jenkins is available at localhost:8081 (Linux) or jenkins:8081 (Mac or with hosts entry)
* Gerrit is available at localhost:8080 (Linux) or gerrit:8080 (Mac or with hosts entry) 
* To set up the local user with push permissions for gerrit:
  -  **Linux:** `sh config-gerrit.sh`
  -  **Mac or with hosts entry:** `sh config-gerrit-mac.sh`

By default, this will use your git name & email, set the gerrit username as local username, and your HTTP gerrit password as 'goober' (and add your local SSH public RSA key).  You can always set this up manually and change it from the gerrit interface.

# Sample project
* By default, there are three repositories, each with a gerrit project
  - 'umbrella' - this contains a manifest for repo
  - 'primary' - Java command line tool to generate random text, after a delay.  Mimics an API provider, built with maven.  
  - 'secondary' - Very basic java program to generate arguments to the program in 'primary.'  Mimics an API consumer, also built in maven

# Creating commits and pushing to gerrit directly
1. You must have a gerrit user configured (see "using it")
2. Git clone repositories (replace 'gerrit' with localhost if using native Docker)
  - http://gerrit:8080/primary  
  - http://gerrit:8080/secondary
3. Go into each repository and do the following to install Gerrit hooks: 
  - *Linux*: `curl -Lo .git/hooks/commit-msg http://localhost:8080/tools/hooks/commit-msg && chmod u+x .git/hooks/commit-msg`
  - *Mac*: `curl -Lo .git/hooks/commit-msg http://gerrit:8080/tools/hooks/commit-msg && chmod u+x .git/hooks/commit-msg`
4. Create a test commit
  - `echo 'blahblahblah' > newfile.txt && git add newfile.txt`
  - `git commit -a -m "Test"`
5. Push by SSH
  - *Linux:* `git push ssh://$USER@localhost:29418/primary HEAD:refs/for/master`
  - *Mac:* `git push ssh://$USER@gerrit:29418/primary HEAD:refs/for/master`
6. You should see new changes for review in Gerrit... and responses back from Jenkins after running 'wf-build'


# Creating and uploading changes by repo
1. You must have a gerrit user configured (see "using it") and repo installed
2. Pull down the project structure via repo (must be installed, see prerequisites)
  - *Linux, native docker:* `repo init -u http://localhost:8080/umbrella && repo sync`
  - *Mac or linux with hosts entry*: `repo init -u http://gerrit:8080/umbrella -m jenkins.xml && repo sync`
3. Start a new working branch in repo
  - `repo start feature-testing --all`
4. Push back changes for review: 
  - `repo upload`
5. You should see changes ready for review, and Jenkins test results from 'wf-build'
6. Feel free to go back to the master branch:
  - `repo sync -d`

# To stop:
* Using Docker Compose: 'docker-compose kill && docker-compose rm'
* Vanilla Docker:  'docker rm -f gerrit && docker rm -f jenkins'

# Troubleshooting

1. Verifying Gerrit access by SSH for jenkins
* go to the gerrit-workflow-demo/jenkins
* run the following:
  - Linux: `ssh -i demo_key_rsa -p 29418 demouser@localhost`
  - Mac: `ssh -i demo_key_rsa -p 29418 demouser@gerrit`
* you should see the following:
```
Enter passphrase for key 'demo_key_rsa': 

  ****    Welcome to Gerrit Code Review    ****

  Hi demouser, you have successfully connected over SSH.

  Unfortunately, interactive shells are disabled.
  To clone a hosted Git repository, use:

  git clone ssh://demouser@gerrit:29418/REPOSITORY_NAME.git

Connection to localhost closed.
```