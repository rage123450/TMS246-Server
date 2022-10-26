# Portal from Lachelein Main street (450003000) to Revelation Place 1

MAP = 450003400 # Revelation Place 1
PORTAL = 4

if sm.hasQuest(34319) or sm.hasQuestCompleted(34319):
    sm.warp(MAP, PORTAL)