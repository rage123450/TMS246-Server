from net.swordie.ms.enums import DimensionalPortalTownType

current = sm.getFieldID()
response = sm.sendAskSlideMenu(5)
mapID = DimensionalPortalTownType.getByVal(response).getMapID()

if mapID != 0 and sm.getFieldID() == current:
    sm.setReturnField()
    sm.warp(mapID)