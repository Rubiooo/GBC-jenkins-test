package common

class GlobalVars {
  static def gitBaseUrl= "ssh://git@gitrepo.georgebrown.ca:2222"

  static def scmPollScheduleMin = '* * * * *'
  static def scmPollScheduleFiveMins = 'H/5 * * * *'
  static def scmPollScheduleHalfHour = 'H/30 * * * *'
  static def scmPollScheduleEveryDay = 'H H(3-8) * * *'
  static def scmPollScheduleSundayMorning = 'H H(4-8) * * 7'
}
