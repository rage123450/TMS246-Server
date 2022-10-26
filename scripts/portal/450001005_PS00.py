KIMA = 3003110

sm.setSpeakerID(KIMA)

if sm.hasQuestCompleted(34107) and (sm.hasQuestCompleted(34108) or sm.hasQuest(34108)):
    sm.warp(450001105, 1)
    sm.dispose()
elif sm.hasQuestCompleted(34107):
    response = sm.sendAskYesNo("...Steering this boat is one of the few things that I enjoy doing...\r\n\r\n#b(You will ride the boat"
                + " to Lake of Oblivion if you accept.)")
    if response:
        sm.sendNext("...Alright, here we go...")
        sm.warpInstanceIn(450001310)
        sm.rideVehicle(1932393)
        sm.progressMessageFont(3, 20, 20, 0, "Use the direction keys to steer the boat.")
        sm.addPopUpSay(KIMA, 6000, "Cross this lake, and you'll arrive at a massive cliff. I don't know what's behind it.", "")
elif sm.hasQuestCompleted(34106) and not sm.hasQuestCompleted(34107):
    sm.sendSayOkay("Don't just enter my boat! Talk to me first...")
    sm.dispose()
else:
    sm.sendSayOkay("Sorry I don't remember how to row this boat. If only the #bTree of Memories#k was back to it's original state...")
    sm.dispose()