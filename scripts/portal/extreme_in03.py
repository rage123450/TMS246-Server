sm.setSpeakerID(9071006)

mapID = [951000210,951000220,951000230,951000240,951000250,951000260,951000270]
runsADay = 10
rand = sm.getRandomIntBelow(len(mapID))

from net.swordie.ms.enums import EventType

selection = sm.sendSayOkay("Monster Park Extreme is where you can face dangerous enemies to level up quickly.\r\n"
                           "#L0##bEnter Monster Park Extreme "+ str(sm.getEventAmountDone(EventType.MonsterParkExtreme)) + "/10 Attempted today #l\r\n")

if selection == 0:

    if chr.getLevel() < 200:
        sm.sendSayOkay("You must be level #b200#k to enter the Dimensional Invasion Party Quest.")
        sm.dispose()

    if sm.partyHasCoolDown(EventType.MonsterParkExtreme, runsADay):
        sm.sendSayOkay("You are currently on cooldown for Monster Park Extreme.")
        sm.dispose()

    if sm.getParty() is None:
        sm.sendSayOkay("Please create a party before entering.")
        sm.dispose()

    else:
        map = mapID[rand]
        sm.addCoolDownInXays(EventType.MonsterParkExtreme, 1, 1)
        sm.warpInstanceIn(map, True)
        sm.setInstanceTime(60*10)