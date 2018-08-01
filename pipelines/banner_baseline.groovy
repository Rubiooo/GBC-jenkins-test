node {

    timestamps {
        stage('prepare env') {
     checkout([
        $class: 'GitSCM'
        branches: [[name: "master"]]
        userRemoteConfigs: [[url: 'ssh://git@gitrepo.georgebrown.ca:7999/gbc/banner_pages.git']]
     ])
        }

        stage('update pom') {
            sh "curl 'https://gitrepo.georgebrown.ca/projects/GBC/repos/banner_pages_fix/raw/base/net.hedtech.banner/pom.xml?at=refs%2Fheads%2Fmaster' -o base/net.hedtech.banner/pom.xml"
            sh "curl 'https://gitrepo.georgebrown.ca/projects/GBC/repos/banner_pages_fix/raw/build/net.hedtech.banner.hr/pom.xml?at=refs%2Fheads%2Fmaster' -o build/net.hedtech.banner.hr/pom.xml"
            sh "curl 'https://gitrepo.georgebrown.ca/projects/GBC/repos/banner_pages_fix/raw/reports/pom.xml?at=refs%2Fheads%2Fmaster' -o reports/pom.xml"
        }

        stage('maven build') {
            withCredentials([file(credentialsId: 'mvnsettings', variable: 'MVNSETTINGS')]) {
                try {
                    sh "docker run -t --rm -v `pwd`:/app -v ${MVNSETTINGS}:/root/.m2/settings.xml maven:3 mvn -f /app/build/net.hedtech.banner.hr/pom.xml package"
                } catch (e) {}
            }
        }
    }
}
