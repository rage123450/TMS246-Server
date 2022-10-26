from time import sleep

currentMap = sm.getFieldID()
if sm.hasMobsInField():
    sm.chat("Kill all monsters first.")
else:
    #add delay to prevent bypassing
    sleep(2)
    if currentMap == 925076300:
        sm.warpInstanceOut(925020003, 1)
    elif currentMap / 10000 == 92507:
        sm.warp(currentMap+100, 0)
        #chr.setDojoPoints(current_dojoPoints+100)
