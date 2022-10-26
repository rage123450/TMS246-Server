# Portal from FM to return map (910000000)

if not sm.getReturnField() == 0:
    sm.warp(sm.getReturnField())
else:
    sm.warp(chr.getPreviousFieldID())