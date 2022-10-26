response = sm.sendAskYesNo("Are you sure you want to leave?")

if response:
    sm.warpInstanceOut(401052104)
    sm.dispose()
