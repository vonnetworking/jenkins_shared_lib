def call(body) {

  stage ("Build") {
    sh "/usr/local/bin/gradle clean build"
  }
  stage ("Unit Test") {
    sh "echo 'Unit Tests would go here...'"
  }
  stage ("Docker Build") {
    def configMap = readYaml file:"pipeline_config.yaml"
    def app_version = readFile("VERSION").trim()
    sh "echo 'App Version: $app_version'"
    sh "env"
    def build_command = "/usr/local/bin/docker image build -t springboot-test:$app_version \$WORKSPACE"
    echo "Build Command: $build_command"
    sh "$build_command"
  }
  stage ("Docker Dev Push") {
    def app_version = readFile file:"VERSION"
    sh "LOCAL_IMAGE_ID=`/usr/local/bin/docker images | grep springboot-test | awk '{print \$3}'` && /usr/local/bin/docker tag \$LOCAL_IMAGE_ID vonnetworking/springboot-test:app-dev-$app_version"

    withCredentials([usernamePassword(credentialsId: 'av_dockerhub_id', usernameVariable: 'HUB_USER', passwordVariable: 'HUB_PASS')]) {
      sh "export PATH=$PATH:/usr/local/bin && /usr/local/bin/docker login -u \$HUB_USER -p \$HUB_PASS && /usr/local/bin/docker push vonnetworking/springboot-test"
    }
  }
  post {
    always {
      echo 'One way or another, I have finished'
      sh "docker rmi --force `docker images | grep springboot-test | head -1 | awk '{ print \$3 }'`"
      deleteDir() /* clean up our workspace */
    }
  }
}
