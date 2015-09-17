// Run this with mvn args on each node
def mvn(args) {
    sh "${tool 'Maven 3.x'}/bin/mvn ${args}"
}

def fetch_repo() {
    sh 'repo init -u http://gerrit:8080/umbrella -m jenkins.xml'  // --repo-url=http://gerrit:8080/static/repo.bundle
    sh 'repo sync'
//    sh "repo download $GERRIT_PROJECT $GERRIT_CHANGE_NUMBER/$GERRIT_PATCHSET_NUMBER"
}

// TOOD patchset use
// TODO supply branch name to script for easy customization

def builds = [:]
builds['workflowrun'] =  {
  stage 'building'
  node {
    sh 'rm -rf source || true'
    // Remove dir component in 1.11
    dir ('source') {
      fetch_repo()
      mvn("clean compile install -f primary/pom.xml")
      mvn("clean compile install -Dmaven.test.skip -f secondary/pom.xml")
      sh "mv */target/*.jar ."
      stash includes: '*.jar', name: 'jars'
    }
  }

  def slowtests = [:]
  slowtests['Fuctional Tests'] = {
    node {
     unstash name:'jars'
     sleep 2

     // Verify the jars can run successfully
     sh 'java -jar primary*.jar -delay 1 --length 100'
     sh 'java -jar secondary*.jar'
     // Fetch both artifacts
     // Run both jar artifacts
     echo 'doing test1 for env 1'
    }
  }
  slowtests['Integration tests'] = {
    node {
      sleep 15
      unstash name:'jars'
      sh 'java -jar primary*.jar `java -jar secondary*.jar`'
    }
  }
  parallel slowtests
}

// PARALLEL BUILD STEP
builds['parallelbuild'] = {
  stage 'building'
  build job: 'freestylebuild', parameters: [[$class: 'StringParameterValue', name: 'sample', value: 'val']]
}

parallel builds