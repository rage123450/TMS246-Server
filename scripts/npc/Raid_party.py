# Vote Points & Donation Points NPC
# Custom server script

import math

sm.setSpeakerID(2007) # Maple Administrator

# ======================================================================================================================
# VP Items
# ======================================================================================================================

vp_exp = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
    [2022461, 1, 1, 0], # Cassandra's Reward 3
    [2022463, 1, 1, 0], # Cassandra's Reward 5
    [5211048, 240, 1, 1], # 4-hour 2x Exp Special Coupon
    [5360042, 240, 1, 1], # 4-hour 2x Drop Special Coupon
    [5211046, 1440, 1, 1], # 1-day 2x Exp Special Coupon
    [5360000, 1440, 1, 1], # 1-day 2x Drop Special Coupon
    [2023380, 1, 1, 0] # 2x Exp Coupon (30 min)
]

vp_cosmetics = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
	[2430182, 10, 1, 0], # Special Beauty Coupon
	[2210010, 10, 1, 0] # Potion of Transformation
]

vp_game_changers = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
    [5680047, 120, 1, 1], # Pvac
	[2630793, 1, 1, 0] # Fury Totem
]

vp_pet_shop = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
    [5190013, 1, 1, 0] # Open Pet Shop Skill
]

# ======================================================================================================================
# DP Items
# ======================================================================================================================

dp_exp = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
    [2450015, 1, 1, 0], # 3x Exp 30 Min
    [2450016, 1, 1, 0], # 3x Exp 60 Min
    [2023722, 1, 1, 0] # [Dice Master] Meso Drop Rate Buff
]

dp_cosmetics = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
    [5552000, 1, 1, 0], # Hair Slot
    [5553000, 1, 1, 0], # Face Slot
	[5155000, 1, 1, 0], # Carta's Indigo Pearl
	[5155004, 1, 1, 0], # Carta's Teal Pearl
	[5155005, 1, 1, 0] # Carta's Scarlet Pearl
]

dp_game_changers = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
    [5680047, 10080, 1, 1], # Pvac
    [1202236, 10080, 1, 1], # Frenzy Totem
    [4034803, 1, 1, 0], # Name Change Coupon
    [2435383, 1, 1, 0], # Pendant Slot Permanent Coupon
	[1122303, 43200, 1, 1], # Hellia Necklace
	[1132183, 43200, 1, 1], # Avenger Quiver Belt
	[1152101, 43200, 1, 1], # Doom Shoulder
	[1113171, 43200, 1, 1], # Grin Ring
    [1202089, 43200, 1, 1], # Sanada Yukimura Totem
    [1202090, 43200, 1, 1], # Honda Tadakatsu Totem
    [1202091, 43200, 1, 1] # Uesugi Kenshin Totem
]

dp_surprise_box = [
    # ItemID, Quantity/Duration, Cost, Time-sensitive (0 : false | 1 : enabled)
	[2435163, 1, 1, 0], # Random Damage Skin Box
	[2437025, 1, 1, 0] # Random Planet Chair Box
]

# ======================================================================================================================
# Menu String Options
# ======================================================================================================================

# Options for main menu
main_menu = sm.options("Trade in Vote Points", "Trade in Donation Points")
# Options for vote point menu
vp_menu = sm.options("Exp / Drop Coupons", "Cosmetics", "Game Changers", "Pet Items")
# Options for donation point menu
dp_menu = sm.options("Exp / Drop Coupons", "Cosmetics", "Game Changers", "Surprise Box")

# ======================================================================================================================
# Time Formatter
# ======================================================================================================================

def transform_minutes(total_minutes):

    days = math.floor(total_minutes / (24 * 60))
    leftover_minutes = total_minutes % (24 * 60)
    hours = math.floor(leftover_minutes / 60)
    minutes = total_minutes - (days * 1440) - (hours * 60)

    if days > 0:
        out = '{:n} Days'.format(days)
    elif hours > 0:
        out = '{:n} Hours'.format(hours)
    else:
        out = '{:n} Minutes'.format(minutes)

    return out

# ======================================================================================================================

option = ""
def showOptions(text, items):
    option = text + "\r\n#b"
    for x in range (len(items)):
        name = items[x][0]
        qty  = items[x][1]
        cost = items[x][2]
        timed = items[x][3]

        if timed == 1: # is time sensitive
            option += "#L" + str(x) + "##i" + str(name) + "# #z" + str(name) + "# (" + str(transform_minutes(qty)) + ")" + " (" + str(cost) + " Points)" + "#l \r\n"
        else:
            option += "#L" + str(x) + "##i" + str(name) + "# #z" + str(name) + "# (" + str(qty) + ")" + " (" + str(cost) + " Points)" + "#l \r\n"

    return sm.sendNext(option)

