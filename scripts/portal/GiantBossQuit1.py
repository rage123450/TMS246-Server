sm.setSpeakerID(9390124)
if sm.sendAskYesNo:
    if sm.sendAskYesNo("Do you wish to leave this place?"):
        sm.warpInstanceOut(863010000)
        sm.dispose()
    else:
        sm.dispose()
