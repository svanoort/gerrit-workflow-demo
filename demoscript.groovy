// Run this with mvn args on each node
def mvn(args) {
    sh "${tool 'Maven 3.x'}/bin/mvn ${args}"
}

def fetch_repo() {
    sh 'repo init -u http://gerrit:8080/umbrella -m jenkins.xml'
    sh 'repo sync'
    sh 'repo download $GERRIT_PROJECT $GERRIT_CHANGE_NUMBER/$GERRIT_PATCHSET_NUMBER'
}

// TODO artifact passing
// TODO generate maven artifacts to do stuff with....
// TOOD patchset use
// TODO supply branch name to script for easy customization

def builds = [:]
builds['devtesting'] =  {
  stage 'building'
  node {
    fetch_repo()
    mvn('-version')
  }

  def integrationtests = [:]
  integrationtests['test1'] = {
    node {
     echo 'doing test1 for env 1'
    }
  }
  integrationtests['test2'] = {
    node {
      // Test command 1 runs with command 2
     // java -jar primary/target/primary-1.0-SNAPSHOT.jar `java -jar secondary/target/secondary-1.0-SNAPSHOT.jar`
     echo 'doing test2 for env 1'
    }
  }
  parallel integrationtests
}

builds['stage'] = {
  stage 'building'
  node {
    fetch_repo()
    sh 'repo init -u http://gerrit:8080/umbrella && repo sync'
    mvn('-version')
  }
}

parallel builds