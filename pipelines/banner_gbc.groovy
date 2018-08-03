node {
  def curl_login="curl -u usernamexxx:passwordxxx"
  timestamps {
        stage('prepare env') {
         scm=checkout([
            $class: 'GitSCM'
            branches: [[name: "master"]]
            userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages.git']]
         ])
          // dir ("_fix") {
          //     checkout([
          //       $class: 'GitSCM',
          //       branches: [[name: "master"]],
          //       userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_fix.git']]
          //       ])
          // }

            dir ("_gbc") {
                checkout([
                  $class: 'GitSCM',
                  branches: [[name: "master"]],
                  userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git']]
                  ])
          }
        }

          stage('fix_repo') {
          // sh ("yes|cp -rf _fix/* .")
             sh ("yes|cp -rf _gbc/* .")
          }


        stage('maven build') {
            withCredentials([file(credentialsId: 'mvnsettings', variable: 'MVNSETTINGS')]) {
                try {
                    sh "docker run -t --rm -v `pwd`:/app -v ${MVNSETTINGS}:/root/.m2/settings.xml -v /tmp/m2.repository/:/root/.m2/repository/ maven:3 mvn -B -f /app/build/net.hedtech.banner.hr/pom.xml clean package"
                } catch (e)
                uploadBuild("./workspace/webapp-workspace/target", "wrksp.war")
                uploadBuild("./build/webapp-services/target", "wrksp.ws.war")
            }
        }

        def uploadBuild(String path, String filename) {
          def repo = "https://artifactory.georgebrown.ca/artifactory/generic-local"
          def now = new Date()
          def timestamp = now.format("yyyyMMdd-HHmm")
          def command = "${curl_login} -T ${path}/${filename} -O \"${repo}/build-gbc/${filename}-${timestamp}\""
          def result =  sh(returnStdout: true, script: command)
          command = "${curl_login} -T ${path}/${filename} -O \"${repo}/build-gbc-latest/${filename}\""
          result =  sh(returnStdout: true, script: command)
          return result
        }

        // stage('docker build') {

        //    sh "docker build -t gbc/banner:${BUILD_ID} . --label 'git_commit="+scm.GIT_COMMIT+"'"
        //    sh "docker push gbc/banner:${BUILD_ID}"
        //    sh "docker rmi gbc/banner:${BUILD_ID}"
        //}
    }
}
