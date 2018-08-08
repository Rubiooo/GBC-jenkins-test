    def createFolder(String timestamp){
      def curl_login="curl -u usernamexxx:passwordxxx"
      def repo = "https://artifactory.georgebrown.ca/artifactory/generic-local"
      def command = "${curl_login} -T version -O \"${repo}/build-gbc/${timestamp}/version\""
      def result =  sh(returnStdout: true, script: command)
      command = "${curl_login} -T version -O \"${repo}/build-gbc/latest/version\""
      result =  sh(returnStdout: true, script: command)
      return result
    }


    def uploadBuild(String path, String filename, String timestamp) {
      def curl_login="curl -u usernamexxx:passwordxxx"
      def repo = "https://artifactory.georgebrown.ca/artifactory/generic-local"

      def command = "${curl_login} -T ${path}/${filename} \"${repo}/build-gbc/${timestamp}/\""
      def result =  sh(returnStdout: true, script: command)
      command = "${curl_login} -T ${path}/${filename} \"${repo}/build-gbc/latest/\""
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
                      branches: [[name: "version"]],
                      userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git']]
                      ])
              }
            }

              stage('fix_repo') {

                 sh ("yes|cp -rf _gbc/* .")
              }

            stage('maven build') {
                withCredentials([file(credentialsId: 'mvnsettings', variable: 'MVNSETTINGS')]) {

                    sh "docker run -t --rm -v `pwd`:/app -v ${MVNSETTINGS}:/root/.m2/settings.xml -v /tmp/m2.repository/:/root/.m2/repository/ maven:3 mvn -B -f /app/build/net.hedtech.banner.hr/pom.xml clean package"

                    sh "echo $timestamp > version"
                    createFolder(timestamp)

                    uploadBuild("./workspace/webapp-workspace/target", "wrksp.war", timestamp)
                    uploadBuild("./build/webapp-services/target", "wrksp.ws.war", timestamp)

                    uploadBuild("./gbc-student/target", "net.hedtech.banner.gbc-student-*.jar", timestamp)
                    uploadBuild("./gbc-general/target", "net.hedtech.banner.gbc-general-*.jar", timestamp)
                    uploadBuild("./gbc-ellucian-arsys/target", "net.hedtech.banner.gbc-ellucian-arsys-*.jar", timestamp)
                    uploadBuild("./gbc-ellucian-finance/target", "net.hedtech.banner.gbc-ellucian-finance-*.jar", timestamp)
                    uploadBuild("./gbc-ellucian-general/target", "net.hedtech.banner.gbc-ellucian-general-*.jar", timestamp)
                    uploadBuild("./gbc-ellucian-positioncontrol/target", "net.hedtech.banner.gbc-ellucian-positioncontrol-*.jar", timestamp)
                    uploadBuild("./gbc-ellucian-student/target", "net.hedtech.banner.gbc-ellucian-student-*.jar", timestamp)
                    uploadBuild("./gbc-ellucian-studentaid/target", "net.hedtech.banner.gbc-ellucian-studentaid-*.jar", timestamp)



                }
            }
        }
    }
