def call {

  stage ("Hello Hello Hello") {
    sh "echo 'Hello Hello Hello'"
  }
  stage ("Is There Anybody In There?") {
    sh "echo 'Is There Anybody In There?'"
  }
  stage ("Welcome to the machine!") {
    sh "echo 'PEW PEW PEW!'"
  }
}
