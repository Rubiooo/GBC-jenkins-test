def uploadBuild(String path, String filename, String timestamp) {
  def curl_login="curl -u usernamexxx:passwordxxx"
  def repo = "https://artifactory.georgebrown.ca/artifactory/generic-local"

  def command = "${curl_login} -T ${path}/${filename} -O \"${repo}/build-gbc/${filename}.${timestamp}\""
  def result =  sh(returnStdout: true, script: command)
  command = "${curl_login} -T ${path}/${filename} -O \"${repo}/build-gbc-latest/${filename}\""
  result =  sh(returnStdout: true, script: command)
  return result
}

node {

  timestamps {
        stage('prepare env') {
         scm=checkout([
            $class: 'GitSCM'
            branches: [[name: "master"]]
            userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages.git']]
         ])

            dir ("_gbc") {
                checkout([
                  $class: 'GitSCM',
                  branches: [[name: "master"]],
                  userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git']]
                  ])
          }
        }

          stage('fix_repo') {

             sh ("yes|cp -rf _gbc/* .")
          }

        stage('maven build') {
            withCredentials([file(credentialsId: 'mvnsettings', variable: 'MVNSETTINGS')]) {
                try {
                    sh "docker run -t --rm -v `pwd`:/app -v ${MVNSETTINGS}:/root/.m2/settings.xml -v /tmp/m2.repository/:/root/.m2/repository/ maven:3 mvn -B -f /app/build/net.hedtech.banner.hr/pom.xml clean package"
                } catch (e)
                uploadBuild("./workspace/webapp-workspace/target", "wrksp.war", timestamp)
                uploadBuild("./build/webapp-services/target", "wrksp.ws.war", timestamp)
            }
        }

    }
}
