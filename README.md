# gerrit-workflow-demo
Demo of Dockerized gerrit/jenkins workflow functionality

# Setup
* Prerequisite: Linux or Mac with a working Docker installation (Boot2Docker or Docker machine will work), and *optionally* Docker Compose
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

* Using Docker Compose:  simply run 'docker compose up'
* Vanilla Docker: 
	- To build: run build.sh (using sudo if needed) - it will need a couple minutes
	- To run: run start.sh

# To stop:

* Using Docker Compose: 'docker compose kill'
* Vanilla Docker:  'docker rm -f gerrit && docker rm -f jenkins'

This demonstrates a fairly complex real-world use case, where workflow builds may be run for 

This comprises 3 parts:
* Jenkins server using workflow and the gerrit trigger plugin to work with gerrit patchsets
* Gerrit server for code review
* Git server, with a pair of local git repos and changes submitted by repo (to allow multi-repo patchsets)


In fitting with Docker best practices, this is split into two Docker containers:

* jenkins-gerrit-wfdemo-jenkins - the jenkins host, complete with its own worfklow directory (runs on port 8081)
* jenkins-gerrit-wfdemo-gerrit - gerrit server on port 8080, with local git repos and repo running in server mode 

# Local Setup
1. Install repo: ```sudo curl https://storage.googleapis.com/git-repo-downloads/repo > /bin/repo && sudo chmod a+x /bin/repo```
2. Run ```repo init -u http://localhost:8080/umbrella && repo sync```
3. Pick a repo: ```cd primary```
4. Install commit-msg hook for gerrit to create chg id FOR EACH REPO: ```curl -Lo .git/hooks/commit-msg http://localhost:8080/tools/hooks/commit-msg && chmod u+x .git/hooks/commit-msg```
5. Create commit content: ```echo sample stuff > file.txt```
6. Commit (you must be a registered user with gerrit): ```git commit -c user.email=demouser@example.com -c user.name="Demo User" -am "Sample commit msg"```
7. Push commit (use HTTP auth from user settings, or SSH id): ```git push local HEAD:refs/for/master```

# Troubleshooting

1. Verifying Gerrit access by SSH for jenkins
* go to the gerrit-workflow-demo/jenkins
* run the following: `ssh -i demo_key_rsa -p 29418 demouser@localhost`
* you should see the following:
```
Enter passphrase for key 'demo_key_rsa': 

  ****    Welcome to Gerrit Code Review    ****

  Hi demouser, you have successfully connected over SSH.

  Unfortunately, interactive shells are disabled.
  To clone a hosted Git repository, use:

  git clone ssh://demouser@25680d00e8b3:29418/REPOSITORY_NAME.git

Connection to localhost closed.
```

2. Set up a local gerrit user:
```shell
sh config-gerrit.sh
```
By default, this will use your git name & email, set the gerrit username as local username, and your HTTP gerrit password as 'goober' (and add your local SSH public RSA key).  You can always set this up manually and change it from the gerrit interface

2. Verifying gerrit push ACLs for user.  Clone and edit a file: 
```shell
git clone http://localhost:8080/primary
cd primary

# Set up the gerrit changeId hook
curl -Lo .git/hooks/commit-msg http://localhost:8080/tools/hooks/commit-msg && chmod u+x .git/hooks/commit-msg

# Create a test commit
echo 'blahblahblah' > newfile.txt && git add newfile.txt
git commit -a -m "Test"

# Verify push by ssh
git push ssh://$USER@localhost:29418/primary HEAD:refs/for/master

# Verify ability to push by HTTP
echo 'moreblah' > morenewfile.txt && git add morenewfile.txt
git commit -a -m "Another test"
git push origin HEAD:refs/for/master
```

3. Repo push access

```shell
repo init -u http://localhost:8080/umbrella && repo sync

# Start a branch
repo start feature-testing --all
# Make and commit changes
repo upload
```