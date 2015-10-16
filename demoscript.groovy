// Run this with mvn args on each node
def mvn(args) {
    sh "${tool 'Maven 3.x'}/bin/mvn ${args}"
}

def fetch_repo() {
    sh 'repo init -u http://gerrit:8080/umbrella -m jenkins.xml' 
    sh 'repo sync'
    sh "repo download $GERRIT_PROJECT $GERRIT_CHANGE_NUMBER/$GERRIT_PATCHSET_NUMBER"
}

def builds = [:]
builds['workflowrun'] =  {
  node {
    sh 'rm -rf source'
    // Remove dir component in 1.11, replaced with deletedir
    dir ('source') {
      fetch_repo()
      mvn("clean compile install -f primary/pom.xml")
      mvn("clean compile install -Dmaven.test.skip -f secondary/pom.xml")
      sh "mv */target/*.jar ."
      stash includes: '*.jar', name: 'jars'
    }
  }

  def slowtests = [:]
  slowtests['Functional Tests'] = {
    node {
     // Fetch both artifacts
     unstash name:'jars'
     sleep 2

     // Verify both jars can run successfully
     sh 'java -jar primary*.jar -delay 1 --length 100'
     sh 'java -jar secondary*.jar'
    }
  }
  slowtests['Integration tests'] = {
    node {
      sleep 15
      unstash name:'jars'
      sh 'java -jar primary*.jar `java -jar secondary*.jar`'
    }
  }
  slowtests['failFast'] = true
  parallel slowtests
}

// PARALLEL BUILD STEP
builds['parallelbuild'] = {
  build job: 'freestylebuild', parameters: [[$class: 'StringParameterValue', name: 'sample', value: 'val']]
}

builds['failFast'] = true
parallel builds