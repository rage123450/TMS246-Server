# 925020002: Exit, 925020003: RoofTop, 925020004: ?
import random

lastFloor = int(sm.getQRValue(3847, "Floor"))
if lastFloor == 63:
    randomNum1 = random.randint(10, 75)
    stagePoint = int(63 * 3.5)
    pointPer = int(randomNum1 + stagePoint)
    currentPoint = int(chr.getDojoPoints() + pointPer)
    chr.setDojoPoints(currentPoint)
chr.chatMessage("You have " + str(currentPoint) + " Dojo Points")
chr.chatMessage("You have cleared "+str(lastFloor)+" Floor in "+sm.getQRValue(3847, "Time")+" seconds.")
sm.setQRValue(3847, "Result", "complete")
