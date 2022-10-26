# A NPC script based on 'Swordie NX Shop NPC'
# Custom server script

cubes = [
    # ItemID, Cost
    [5062009, 1200], # Red Cube
    [5062010, 2200], # Black Cube
    [5062500, 2400] # Bonus Potential Cube
]

scrollsAndFreezers = [
    # ItemID, Cost
    [2049716, 10000], # Epic Potential Scroll 100%
    [2048305, 150000], # Bonus Potential Scroll 70%
    [2531005, 20000], # Protection Scroll
    [2530003, 15000], # Lucky Day Scroll
    [2532000, 30000], # Safety Scroll
    [2049100, 2500], # Chaos Scroll 60%
    [2049600, 50000], # Innocence Scroll 70%
    [2049301, 10000], # Equip Enhancement Scroll
    [2049300, 250000], # Advanced Equip Enhancement Scroll
    [5133000, 200] # Buff Freezer
]

stamps = [
    # ItemID, Cost
    [2049505, 10000], # Gold Potential Stamp
    [2048302, 30000] # Gold Bonus Potential Stamp
]

hammers = [
    # ItemID, Cost
    [2470011, 4900] # Golden Hammer 100%
]

flames = [
    # ItemID, Cost
    [2048716, 20000] # Powerful Rebirth Flame
]

# ======================================================================================================================

option = ""
def showOptions(text, items):
    option = text + "\r\n#b"
    for x in range (len(items)):
        itemId = items[x][0]
        cost = items[x][1]

        option += "#L" + str(x) + "##i" + str(itemId) + "# #z" + str(itemId) + "# - " + str(cost) + " NX" "#l\r\n"

    return sm.sendNext(option)

# ======================================================================================================================

def exchange(selection, items):
    itemId = items[selection][0]
    cost = items[selection][1]

    qty = sm.sendAskNumber("How many #i" + str(itemId) + "# #b#z" + str(itemId) + "##k do you want to purchase?", 1, 1, 1000)
    totalCost = cost * qty

    playerCurrency = chr.getUser().getNxPrepaid()

    if sm.sendAskYesNo("You currently have #b" + str(playerCurrency) + " NX#k.\r\nAre you sure you want the following item(s)?\r\n\r\n#i" + str(itemId) + "# #b#z" + str(itemId) + "##k (x#b" + str(qty) + "#k) for #r" + str(totalCost) + " #kNX?"):
        if playerCurrency >= totalCost:
            if sm.canHold(itemId):
                sm.giveItem(itemId, qty)
                chr.deductNX(totalCost)
            else:
                sm.sendSayOkay("Please make sure you have enough space in your inventory.")
        else:
            sm.sendSayOkay("You don't have enough #rNX#k to buy this item.")

# ======================================================================================================================

def showAndExchange(text, items):
    selection = showOptions(text, items)
    exchange(selection, items)

# ======================================================================================================================

main_menu = sm.options("Cubes", "Scrolls and Buff Freezers", "Stamps", "Hammer", "Flames")

# ======================================================================================================================

selection = sm.sendNext("You currently have #b" + str(chr.getUser().getNxPrepaid()) + "#k NX.\r\nWhat would you like to buy?\r\n#b" + main_menu + "#k")
if selection == 0:
    showAndExchange("What kind of cube would you like to buy?", cubes)
elif selection == 1:
    showAndExchange("What kind of scroll would you like to buy?", scrollsAndFreezers)
elif selection == 2:
    showAndExchange("What kind of stamp would you like to buy?", stamps)
elif selection == 3:
    showAndExchange("What kind of hammer would you like to buy?", hammers)
elif selection == 4:
    showAndExchange("What kind of flame would you like to buy?", flames)
else:
    sm.sendSayOkay("An error has occurred. Please report to admins.")