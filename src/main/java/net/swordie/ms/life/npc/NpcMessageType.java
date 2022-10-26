package net.swordie.ms.life.npc;

import net.swordie.ms.util.Util;

/**
 * Created on 2/19/2018.
 */
public enum NpcMessageType {
    SayOk(0, false, false, ResponseType.Response),
    SayNext(0, false, true, ResponseType.Response),
    SayPrev(0, true, false, ResponseType.Response),
    Say(0, true, true, ResponseType.Response),
    SayUnk(1, ResponseType.Response),
    SayImage(2, ResponseType.Response),
    AskYesNo(3, ResponseType.Response),
    AskText(4, ResponseType.Text),
    AskNumber(5, ResponseType.Answer),
    AskMenu(6, ResponseType.Answer),
    InitialQuiz(7, ResponseType.Answer),
    InitialSpeedQuiz(8, ResponseType.Answer),
    ICQuiz(9, ResponseType.Answer),
    AskAvatar(10, ResponseType.Answer),
    AskAndroid(11, ResponseType.Answer),
    // 12
    AskPet(13, ResponseType.Answer),
    AskPetAll(14, ResponseType.Answer),
    AskActionPetEvolution(15, ResponseType.Answer),
    AskAccept2(16, ResponseType.Response),
    AskAccept(17, ResponseType.Response),
    AskBoxtext(18, ResponseType.Answer),
    AskSlideMenu(19, ResponseType.Answer),

    AskIngameDirection(21, ResponseType.Response),
    PlayMovieClip(22, ResponseType.Response),
    AskCenter(23, ResponseType.Answer),
    AskAvatar2(24, ResponseType.Answer),
    AskSelectMenu(26, ResponseType.Answer),

    AskAngelicBuster(27, ResponseType.Answer),
    SayIllustration(28, ResponseType.Answer),
    SayDualIllustration(29, ResponseType.Answer),
    AskYesNoIllustration(30, ResponseType.Answer),
    AskAcceptIllustration(31, ResponseType.Answer),
    AskMenuIllustration(32, ResponseType.Answer),
    AskYesNoDualIllustration(33, ResponseType.Answer),
    AskAcceptDualIllustration(34, ResponseType.Answer),
    AskMenuDualIllustration(35, ResponseType.Answer),
    AskAvatarZero(36, ResponseType.Answer),
    AskWeaponBox(39, ResponseType.Answer),
    AskBoxTextBgImg(40, ResponseType.Answer),
    AskUserSurvey(41, ResponseType.Answer),
    Monologue(42, ResponseType.Response),
    AskMixHair(43, ResponseType.Answer),
    AskMixHairExZero(44, ResponseType.Answer),
    OnAskCustomMixHair(45, ResponseType.Answer),
    OnAskCustomMixHairAndProb(46, ResponseType.Answer),
    OnAskMixHairNew(47, ResponseType.Answer),
    OnAskMixHairNewExZero(48, ResponseType.Answer),
    OnAskScreenShinningStarMsg(49, ResponseType.Answer),
    OnAskNumberUseKeyPad(52, ResponseType.Answer),
    OnSpinOffGuitarRhythmGame(53, ResponseType.Answer),
    OnGhostParkEnter(54, ResponseType.Answer),
    None(-1, ResponseType.Answer),
    ;

    private byte val;
    private boolean prevPossible, nextPossible;
    private int delay;
    private ResponseType responseType;

    NpcMessageType(int val, ResponseType responseType) {
        this.val = (byte) val;
        prevPossible = false;
        nextPossible = false;
        this.responseType = responseType;
    }

    NpcMessageType(int val, boolean prev, boolean next, ResponseType responseType) {
        this.val = (byte) val;
        prevPossible = prev;
        nextPossible = next;
        this.responseType = responseType;
    }

    public static NpcMessageType getByVal(byte val) {
        return Util.findWithPred(values(), v -> v.getVal() == val);
    }

    public byte getVal() {
        return val;
    }

    public boolean isPrevPossible() {
        return prevPossible;
    }

    public boolean isNextPossible() {
        return nextPossible;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }


    public ResponseType getResponseType() {
        return responseType;
    }

    public enum ResponseType {
        Response, Answer, Text
    }
}
