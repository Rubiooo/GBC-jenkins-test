

    def uploadBuild(String path, String filename, String timestamp) {

      withCredentials([usernamePassword(credentialsId: 'artifactory', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {

      def curl_login="curl -u $USERNAME:$PASSWORD"
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
                      branches: [[name: "master"]],
                      userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages_gbc.git']]
                      ])
              }
            }

              stage('fix_repo') {

                 sh ("yes|cp -rf _gbc/* .")
              }

            stage('maven build') {
                def now = new Date()
                def timestamp=now.format("yyyyMMdd-HHmm")
                wrap([$class: 'BuildUser']) {
                    def slackMessage = ":black_square_button: "+ env.BUILD_USER +" start building Admin Page on version " + timestamp)"
                    slackSend (channel: 'jenkins', message: slackMessage)
                }

                try {
                  withCredentials([file(credentialsId: 'mvnsettings', variable: 'MVNSETTINGS')]) {
                    sh "docker run -t --rm -v `pwd`:/app -v ${MVNSETTINGS}:/root/.m2/settings.xml -v /tmp/m2.repository/:/root/.m2/repository/ maven:3 mvn -B -f /app/build/net.hedtech.banner.hr/pom.xml clean package"
                  }
                  sh "mkdir -p dist"
                  sh "tar czfv dist/devl.wrksp.ws.tar.gz -C devl/wrksp.ws ."
                } catch (Exception e) {
                    slackSend (channel: 'jenkins', message: ":bangbang: @channel Build failed, please check ${BUILD_URL}")
                    currentBuild.result = 'FAILURE'
                    error('Build failure')
                }

                uploadBuild("./dist", "devl.wrksp.ws.tar.gz",timestamp)
                uploadBuild("./workspace/webapp-workspace/target", "wrksp.war", timestamp)
                uploadBuild("./build/webapp-services/target", "wrksp.ws.war", timestamp)

                uploadBuild("./gbc-student/target", "net.hedtech.banner.gbc-student-*.jar", timestamp)
                uploadBuild("./gbc-general/target", "net.hedtech.banner.gbc-general-*.jar", timestamp)
                uploadBuild("./gbc-payroll/target", "net.hedtech.banner.gbc-payroll-*.jar", timestamp)
                uploadBuild("./gbc-ellucian-arsys/target", "net.hedtech.banner.gbc-ellucian-arsys-*.jar", timestamp)
                uploadBuild("./gbc-finance/target", "net.hedtech.banner.gbc-finance-*.jar", timestamp)
                uploadBuild("./gbc-ellucian-finance/target", "net.hedtech.banner.gbc-ellucian-finance-*.jar", timestamp)
                uploadBuild("./gbc-arsys/target", "net.hedtech.banner.gbc-arsys-*.jar", timestamp)
                uploadBuild("./gbc-ellucian-general/target", "net.hedtech.banner.gbc-ellucian-general-*.jar", timestamp)
                uploadBuild("./gbc-ellucian-positioncontrol/target", "net.hedtech.banner.gbc-ellucian-positioncontrol-*.jar", timestamp)
                uploadBuild("./gbc-ellucian-student/target", "net.hedtech.banner.gbc-ellucian-student-*.jar", timestamp)
                uploadBuild("./gbc-ellucian-studentaid/target", "net.hedtech.banner.gbc-ellucian-studentaid-*.jar", timestamp)


                slackSend (channel: 'jenkins', message: ":ballot_box_with_check: Finish building Admin Page and upload to https://artifactory.georgebrown.ca/artifactory/generic-local/ " + timestamp)
            }
        }
    }
