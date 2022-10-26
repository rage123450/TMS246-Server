response = sm.sendAskYesNo("Are you sure you want to leave?")

if response:
    sm.warpInstanceOut(401051104)
    sm.dispose()
