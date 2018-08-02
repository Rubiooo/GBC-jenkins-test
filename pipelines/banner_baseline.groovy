node {
  def curl_login="curl -u usernamexxx:passwordxxx"
  timestamps {
        stage('prepare env') {
         scm=checkout([
            $class: 'GitSCM'
            branches: [[name: "master"]]
            userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages.git']]
         ])
           dir ("_fix") {
               checkout([
                 $class: 'GitSCM',
                 branches: [[name: "master"]],
                 userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_fix.git']]
                 ])
           }

           dir ("_gbc") {
                checkout([
                  $class: 'GitSCM',
                  branches: [[name: "master"]],
                  userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git']]
                  ])
          }
        }

        stage('fix_repo') {
          sh ("yes|cp -rf _fix/* .")
          sh ("yes|cp -rf _gbc/* .")
        }


        stage('maven build') {
            withCredentials([file(credentialsId: 'mvnsettings', variable: 'MVNSETTINGS')]) {
                try {
                    sh "docker run -t --rm -v `pwd`:/app -v ${MVNSETTINGS}:/root/.m2/settings.xml -v /tmp/m2.repository/:/root/.m2/repository/ maven:3 mvn -f /app/build/net.hedtech.banner.hr/pom.xml deploy"
                    sh "$curl_login -T ./workspace/webapp-workspace/target/wrksp.war -O \"https://artifactory.georgebrown.ca/artifactory/generic-local/build-${BUILD_NUMBER}/wrksp.war\""
                    sh "$curl_login -T ./build/webapp-services/target/wrksp.ws.war -O \"https://artifactory.georgebrown.ca/artifactory/generic-local/build-${BUILD_NUMBER}/wrksp.ws.war\""
                } catch (e) {}
            }
        }

        stage('docker build') {

            sh "docker build -t gbc/banner:${BUILD_ID} . --label 'git_commit="+scm.GIT_COMMIT+"'"
            sh "docker push gbc/banner:${BUILD_ID}"
            sh "docker rmi gbc/banner:${BUILD_ID}"
        }
    }
}
