# Mu Lung Dojo Floors (except 1st Floor) | Used to spawn the Bosses & Display "Floor Effect & Dojo Clock"

from net.swordie.ms.constants import WzConstants
import random

MuLungDojoF1 = 925070100
MuLungDojoF2 = 925070200
MuLungDojoF3 = 925070300
MuLungDojoF4 = 925070400
MuLungDojoF5 = 925070500
MuLungDojoF6 = 925070600
MuLungDojoF7 = 925070700
MuLungDojoF8 = 925070800
MuLungDojoF9 = 925070900
MuLungDojoF10 = 925071000
MuLungDojoF11 = 925071100
MuLungDojoF12 = 925071200
MuLungDojoF13 = 925071300
MuLungDojoF14 = 925071400
MuLungDojoF15 = 925071500
MuLungDojoF16 = 925071600
MuLungDojoF17 = 925071700
MuLungDojoF18 = 925071800
MuLungDojoF19 = 925071900
MuLungDojoF20 = 925072000
MuLungDojoF21 = 925072100
MuLungDojoF22 = 925072200
MuLungDojoF23 = 925072300
MuLungDojoF24 = 925072400
MuLungDojoF25 = 925072500
MuLungDojoF26 = 925072600
MuLungDojoF27 = 925072700
MuLungDojoF28 = 925072800
MuLungDojoF29 = 925072900
MuLungDojoF30 = 925073000
MuLungDojoF31 = 925073100
MuLungDojoF32 = 925073200
MuLungDojoF33 = 925073300
MuLungDojoF34 = 925073400
MuLungDojoF35 = 925073500
MuLungDojoF36 = 925073600
MuLungDojoF37 = 925073700
MuLungDojoF38 = 925073800
MuLungDojoF39 = 925073900
MuLungDojoF40 = 925074000
MuLungDojoF41 = 925074100
MuLungDojoF42 = 925074200
MuLungDojoF43 = 925074300
MuLungDojoF44 = 925074400
MuLungDojoF45 = 925074500
MuLungDojoF46 = 925074600
MuLungDojoF47 = 925074700
MuLungDojoF48 = 925074800
MuLungDojoF49 = 925074900
MuLungDojoF50 = 925075000
MuLungDojoF51 = 925075100
MuLungDojoF52 = 925075200
MuLungDojoF53 = 925075300
MuLungDojoF54 = 925075400
MuLungDojoF55 = 925075500
MuLungDojoF56 = 925075600
MuLungDojoF57 = 925075700
MuLungDojoF58 = 925075800
MuLungDojoF59 = 925075900
MuLungDojoF60 = 925076000
MuLungDojoF61 = 925076100
MuLungDojoF62 = 925076200
MuLungDojoF63 = 925076300

# TODO: add HP
bossPerFloor = {
    MuLungDojoF1: 9305600,  # Mano
    MuLungDojoF2: 9305601,  # Mushmom
    MuLungDojoF3: 9305602,  # Stumpy
    MuLungDojoF4: 9305603,  # Blue Mushmom
    MuLungDojoF5: 9305604,  # Zombie Mushmom
    MuLungDojoF6: 9305605,  # King Slime
    MuLungDojoF7: 9305606,  # Dyle
    MuLungDojoF8: 9305607,  # King Clang
    MuLungDojoF9: 9305608,  # Faust
    MuLungDojoF10: 9305609,  # Hilla
    MuLungDojoF11: 9305610,  # Metal Golem
    MuLungDojoF12: 9305611,  # Eliza
    MuLungDojoF13: 9305612,  # Jr. Balrog
    MuLungDojoF14: 9305613,  # Nine-Tailed Fox
    MuLungDojoF15: 9305614,  # Deo
    MuLungDojoF16: 9305615,  # Zeno
    MuLungDojoF17: 9305616,  # Timer
    MuLungDojoF18: 9305617,  # Snack Bar
    MuLungDojoF19: 9305618,  # Tae Roon
    MuLungDojoF20: 9305619,  # Von Leon
    MuLungDojoF21: 9305620,  # Papa Pixie
    MuLungDojoF22: 9305621,  # Alishar
    MuLungDojoF23: 9305622,  # Lord Pirate
    MuLungDojoF24: 9305623,  # Deet and Roi
    MuLungDojoF25: 9305624,  # Frankenroid
    MuLungDojoF26: 9305625,  # Chimera
    MuLungDojoF27: 9305626,  # Poison Golem
    MuLungDojoF28: 9305627,  # King Sage Cat
    MuLungDojoF29: 9305628,  # Crimson Balrog
    MuLungDojoF30: 9305629,  # Arkarium
    MuLungDojoF31: 9305630,  # Manon
    MuLungDojoF32: 9305631,  # Griffey
    MuLungDojoF33: 9305632,  # Snowman
    MuLungDojoF34: 9305633,  # Papulatus
    MuLungDojoF35: 9305634,  # Ani
    MuLungDojoF36: 9305635,  # Leviathan
    MuLungDojoF37: 9305636,  # Dodo
    MuLungDojoF38: 9305637,  # Lilynouch
    MuLungDojoF39: 9305638,  # Lyka
    MuLungDojoF40: 9305639,  # Magnus
    MuLungDojoF41: 9305656,  # King Omen
    MuLungDojoF42: 9305657,
    MuLungDojoF43: 9305658,
    MuLungDojoF44: 9305659,
    MuLungDojoF45: 9305660,
    MuLungDojoF46: 9305661,
    MuLungDojoF47: 9305662,
    MuLungDojoF48: 9305663,
    MuLungDojoF49: 9305664,
    MuLungDojoF50: 9305665,
    MuLungDojoF51: 9305666,
    MuLungDojoF52: 9305667,
    MuLungDojoF53: 9305668,
    MuLungDojoF54: 9305669,
    MuLungDojoF55: 9305670,
    MuLungDojoF56: 9305671,
    MuLungDojoF57: 9305672,
    MuLungDojoF58: 9305673,
    MuLungDojoF59: 9305674,
    MuLungDojoF60: 9305675,
    MuLungDojoF61: 9305676,
    MuLungDojoF62: 9305677,
    MuLungDojoF63: 9305640
}

currentPoint = 0
stage = ((sm.getFieldID() % 10000) / 100)
if stage == 1:
    sm.createQuestWithQRValue(3847, "Time=0;Floor=0;Result=start")
    sm.showTimerInfoEx(900, 0)
else:
    sm.pauseInstanceTime(False)
    randomNum1 = random.randint(10, 75)
    stagePoint = int(stage * 3.5)
    pointPer = int(randomNum1 + stagePoint)
    currentPoint = int(chr.getDojoPoints() + pointPer)
    chr.setDojoPoints(currentPoint)

sm.showFieldEffect(WzConstants.EFFECT_DOJO_STAGE)
sm.showFieldEffect(str(WzConstants.EFFECT_DOJO_STAGE_NUMBER) + str(stage))
chr.chatMessage("You have " + str(currentPoint) + " Dojo Points")

sm.spawnMobWithAppearType(bossPerFloor[sm.getFieldID()], 0, 7, 1, 3000)  # Spawns mob based on Field ID

sm.waitForMobDeath(bossPerFloor[sm.getFieldID()])

sm.pauseInstanceTime(True)
sm.createQuestWithQRValue(3847, "Time="+str(900 - sm.getInstanceRemainingTime())+";Floor="+str(stage)+";Result=start")
sm.showFieldEffect(WzConstants.EFFECT_DOJO_CLEAR)
