package common

class GlobalVars {
  static def gitBaseUrl= "ssh://git@gitrepo.georgebrown.ca:2222"

  static def scheduleMin = '* * * * *'
  static def scheduleFiveMins = 'H/5 * * * *'
  static def scheduleHalfHour = 'H/30 * * * *'
  static def scheduleEveryDay = 'H H(3-8) * * *'
  static def scheduleSundayMorning = 'H H(4-8) * * 7'
}
