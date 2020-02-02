def call(body) {

  stage ("Build") {
    sh "/usr/local/bin/gradle clean build"
  }
  stage ("Unit Test") {
    sh "echo 'Unit Tests would go here...'"
  }
  stage ("Docker Build") {
    def configMap = readYaml file:"pipeline_config.yaml"
    def app_version = readFile file:"VERSION"
    sh "echo 'App Version: $app_version'"
    sh "/usr/local/bin/docker image build -t springboot-test:$app_version ."
  }
  stage ("Docker Dev Push") {
    def app_version = readFile file:"VERSION"
    sh "LOCAL_IMAGE_ID=`/usr/local/bin/docker images | grep springboot-test | awk '{print \$3}'` && /usr/local/bin/docker tag \$LOCAL_IMAGE_ID vonnetworking/springboot-test:app-dev-$app_version && /usr/local/bin/docker push vonnetworking/springboot-test"
  }
}