# ======================================================================================================================
# Exchange
# ======================================================================================================================

def exchange(opt, items, donation):
    name = items[opt][0]
    qty  = items[opt][1]
    cost = items[opt][2]
    timed = items[opt][3]

    currency = sm.getVotePoints()
    currencyName = "vote points"
    if donation:
        currency = sm.getDonationPoints()
        currencyName = "donation points"

    durOrQty = ""
    if timed:
        durOrQty = "(#b" + str(transform_minutes(qty)) + "#k)"
    else:
        durOrQty = "(x#b" + str(qty) + "#k)"

    timeMsg = ""
    if timed == 1:
        timeMsg = "\r\n\r\n(#rThis is a time-sensitive item, duration until expire will start as soon as item is in your inventory!!#k)"

    if sm.sendAskYesNo("You currently have #b" + str(currency) + " " + currencyName + "#k.\r\nAre you sure you want the following item(s)?\r\n\r\n#i" + str(name) + "# #b#z" + str(name) + "##k " + durOrQty + " for #r" + str(cost) + "#k " + currencyName + "?" + timeMsg):
        if currency >= cost:
            if sm.canHold(name):
                if timed == 1: # is time sensitive
                    sm.giveItemWithExpireDate(name, 1, False, qty)
                else:
                    sm.giveItem(name, qty)

                if donation: # is donation points
                    sm.deductDonationPoints(cost)
                else:
                    sm.deductVotePoints(cost)
                sm.sendSayOkay("You have obtained " + durOrQty + " #b#z" + str(name) + "##k for #r" + str(cost) + "#k " + currencyName + ".")
            else:
                sm.sendNext("Please make sure you have enough space in your inventory.")
        else:
            sm.sendNext("You don't have enough " + currencyName + ". You need #r" + str(cost) + "#k " + currencyName + ".")

# ======================================================================================================================

def showAndExchange(msg, items, donation):
     selection = showOptions(msg, items)
     exchange(selection, items, donation)

# ======================================================================================================================
# Vote Points Menu
# ======================================================================================================================

def votePointOptions():
    type = False
    prompt = "You currently have #b" + str(sm.getVotePoints()) + " vote points#k.\r\nWhat would you like to buy with your vote points?\r\n\r\n(#dYou can obtain vote points by voting for us every 12 hours through our website or discord#k.)\r\n"
    selection = sm.sendNext(prompt + "#b" + vp_menu + "#k")

    if selection == 0:
        showAndExchange("What would you like from the Exp / Drop coupon shop?", vp_exp, type)
    elif selection == 1:
        showAndExchange("What would you like from the Cosmetics shop?", vp_cosmetics, type)
    elif selection == 2:
        showAndExchange("What would you like from the Game Changers shop?", vp_game_changers, type)
    elif selection == 3:
        showAndExchange("What would you like from the Pet shop?", vp_pet_shop, type)

# ======================================================================================================================
# Donation Points Menu
# ======================================================================================================================

def donationPointOptions():
    type = True # is DP
    prompt = "You currently have #b" + str(sm.getDonationPoints()) + " donation points#k.\r\nWhat would you like to buy with your donation points?\r\n\r\n(#dYou can obtain donation points by visiting our website and purchasing them by clicking on the store#k.)\r\n"
    selection = sm.sendNext(prompt + "#b" + dp_menu + "#k")

    if selection == 0:
        showAndExchange("What would you like from the Exp / Drop coupon shop?", dp_exp, type)
    elif selection == 1:
        showAndExchange("What would you like from the Cosmetics shop?", dp_cosmetics, type)
    elif selection == 2:
        showAndExchange("What would you like from the Game Changers shop?", dp_game_changers, type)
    elif selection == 3:
        showAndExchange("What would you like from the Surprise Box shop?", dp_surprise_box, type)
    else:
        sm.sendSayOkay("An error has occurred. Please report to admins.")

# ======================================================================================================================
# Start Menu
# ======================================================================================================================

selection = sm.sendNext("Hello #r#h0##k! How can I help you today?\r\n#b" + main_menu + "#k")
if selection:
    donationPointOptions()
else:
	votePointOptions()