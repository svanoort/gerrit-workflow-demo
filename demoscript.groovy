// Run this with mvn args on each node
def mvn(args) {
    sh "${tool 'Maven 3.x'}/bin/mvn ${args}"
}

// TODO artifact passing
// TODO generate maven artifacts to do stuff with....
// TOOD patchset use
// TODO supply branch name to script for easy customization

def builds = [:]
builds['devtesting'] =  {
  stage 'building'
  node {
    echo 'running dev testing'
    // Do build
  }


  def integrationtests = [:]
  integrationtests['test1'] = {
    node {
     echo 'doing test1 for env 1'
    }
  }
  integrationtests['test2'] = {
    node {
     echo 'doing test2 for env 1'
    }
  }
  parallel integrationtests
}

builds['stage'] = {
  stage 'building'
  node {
    echo 'building for stage'
  }
}

parallel builds