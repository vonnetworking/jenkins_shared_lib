def call(body) {

  stage ("Build") {
    sh "gradle clean build"
  }
  stage ("Unit Test") {
    sh "echo 'Unit Tests would go here...'"
  }
  stage ("Docker Build") {
    def configMap = readYaml file:"pipeline_config.yaml"
    def app_version = readFile file:"VERSION"
    sh "docker image build -t springboot-test:$app_version ."
  }
  stage ("Docker Dev Push") {
    def app_version = readFile file:"VERSION"
    sh "LOCAL_IMAGE_ID=`docker images | grep springboot-test | awk '{print \$3}'` && docker tag \$LOCAL_IMAGE_ID vonnetworking/springboot-test:app-dev-$app_version && docker push vonnetworking/springboot-test"
  }
}
