// Run this with mvn args on each node
def mvn(args) {
    sh "${tool 'Maven 3.x'}/bin/mvn ${args}"
}

def fetch_repo() {
    sh 'repo init -u http://gerrit:8080/umbrella -m jenkins.xml'
    sh 'repo sync'
    sh "repo download $GERRIT_PROJECT $GERRIT_CHANGE_NUMBER/$GERRIT_PATCHSET_NUMBER"
}

// TODO artifact passing
// TODO generate maven artifacts to do stuff with....
// TOOD patchset use
// TODO supply branch name to script for easy customization

def builds = [:]
builds['workflowrun'] =  {
  stage 'building'
  node {
    fetch_repo()
    mvn("clean compile install -Dmaven.test.skip -f primary/pom.xml")
    mvn("clean compile install -Dmaven.test.skip -f secondary/pom.xml")
    step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
  }

  def slowtests = [:]
  slowtests['Fuctional Tests'] = {
    node {
     sleep 2
     // Fetch both artifacts
     // Run both jar artifacts 
     echo 'doing test1 for env 1'
    }
  }
  slowtests['Integration tests'] = {
    node {
      sleep 15
      // Fetch both artifacts
      // Test command 1 runs with command 2
     // java -jar primary/target/primary-1.0-SNAPSHOT.jar `java -jar secondary/target/secondary-1.0-SNAPSHOT.jar`
    }
  }
  parallel slowtests
}

builds['freestylebuild'] = {
  stage 'building'
  node {
    fetch_repo()
    sh 'repo init -u http://gerrit:8080/umbrella && repo sync'
    sleep 30
    mvn("clean compile install -Dmaven.test.skip -f primary/pom.xml")
    mvn("clean compile install -Dmaven.test.skip -f secondary/pom.xml")
  }
}

parallel builds