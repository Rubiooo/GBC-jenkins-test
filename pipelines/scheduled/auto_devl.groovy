node {

  timestamps {
      slackSend (channel: 'db-promotion', message: "====== auto devl Begin ======")
      stage("load load_script") {

          build job: '/scripts_promotion/load_script',
              parameters: [string(name: 'Environment', value: 'devl'),
                  string(name: 'Username', value: 'jenkins'),
                  [$class: 'com.michelin.cio.hudson.plugins.passwordparam.PasswordParameterValue', description: 'db password', name: 'Password', value: 'abc123'],
                  booleanParam(name: 'DryRun', value: true)]
      }

      stage("build and deploy devl") {
          build job: 'banner_page_build/build_gbc'

          build job: '/banner_page_build/deploy_dev'
      }
      slackSend (channel: 'db-promotion', message: "====== auto devl Finish ======")
  } //timestamps
} //node
