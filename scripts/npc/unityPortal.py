from net.swordie.ms.enums import DimensionalPortalType

current = sm.getFieldID()
response = sm.sendAskSlideMenu(0)
mapID = DimensionalPortalType.getByVal(response).getMapID()

if mapID != 0 and sm.getFieldID() == current:
    sm.setReturnField()
    sm.warp(mapID)