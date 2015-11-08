// Run this with mvn args on each node
def mvn(args) {
    sh "${tool 'Maven 3.x'}/bin/mvn ${args}"
}

def fetch_repo() {
    sh 'repo init -u http://gerrit:8080/umbrella -m jenkins.xml' 
    sh 'repo sync'
    sh "repo download $GERRIT_PROJECT $GERRIT_CHANGE_NUMBER/$GERRIT_PATCHSET_NUMBER"
}

// Builds are defined before being run!
// We are building up a set of parallel pipelines, one using workflow for build +
//builds... they're defined first as a name : {block} pairs, then run by 'parallel' DSL step
def builds = [:]
// Core build/run inlined with workflow
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

  // Set of tests that run slowly and are run in parallel for speed
  def slowtests = [:]

  // Fast functional tests run in parallel with slower integration tests
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

  // Slower integration tests that are run
  slowtests['Integration tests'] = {
    node {
      sleep 15
      unstash name:'jars'
      sh 'java -jar primary*.jar `java -jar secondary*.jar`'
    }
  }
  slowtests['failFast'] = true
  parallel slowtests  // This is where it actually will run this step when block is executed
}

// PARALLEL NESTED PIPE
builds['parallelbuild'] = {
  build job: 'freestylebuild', parameters: [[$class: 'StringParameterValue', name: 'sample', value: 'val']]
}

builds['failFast'] = true
parallel builds // This is where the block is finally run