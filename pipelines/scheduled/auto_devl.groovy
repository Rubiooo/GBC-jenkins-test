node {

  timestamps {
      slackSend (baseUrl: 'https://gbcitsea.slack.com/services/hooks/jenkins-ci/', channel: 'db-promotion', message: "====== auto devl Begin ======")

      stage("load script") {

          build job: '/scripts_promotion/load_script',
              parameters: [string(name: 'Environment', value: 'devl'),
                  string(name: 'Username', value: 'jenkins'),
                  [$class: 'com.michelin.cio.hudson.plugins.passwordparam.PasswordParameterValue', description: 'db password', name: 'Password', value: 'abc123'],
                  booleanParam(name: 'DryRun', value: false)]
      }

      stage("build and deploy devl") {
          build job: '/banner_page_build/build_gbc'

          build job: '/banner_page_build/deploy_dev'
      }

      slackSend (baseUrl: 'https://gbcitsea.slack.com/services/hooks/jenkins-ci/', channel: 'db-promotion', message: "====== auto devl Finish ======")
  } //timestamps
} //node
