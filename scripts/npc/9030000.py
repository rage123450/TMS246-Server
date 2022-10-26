# Fredrick and Quick Move NPC

sm.setSpeakerID(9030000) # Fredrick

FREE_MARKET = 910000000

if sm.getFieldID() == FREE_MARKET:
    sm.sendSayOkay("I'm Fredrick, The Store Banker.")
elif sm.sendAskYesNo("Would you like to be teleported to the #bFree Market#k?"):
    sm.setReturnField()
    sm.warp(FREE_MARKET)