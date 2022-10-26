# Portal from Lachelein Main street (450003000) to Nightmare Clocktower 1F

MAP = 450003500 # Nightmare Clocktower 1F
PORTAL = 1

if sm.hasQuest(34326) or sm.hasQuestCompleted(34326):
    sm.warp(MAP, PORTAL)