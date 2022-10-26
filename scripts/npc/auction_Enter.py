# Eggrich - Auction NPC

from net.swordie.ms.connection.packet import Stage
from net.swordie.ms.connection.packet import FieldPacket
from net.swordie.ms.world.auction import AuctionResult

if sm.sendAskYesNo("Do you want to go to the #bAuction House#k?"):
    if chr.getField().isTown():
        chr.enterNewStageField()
        chr.write(Stage.setAuctionField(chr))
        chr.write(FieldPacket.auctionResult(AuctionResult.initialize()))
        chr.getField().removeChar(chr)
    else:
        chr.dispose()