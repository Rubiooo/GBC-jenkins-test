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
        }

        stage('fix_repo') {
          sh ("yes|cp -rf _fix/* .")
        }


        stage('maven build') {
            withCredentials([file(credentialsId: 'mvnsettings', variable: 'MVNSETTINGS')]) {
                try {
                    sh "docker run -t --rm -v `pwd`:/app -v ${MVNSETTINGS}:/root/.m2/settings.xml maven:3 mvn -f /app/build/net.hedtech.banner.hr/pom.xml package"
                } catch (e) {}
            }
        }

        stage('docker build') {

            sh "docker build -t gbc/banner:${BUILD_ID} . --label 'git_commit="+scm.GIT_COMMIT+"'"
            sh "docker push gbc/banner:${BUILD_ID}"
        }        
    }
}
