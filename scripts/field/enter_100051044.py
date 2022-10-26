# id 100051044 (Partem : Soul Chamber), field 100051044
sm.lockInGameUI(True, False)
sm.removeAdditionalEffect()
sm.blind(True, 255, 0, 0, 0, 0)
sm.zoomCamera(0, 3000, 0, -1145, 268)
sm.spawnNpc(1013350, -1096, 43)
sm.showNpcSpecialActionByTemplateId(1013350, "summon", 0)
sm.blind(True, 255, 0, 0, 0, 0)
sm.sendDelay(1200)
sm.blind(False, 0, 0, 0, 0, 1000)
sm.sendDelay(1400)
sm.zoomCamera(4000, 2000, 4000, -1010, 85)
sm.sendDelay(4500)
sm.createFieldTextEffect("#fnArial##fs18#Moments Later, in the Soul Chamber", 100, 1000, 6, -50, -50, 1, 4, 0, 0, 0)
sm.setSpeakerType(3)
sm.setParam(37)
sm.setColor(1)
sm.setInnerOverrideSpeakerTemplateID(1013350) # Brie
sm.sendNext("#face0#Phew... Th-th-that was so scary! We almost became permanent residents of these creepy ruins! If it wasn't for your quick thinking, we would've... *pant* *pant*")
sm.sendSay("#face0#Anyway, we finally made it. This is where the noise is coming from.")
sm.sendDelay(500)
sm.setParam(549)
sm.setInnerOverrideSpeakerTemplateID(1013358) # Pathfinder
sm.sendNext("#face3##b(You see something shining brightly upon a raised dais in the middle of the chamber. After going through all that trouble to get here, you might as well take a closer look.)#k")
sm.sendDelay(500)
sm.zoomCamera(3000, 1000, 3000, -400, -45)
sm.moveNpcByTemplateId(1013350, False, 400, 150)
sm.forcedMove(False, 450)
sm.sendDelay(5000)
sm.setParam(37)
sm.setInnerOverrideSpeakerTemplateID(1013360) # Soul
sm.sendNext("#face0#You are the one who has trod the hidden path. You have shown yourself qualified to be the bearer of the power.")
sm.sendDelay(500)
sm.setParam(549)
sm.setInnerOverrideSpeakerTemplateID(1013358) # Pathfinder
sm.sendNext("#face0##b(You hear a low-pitched voice echo in the chamber. You're not sure how to respond, but after everything you've gone through to get here, you decide to just speak candidly to the voice.)#k")
sm.sendDelay(500)
sm.showFadeTransition(0, 1000, 3000)
sm.zoomCamera(0, 1000, 2147483647, 2147483647, 2147483647)
sm.moveCamera(True, 0, 0, 0)
sm.sendDelay(300)
sm.removeOverlapScreen(1000)
sm.moveCamera(True, 0, 0, 0)
sm.lockInGameUI(False, True)
sm.createQuestWithQRValue(35947, "12=1;22=1;16=1;26=1;29=1;2=1;6=1")