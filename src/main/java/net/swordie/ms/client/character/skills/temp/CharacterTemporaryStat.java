package net.swordie.ms.client.character.skills.temp;

import net.swordie.ms.util.Util;
import org.apache.log4j.LogManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Created on 1/2/2018.
 */
public enum CharacterTemporaryStat implements Comparator<CharacterTemporaryStat> {
    IndiePAD(0),
    IndieMAD(1),
    IndieDEF(2),
    IndieMHP(3),
    IndieMHPR(4),
    IndieMMP(5),
    IndieMMPR(6),
    IndieACC(7),
    IndieEVA(8),
    IndieJump(9),
    IndieSpeed(10),//移動速度
    IndieAllStat(11),
    IndieAllStatR(12),
    IndieDodgeCriticalTime(13),
    IndieEXP(14),
    IndieBooster(15),//武器加速
    IndieFixedDamageR(16),
    PyramidStunBuff(17), // Osiris' Eye: Stuns monsters for 1 second. Includes Pharaoh Yetis and Pharaoh Mummies at a 30% chance. Costs 500 points.
    PyramidFrozenBuff(18), // Horus' Eye: Slows down all monsters for 15 seconds. Includes Pharaoh Yetis and Pharaoh Mummies at a 30% chance. Costs 700 points.
    PyramidFireBuff(19), // Isis' Eye: Does Damage over time to all monsters for 15 seconds every second. Costs 500 points.
    PyramidBonusDamageBuff(20), // Anubis' Eye: Increases 40 attack for 15 seconds. Costs 500 points.
    IndieRelaxEXP(21),
    IndieSTR(22),
    IndieDEX(23),

    IndieINT(24),
    IndieLUK(25),
    IndieDamR(26),
    IndieScriptBuff(27),
    IndieMDF(28),
    IndieAsrR(29),
    IndieTerR(30),
    IndieCr(31),
    IndiePDDR(32),
    IndieCrDmg(33),
    IndieBDR(34), // 增加B傷
    IndieStatR(35),
    IndieStance(36), // 格檔機率
    IndieIgnoreMobpdpR(37),
    IndieEmpty(38),
    IndiePADR(39),
    IndieMADR(40),
    IndieEVAR(41),
    IndieDrainHP(42),
    IndiePMdR(43),
    IndieForceJump(44), // Max Jump (?)
    IndieForceSpeed(45), // Max Speed (?)
    IndieQrPointTerm(46),

    IndieSummon(47), // Seems to be used by GMS for almost all their summons
    IndieCooltimeReduce(48),
    IndieNotDamaged(49), // Given by Soul Eclipse DW 3rd V & Omega Blaster (Attack mode)  |  Invincibility
    IndieKeyDownTime(50), // DemonicBlast | Twin Blades of Time
    IndieDamReduceR(51),
    IndieCrystalCharge(52),
    IndieNegativeEVAR(53),
    IndieHitDamage(54),
    IndieNoKnockBack(55),//-> 60?
    Indie205_56(56), // err38
    IndieHitDamageInclHPR(57), // Indie Hit Damage, including HP% hits
    IndieArcaneForce(58),
    Indie205_59(59), // may not be here, but at the very least before IndieUNK3
    Indie205_60(60),
    Indie205_61(61),
    NuclearOption(62),
    IndieUNK3(63), // weird one: also gets encoded in normal stat
    IndiePartyExp(64),
    IndieUNK5(65),
    IndiePartyDrop(66),
    IndieUnk67(67),
    IndieFloating(69),//飛行
    IndieStatCount(78),

    PAD(79),//物理攻擊力
    DEF(80),//物理防御力
    MAD(81),//魔法防御力
    ACC(82),//命中率
    EVA(83),//迴避率

    Craft(84),//手技
    Speed(85),//移動速度
    Jump(86),//跳躍力
    MagicGuard(87),//魔心防禦
    DarkSight(88),//隱藏術
    Booster(89),//攻擊加速
    PowerGuard(90),//傷害反擊
    MaxHP(91),//神聖之火_最大HP
    MaxMP(92),//神聖之火_最大MP

    Invincible(93),//神聖之光
    SoulArrow(94),//無形之箭
    Stun(95),//昏迷
    Poison(96),//中毒
    Seal(97),//封印
    Darkness(98),//黑暗
    ComboCounter(99),//鬥氣集中
    WorldReaver(-1),
    HammerOfTheRighteous(100),
    BigHammerOfTheRighteous(101),
    WeaponCharge(102),

    HolySymbol(103),//*實用的祈禱
    MesoUp(104),
    ShadowPartner(105),
    Steal(106),
    PickPocket(107),
    MesoGuard(108),
    Thaw(109),
    Weakness(110),
    Curse(111),
    Slow(112),
    Morph(113),
    Regen(114),
    BasicStatUp(115),
    Stance(116), // *
    SharpEyes(117),
    ManaReflection(118),
    Attract(119),
    NoBulletConsume(120),
    Infinity(121), // *
    AdvancedBless(122),
    IllusionStep(123),
    Blind(124),
    Concentration(125),
    BanMap(126),
    MaxLevelBuff(127),
    MesoUpByItem(128),
    WealthOfUnion(129),
    RuneOfGreed(130),
    Ghost(131),
    Barrier(132),
    Unk111(-1), // err38 - other Morph
    Unk112(-1), // Looks like DojangShield
    ReverseInput(133),
    ItemUpByItem(134),
    RespectPImmune(135),
    RespectMImmune(136),
    DefenseAtt(137),
    DefenseState(138),
    DojangBerserk(139),
    DojangInvincible(140),

    DojangShield(141),
    SoulMasterFinal(142),
    WindBreakerFinal(143),
    ElementalReset(144),
    HideAttack(145),
    EventRate(146),
    ComboAbilityBuff(147),
    ComboDrain(148),
    ComboBarrier(149),

    BodyPressure(150),
    RepeatEffect(151),
    ExpBuffRate(152),
    StopPortion(153),
    StopMotion(154),
    Fear(155),
    HiddenPieceOn(156),
    MagicShield(157),
    MagicResistance(158),
    SoulStone(159),

    Flying(160), // *
    Frozen(161), // *
    AssistCharge(162),
    Enrage(163),
    DrawBack(164),
    NotDamaged(165),
    FinalCut(166), // *
    HowlingAttackDamage(167),
    BeastFormDamageUp(168),
    Dance(169),

    EMHP(170),
    EMMP(171),
    EPAD(172),
    EMAD(173),
    EPDD(174),
    Guard(175),//PerfectArmor?
    Cyclone(176),
    RadiantOrb(177),
    HowlingCritical(-1),
    HowlingMaxMP(-1),
    HowlingDefence(-1),
    // 1 2 of these are removed, idk which
    HowlingEvasion(-1),
    Conversion(-1),
//    Revive(169), either Revive or PinkbeanMinibeenMove is removed
    PinkbeanMinibeenMove(180),
    Sneak(181),
    Mechanic(182),
    BeastFormMaxHP(183),
    Dice(184),
    BlessingArmor(185),
    DamR(186),
    TeleportMasteryOn(187),
    CombatOrders(188),
    Beholder(189),
    DispelItemOption(190),
    Inflation(191),
    OnixDivineProtection(192),
    Web(193),
    Unk188_177(194),
    Bless(195),
//    TimeBomb(183), // removed 188?
    DisOrder(196),
    Thread(197),
    Team(198),
    Explosion(199),
    BuffLimit(200),

//------------------------
    STR(202),
    INT(203),
    DEX(204),
    LUK(205),
    DispelItemOptionByField(206),
    DarkTornado(207),
    PVPDamage(208),
    PvPScoreBonus(209),
    PvPInvincible(210),
    PvPRaceEffect(211),
    WeaknessMdamage(212),
    Frozen2(213), //
    PVPDamageSkill(214),
    AmplifyDamage(215),
    Shock(216),
    InfinityForce(217),
    IncMaxHP(218),
    IncMaxMP(219),
    HolyMagicShell(220),
    KeyDownTimeIgnore(221),
    ArcaneAim(222),
    MasterMagicOn(223),
    AsrR(224),
    TerR(225),
    DamAbsorbShield(226),
    DevilishPower(227),
    Roulette(228),
    SpiritLink(229),
    AsrRByItem(230),
    Event(231),
    CriticalBuff(232),
    DropRate(233),
    PlusExpRate(234),
    ItemInvincible(235),
    Awake(236),
    ItemCritical(237),
    Event2(238),

    VampiricTouch(230),
    DDR(231),
    IncCriticalDam(232),
    IncTerR(233),
    IncAsrR(234),
    DeathMark(235),
    UsefulAdvancedBless(236),
    Lapidification(237),
    VenomSnake(238),
    CarnivalAttack(239),
    CarnivalDefence(240),
    CarnivalExp(241),
    SlowAttack(242),
    PyramidEffect(243),
    KillingPoint(-1/*244*/),
    HollowPointBullet(245),
    KeyDownMoving(253), //
    IgnoreTargetDEF(254),
    ReviveOnce(248),

    Invisible(249),
    EnrageCr(250),
    EnrageCrDamMin(251),
    Judgement(259),
    DojangLuckyBonus(253),
    PainMark(254),
    Magnet(255),//262?
    MagnetArea(256),//263?

    GuidedArrow(257),
    SpiritFlow(258),
    LucentBrand(266), //
    ExtraSkillCTS(267),
    Unk199_256(261),
    TideOfBattle(262),
    GrandGuardian(263),
    Unk200_260(264),
    VampDeath(274),
    BlessingArmorIncPAD(266),
    KeyDownAreaMoving(267),
    Larkness(277),
    StackBuff(278),
    BlessOfDarkness(279),
    AntiMagicShell(280), // *
    AntiMagicShellEx(280),
    LifeTidal(281),
    HitCriDamR(282),
    SmashStack(283),
    PartyBarrier(284),
    ReshuffleSwitch(285),
    SpecialAction(286),
    VampDeathSummon(287),
    StopForceAtomInfo(288),
    SoulGazeCriDamR(289),
    SoulRageCount(290),
    PowerTransferGauge(291),
    AffinitySlug(292),
    Trinity(293),
    IncMaxDamage(294),
    BossShield(295),
    MobZoneState(296),
    GiveMeHeal(297),
    TouchMe(298),
    Contagion(299), // dc
    ComboUnlimited(300),
    SoulExalt(301),
    IgnorePCounter(302),
    IgnoreAllCounter(303),
    IgnorePImmune(304),
    IgnoreAllImmune(305),
    FinalJudgement(-1),
    Unk188_283(306),

    FireAura(307),
    VengeanceOfAngel(312), // * 308 -> 312
    HeavensDoor(309),

    Preparation(303),
    BullsEye(304),
    IncEffectHPPotion(305),
    IncEffectMPPotion(306),
    BleedingToxin(307),
    IgnoreMobDamR(308),
    Asura(309),
    OmegaBlaster(310),
    FlipTheCoin(311),

    UnityOfPower(312),
    Stimulate(313),
    ReturnTeleport(314),
    DropRIncrease(322),
    IgnoreMobpdpR(323),
    BdR(324),
    CapDebuff(318),

    Exceed(319),
    DiabolikRecovery(320),
    FinalAttackProp(321),
    ExceedOverload(322),
    OverloadCount(323),
    BuckShot(324),
    FireBomb(325),
    HalfstatByDebuff(326),
    SurplusSupply(327),
    SetBaseDamage(328),
    EVAR(330),//329 -> 330
    NewFlying(337), // *
    AmaranthGenerator(332),
    OnCapsule(333),
    CygnusElementSkill(334),
    StrikerHyperElectric(335),
    EventPointAbsorb(336),
    EventAssemble(337),
    StormBringer(338),
    ACCR(339),
    DEXR(340),
    Albatross(341),
    Translucence(342),
    PoseType(343),
    LightOfSpirit(344),
    ElementSoul(345),
    GlimmeringTime(346),
    TrueSight(347),
    SoulExplosion(348),
    SoulMP(355),
    FullSoulMP(356),
    SoulSkillDamageUp(350),

    ElementalCharge(358), // *
    Restoration(352),
    CrossOverChain(360),
    //
    ChargeBuff(362),
    Reincarnation(363), // *
    KnightsAura(364),
    ChillingStep(365),
    DotBasedBuff(366),

    BlessEnsenble(367),
    ComboCostInc(368),
    ExtremeArchery(369),
    NaviFlying(362),
    QuiverCatridge(-1),
    AdvancedQuiver(364),
    UserControlMob(365),
    ImmuneBarrier(373), // *
    ArmorPiercing(374),

    CriticalGrowing(375),
    RelicEmblem(371), // *
    QuickDraw(372), // *
    BowMasterConcentration(373), // *
    TimeFastABuff(374),
    TimeFastBBuff(375),

    GatherDropR(376),
    AimBox2D(377),
    Unk203_374(378),
    IncMonsterBattleCaptureRate(379),
    CursorSniping(380),
    DebuffTolerance(381),
    DotHealHPPerSecond(382),
    SpiritGuard(386),//*
    PreReviveOnce(387),//*

    SetBaseDamageByBuff(388),//*
    LimitMP(386),
    ReflectDamR(387),
    ComboTempest(388),
    MHPCutR(389),
    MMPCutR(390),
    SelfWeakness(391),
    ElementDarkness(392),

    FlareTrick(393),
    Ember(399),
    Dominion(395),
    SiphonVitality(396),
    DarknessAscension(397),
    BossWaitingLinesBuff(398),
    DamageReduce(399),
    ShadowServant(400),

    ShadowIllusion(401),
    KnockBack(407), // *
    AddAttackCount(403),
    ComplusionSlant(404),
    JaguarSummoned(405),
    JaguarCount(406),

    SSFShootingAttack(412),//*
    DevilCry(413),
    ShieldAttack(415),//*
    BMageAura(410),//刪除了?
    DarkLighting(411),
    AttackCountX(412),
    BMageDeath(413),
    BombTime(414),

    NoDebuff(415),
    BattlePvPMikeShield(416),
    BattlePvPMikeBugle(417),
    XenonAegisSystem(418),
    AngelicBursterSoulSeeker(419),
    HiddenPossession(420),
    NightWalkerBat(421),
    NightLordMark(422),

    WizardIgnite(423),
    FireBarrier(424), // *
    ChangeFoxMan(425),
    DivineEcho(430),
    BattlePvPHelenaMark(431),
    BattlePvPHelenaWindSpirit(432),
    MastemasMark(458),//*
    RiftOfDamnation(430),

    QuiverBarrage(431),
    SwordsOfConsciousness(432),
    PrimalGrenade(433),
    Unk200_430(434),
    Unk200_431(435),
    BattlePvPLangEProtection(465),//*
    BattlePvPLeeMalNyunScaleUp(467),
    BattlePvPRevive(468),
    Unk188_419(469), // err38
    Unk188_420(470),
    PinkbeanAttackBuff(470),//*
    PinkbeanRelax(472),
    PinkbeanRollingGrade(473), // *

    PinkbeanYoYoStack(474),
    RandAreaAttack(475),
    Unk200_442(476),
    NextAttackEnhance(477),
//    AranBeyonderDamAbsorb(447), // between NextAttackEnhance and RoyalGuardState: 1 removed
    AranCombotempastOption(478),
    NautilusFinalAttack(479),
    ViperTimeLeap(480),
    RoyalGuardState(481), // *

    RoyalGuardPrepare(482),
    MichaelSoulLink(483),
    MichaelStanceLink(484),
    TriflingWhimOnOff(455),
    AddRangeOnOff(456),
    KinesisPsychicPoint(486), // * 457 -> 486
    KinesisPsychicShield(489), // *
    KinesisIncMastery(460),
    KinesisMind_Break(486),//
    KinesisPsychicOver(487),// *
    KinesisPsychicEnergeShield(489),// *
    BladeStance(491),
    DebuffActiveSkillHPCon(463),
    DebuffIncHP(464),
    BowMasterMortalBlow(465),
    AngelicBursterSoulResonance(466),
    Fever(467),

    IgnisRore(468),
    //    RpSiksin(459), somewhere between 436 and 459 one is removed
    TeleportMasteryRange(497), // 469 -> 497
    FixCoolTime(470),
    IncMobRateDummy(471),
    AdrenalinBoost(501), // *
    AranSmashSwing(502),
    AranDrain(503),

    AranBoostEndHunt(504),
    HiddenHyperLinkMaximization(505),
    RWCylinder(506),
    RWCombination(507),
    Unk188_460(508),
    RWMagnumBlow(509),
    RWBarrier(481),
    RWBarrierHeal(482),

    RWMaximizeCannon(483),
    RWOverHeat(484),
    UsingScouter(514),
    RWMovingEvar(486),
    Stigma(516),

    MahasFury(514),
    RuneCooltime(515),
    Unk188_471(516),
    Unk188_472(517),
    Unk188_473(518),
    Unk188_474(519),
    MeltDown(520),
    SparkleBurstStage(521),
    LightningCascade(522),
    BulletParty(523),
    Unk188_479(524),

    AuraScythe(525),
    Benediction(526),
    VoidStrike(527),
    ReduceHitDmgOnlyHPR(528),
    Unk199_493(529),
    WeaponAura(530),//*
    ManaOverload(531),
    RhoAias(532),// * done
    PsychicTornado(533),//* done 507 -> 533
    SpreadThrow(534),
    超載魔力(535),
    WindEnergy(535),
    MassDestructionRockets(536),
    ShadowAssault(537),
    Unk188_492(538),  // Awakened Relic placeholder
    Unk188_493(539),
    Unk188_494(540),
    BlitzShield(541),

    SplitShot(542),
    FreudBlessing(543),
    OverloadMode(573),
    SpotLight(574), // *
    Unk188_500(575),
    MuscleMemory(576), // *
    //    NuclearOption(522), // moved/removed
    WingsOfGlory(577), // *
    TrickBladeMobStat(578),
    UNK248_561(561),
    Overdrive(524),
    EtherealForm(525),
    LastResort(526),
    ViciousShot(527),
    Unk176_466(583), // *
    Unk200_527(584),
    Unk200_528(530),
    ImpenetrableSkin(521),//586 -> 521?

    Unk199_520(532),
    Unk199_521(533),
    Unk199_521Ex(533),
    Resonance(534),
    FlashCrystalBattery(535),
    Unk199_524(536),
    CrystalBattery(537),
    Unk199_526(538),
    Unk199_527(539),
    Unk199_528(540),
    Unk199_529(541),
    Unk199_530(542),
    Unk200_557(543),

    SpecterEnergy(599),
    SpecterState(600),
    BasicCast(601), // *
    ScarletCast(602),
    GustCast(603),
    AbyssalCast(604),
    ImpendingDeath(605),
    AbyssalRecall(606),
    ChargeSpellAmplifier(607),
    InfinitySpell(608),
    ConversionOverdrive(609),
    Solus(610),
    Unk199_543(611),
    Unk199_544(612),
    Unk199_545(613),
    Unk199_546(614),
    BigBangAttackCharge(615),//584?
    Unk199_548(616),
    TridentStrike(617),
    ComboInstinct(618),
    GaleBarrier(619),
    Unk199_552(620),
    Unk199_553(621),
    SwordOfLight(-1), // *
    PhantomMark(622),
    PhantomMarkMobStat(623),
    Unk200_568(624),
    Unk200_569(625),
    Unk200_570(626),
    KeyDownCTS(627), // Used in Keydown skills: 400011028, 37121052, 37121003‬, 400051024, 11121052, 400011091,  more
    Unk200_572(628), // Alliance Snow
    Unk200_573(629),
    //
    //
    //
    Unk200_574(633),
    Unk202_575(634),
    Unk202_576(635),
    TanadianRuin(636),
    AeonianRise(537),
    Unk202_579(581),
    Unk202_580(582),
    Unk205_583(583),
    Unk205_584(584),
    Unk205_585(585),
    AncientGuidance(642), // Ancient Guidance Mode   Mode and Stack should be encoded (x,y)
    Unk205_587(643),
    Unk205_588(644),
    Unk205_589(645),
    Unk205_590(590),
    Unk205_591(591),
    Unk205_592(592),
    Unk205_593(593),
    Unk199_555(594),
    Unk199_556(595),
    Unk199_557(596),
    Unk199_558(597),
    Unk199_559(598),

    HayatoStance(599), // *
    HayatoStanceBonus(600),
    EyeForEye(601),
    WillowDodge(602),

    Unk176_471(603),
    HayatoPAD(604),
    HayatoHPR(605),
    HayatoMPR(606),
    HayatoBooster(607),
    Unk176_476(608),
    Unk176_477(609),
    Jinsoku(610),

    HayatoCr(611),
    HakuBlessing(612),
    HayatoBoss(613),
    BattoujutsuAdvance(614),
    Unk176_483(615),
    Unk176_484(616),
    BlackHeartedCurse(617), // *

    BeastMode(618),
    TeamRoar(619), // *
    Unk176_488(620),
    Unk176_489(621),
    Unk176_490(622),
    Unk176_491(660), // Sets HP's number to xOption
    Unk176_492(661),
    Unk176_493(625),
    WaterSplashEventMarking(626),
    WaterSplashEventMarking2(627),
    WaterSplashEventCombo(628),
    WaterSplashEventWaterDripping(629),
    Unk188_539(630),
    YukiMusumeShoukan(696),
    IaijutsuBlade(632),
    Unk199_594(633),
    Unk199_595(634),
    Unk199_596(635),
    Unk199_597(636),
    BroAttack(637),
    LiberatedSpiritCircle(638),
    Unk205_639(639),
    Unk205_640(640),




    TalismanEnergy(659),
    ScrollEnergy(660),
    TalismanClone(661),
    CloneRampage(662),
    ButterflyDream(663),
    MiracleTonic(664), //361 sniffer ok
    SageWrathOfGods(665),
    EmpiricalKnowledge(666),
    UnkBuffStat52(667),
    UnkBuffStat53(668),
    Graffiti(669),
    DreamDowon(670),//按壓型 根據剩餘時間長短返還技能冷卻
    意志之劍_重磅出擊(671),//凱薩5轉新技能
    乙太(672),
    創造(673),
    護堤(674),
    奇蹟(675),
    復原(676),
    高潔精神(676),
    魔劍共鳴(677),
    //
    //
    //
    //
    //
    //
    IceAura(695),
    //696
    ZeroAuraStr(697),
    ZeroAuraSpd(698),
    //699
    //700
    亡靈(701),//惡復5轉新技
    亡靈傷害(702),
    殘影幻象(703),//弓箭手5轉共通技能
    極冰風暴(704),
    光子射線(705),
    深淵閃電(706),
    槍雷連擊(707),
    皇家騎士(708),
    火蜥蜴的惡作劇(709),
    引力法則(710),
    連射十字弓砲彈(711),
    水晶之門(712),
    飛閃起爆符(713),
    仙技_天地人幻影(714),
    黑暗靈氣(715),
    武器變換終章(716),
    解放寶珠(717),
    解放寶珠動作(718),
    自主武器(719),
    遺跡解放_釋放(720),
    //721
    殘影衝擊(722),
    //723
    主導(724),
    死亡祝福(725),
    死亡降臨(726),
    殘留憤恨(727),
    掌握痛苦(728),
    龍炸裂(729),
    //730
    //731
    //732
    事前準備(733),
    啟動鬥氣爆發(734),
    雪吉拉_火大了(735),
    //736
    //737
    //738
    魔術秀時間(739),
    龍脈讀取(740),
    山之種子(741),
    山不敗(742),//
    吸收_潑江水(743),
    吸收_凜冽的寒風(744),
    吸收_陽光之力(745),
    自由龍脈(746),
    花中君子(747),
    大自然夥伴(748),
    //
    //
    //
    海龍(751),
    海龍石(752),
    海龍螺旋(753),
    神聖之水(754),
    勝利之羽(755),
    閃光幻象(756),
    神聖之血(757),
    EnergyCharged(758),//641-> 758
    DashSpeed(759),
    DashJump(760),
    RideVehicle(761),
    PartyBooster(762),
    GuidedBullet(763),
    Undead(764),
    RideVehicleExpire(765),
    RelicGauge(766),
    死亡標記(767),
    None(-1),

    UNK248_763(763),
    UNK248_764(764),
    UNK248_768(768),
    UNK248_771(771),
    ;

    private int bitPos;
    private int val;
    private int pos;
    public static final int length = 33;
    private static final org.apache.log4j.Logger log = LogManager.getRootLogger();

    public static final List<CharacterTemporaryStat> ORDER = Arrays.asList(
            STR,INT,DEX,LUK,PAD,DEF,MAD,ACC,EVA,EVAR,Craft,Speed,Jump,EMHP,EMMP,EPAD,EMAD,EPDD,MagicGuard,DarkSight,
            Booster,PowerGuard,Guard,MaxHP,MaxMP,Invincible,SoulArrow,Stun,Shock,Poison,Seal,Darkness,ComboCounter,WorldReaver,HammerOfTheRighteous,BigHammerOfTheRighteous,
            WeaponCharge,ElementalCharge,HolySymbol,MesoUp,ShadowPartner,PickPocket,MesoGuard,
            Thaw,Weakness,WeaknessMdamage,Curse,Slow,Bless,BuffLimit,Team,DisOrder,Thread,Morph, Unk111,Regen,
            BasicStatUp,Stance,SharpEyes,ManaReflection,Attract,Magnet,MagnetArea, GuidedArrow, SpiritFlow, LucentBrand, ExtraSkillCTS,Unk199_256, TideOfBattle, NoBulletConsume,
            StackBuff,Trinity,Infinity,AdvancedBless,IllusionStep,Blind,Concentration,BanMap,MaxLevelBuff, Unk112,
            DojangShield,ReverseInput,MesoUpByItem,Ghost,Barrier,ItemUpByItem,RespectPImmune,RespectMImmune,DefenseAtt,
            DefenseState,DojangBerserk,DojangInvincible,SoulMasterFinal,WindBreakerFinal,ElementalReset,HideAttack,
            EventRate,ComboAbilityBuff,ComboDrain,ComboBarrier,PartyBarrier,BodyPressure,RepeatEffect,ExpBuffRate,
            StopPortion,StopMotion,Fear,MagicShield,MagicResistance,SoulStone,Flying,NewFlying,NaviFlying,Frozen,
            Frozen2,Web,Enrage,NotDamaged,FinalCut,HowlingAttackDamage,BeastFormDamageUp,Dance,OnCapsule,
            HowlingCritical,HowlingMaxMP,HowlingDefence,HowlingEvasion,Conversion,/*Revive,*/ PinkbeanMinibeenMove, Mechanic,DrawBack,
            /*Sneak,*/ BeastFormMaxHP,Dice,BlessingArmor,BlessingArmorIncPAD,DamR,TeleportMasteryOn,
            CombatOrders,Beholder,DispelItemOption,DispelItemOptionByField,Inflation,OnixDivineProtection,
            Unk188_177, Explosion,DarkTornado,IncMaxHP,IncMaxMP,PVPDamage,PVPDamageSkill,PvPScoreBonus,PvPInvincible,
            PvPRaceEffect,HolyMagicShell,InfinityForce,AmplifyDamage,KeyDownTimeIgnore,MasterMagicOn,AsrR,AsrRByItem,
            TerR,DamAbsorbShield,Roulette,Event,SpiritLink,CriticalBuff,DropRate,PlusExpRate,ItemInvincible,
            ItemCritical,Event2,VampiricTouch,DDR,IncCriticalDam,IncTerR,IncAsrR,DeathMark,PainMark,UsefulAdvancedBless,
            Lapidification,VampDeath,VampDeathSummon,VenomSnake,CarnivalAttack,CarnivalDefence,CarnivalExp,SlowAttack,
            PyramidEffect,HollowPointBullet,KeyDownMoving,KeyDownAreaMoving,CygnusElementSkill,IgnoreTargetDEF,
            Invisible,ReviveOnce,AntiMagicShell,EnrageCr,EnrageCrDamMin,BlessOfDarkness,LifeTidal,Judgement,
            DojangLuckyBonus,HitCriDamR,Larkness,SmashStack,ReshuffleSwitch,SpecialAction,ArcaneAim,
            StopForceAtomInfo,SoulGazeCriDamR,SoulRageCount,PowerTransferGauge,AffinitySlug,SoulExalt,HiddenPieceOn,
            BossShield,MobZoneState,GiveMeHeal,TouchMe,Contagion,ComboUnlimited,IgnorePCounter,IgnoreAllCounter,
            IgnorePImmune,IgnoreAllImmune,FinalJudgement, Unk188_283,KnightsAura,IceAura,FireAura,VengeanceOfAngel,
            HeavensDoor,Preparation,BullsEye,IncEffectHPPotion,IncEffectMPPotion,SoulMP,FullSoulMP,SoulSkillDamageUp,
            BleedingToxin,IgnoreMobDamR,Asura,OmegaBlaster,FlipTheCoin,UnityOfPower,Stimulate,ReturnTeleport, CapDebuff,
            DropRIncrease,IgnoreMobpdpR,BdR,Exceed,DiabolikRecovery,FinalAttackProp,ExceedOverload,DevilishPower,
            OverloadCount,BuckShot,FireBomb,HalfstatByDebuff,SurplusSupply,SetBaseDamage,AmaranthGenerator,
            StrikerHyperElectric,EventPointAbsorb,EventAssemble,StormBringer,ACCR,DEXR,Albatross,Translucence,
            PoseType,LightOfSpirit,ElementSoul,GlimmeringTime,Restoration,ComboCostInc,ChargeBuff,TrueSight,
            CrossOverChain,ChillingStep,Reincarnation,DotBasedBuff,BlessEnsenble,ExtremeArchery,QuiverCatridge,
            AdvancedQuiver,UserControlMob,ImmuneBarrier,ArmorPiercing,ZeroAuraStr,ZeroAuraSpd,CriticalGrowing,/*Unk202_367,*/
            QuickDraw, RelicEmblem,BowMasterConcentration,TimeFastABuff,TimeFastBBuff,GatherDropR,AimBox2D,Unk203_374,
            IncMonsterBattleCaptureRate,CursorSniping,DebuffTolerance,DotHealHPPerSecond,SpiritGuard,PreReviveOnce,
            SetBaseDamageByBuff,LimitMP,ReflectDamR,ComboTempest,MHPCutR,MMPCutR,SelfWeakness, ElementDarkness, FlareTrick,
            Ember,Dominion,SiphonVitality,DarknessAscension,BossWaitingLinesBuff,DamageReduce,
            ShadowServant, ShadowIllusion,AddAttackCount,ComplusionSlant,JaguarSummoned,JaguarCount,
            SSFShootingAttack,DevilCry,ShieldAttack,BMageAura,DarkLighting,AttackCountX,BMageDeath,BombTime,NoDebuff,
            XenonAegisSystem,AngelicBursterSoulSeeker,HiddenPossession,NightWalkerBat,NightLordMark,WizardIgnite,
            BattlePvPHelenaMark,BattlePvPHelenaWindSpirit,BattlePvPLangEProtection,BattlePvPLeeMalNyunScaleUp,
            BattlePvPRevive, Unk188_419, Unk188_420,PinkbeanAttackBuff,RandAreaAttack,Unk200_442,BattlePvPMikeShield,BattlePvPMikeBugle,
            PinkbeanRelax,PinkbeanYoYoStack, WindEnergy,NextAttackEnhance,/*AranBeyonderDamAbsorb,*/AranCombotempastOption,
            NautilusFinalAttack,ViperTimeLeap,RoyalGuardState,RoyalGuardPrepare,MichaelSoulLink,MichaelStanceLink,
            TriflingWhimOnOff,AddRangeOnOff,KinesisPsychicPoint,KinesisPsychicOver,KinesisPsychicShield,
            KinesisIncMastery,KinesisPsychicEnergeShield,BladeStance,DebuffActiveSkillHPCon,DebuffIncHP,
            BowMasterMortalBlow,AngelicBursterSoulResonance,Fever,IgnisRore,/*RpSiksin,*/TeleportMasteryRange,FireBarrier,
            ChangeFoxMan,FixCoolTime,IncMobRateDummy,AdrenalinBoost,AranSmashSwing,AranDrain,AranBoostEndHunt,
            HiddenHyperLinkMaximization,RWCylinder,RWCombination, Unk188_460,RWMagnumBlow,RWBarrier,RWBarrierHeal,
            RWMaximizeCannon,RWOverHeat,RWMovingEvar,Stigma,MahasFury, RuneCooltime, Unk188_471, Unk188_472, Unk188_473, Unk188_474, MeltDown, SparkleBurstStage,
            LightningCascade,BulletParty, Unk188_479, AuraScythe,Benediction, VoidStrike, ReduceHitDmgOnlyHPR, Unk199_493, DivineEcho,WeaponAura,ManaOverload, RhoAias,
            PsychicTornado,SpreadThrow, MassDestructionRockets, ShadowAssault, Unk188_492, Unk188_493, Unk188_494, BlitzShield, SplitShot, FreudBlessing,OverloadMode,

            MastemasMark, /*NuclearOption,*/ WingsOfGlory, RiftOfDamnation,
            Unk188_500,SpotLight, Cyclone, MuscleMemory, TrickBladeMobStat, Overdrive, EtherealForm, LastResort, ViciousShot,Unk176_466,Unk200_527,Unk200_528, ImpenetrableSkin,
            RadiantOrb,
            Unk199_520, Unk199_521, Resonance, FlashCrystalBattery,Unk199_524, CrystalBattery,Unk199_526,Unk199_527,Unk199_528,Unk199_529,
            Unk199_530, Unk200_557, SpecterEnergy, SpecterState, BasicCast, ScarletCast, GustCast, AbyssalCast, ImpendingDeath, AbyssalRecall, ChargeSpellAmplifier,
            InfinitySpell, ConversionOverdrive, Solus,Unk199_543,Unk199_544,Unk199_545,Unk199_546,Unk199_548, TridentStrike,
            ComboInstinct, GaleBarrier,GrandGuardian, QuiverBarrage,Unk199_552, SwordsOfConsciousness,Unk199_553, SwordOfLight, PhantomMark, PhantomMarkMobStat,
            PrimalGrenade,Unk200_430,Unk200_568,Unk200_569,Unk200_260, Unk200_431,Unk200_570, KeyDownCTS,Unk200_572,Unk200_573,Unk200_574,
            Unk202_575,Unk202_576, TanadianRuin, AeonianRise,Unk202_579,Unk202_580,Unk205_583,Unk205_584,Unk205_585, AncientGuidance,
            Unk205_587,Unk205_588,Unk205_589,Unk205_590,Unk205_591,
            Unk205_592,Unk205_593,
            IncMaxDamage,
            Unk199_555,Unk199_556,Unk199_558,Unk199_559,

            IndieUNK3,
            HayatoStance,HayatoBooster,HayatoStanceBonus,WillowDodge, Unk176_471,HayatoPAD,HayatoHPR,HayatoMPR,Jinsoku,
            HayatoCr,HakuBlessing,HayatoBoss, BattoujutsuAdvance, Unk176_483, Unk176_484,BlackHeartedCurse, EyeForEye,
            BeastMode,TeamRoar, Unk176_488, Unk176_489, Unk176_493, WaterSplashEventMarking, WaterSplashEventMarking2, WaterSplashEventCombo, WaterSplashEventWaterDripping,
            Unk188_539, YukiMusumeShoukan, IaijutsuBlade, Unk199_595,Unk199_596,Unk199_597, BroAttack, LiberatedSpiritCircle, Unk205_640,山不敗,KinesisMind_Break,超載魔力,
            DreamDowon,乙太,創造,奇蹟,魔劍共鳴,
            TalismanEnergy,ScrollEnergy,TalismanClone,MiracleTonic,CloneRampage

    );


    private static final List<CharacterTemporaryStat> REMOTE_ORDER = Arrays.asList(
            Speed,ComboCounter,WorldReaver,HammerOfTheRighteous,WeaponCharge,ElementalCharge,Stun,Shock,Darkness,Seal,
            Weakness,WeaknessMdamage,Curse,Slow,PvPRaceEffect,Bless,Team,DisOrder,Thread,Poison,Poison,ShadowPartner,
            DarkSight,SoulArrow,Morph,Unk111,Attract,Magnet,MagnetArea,NoBulletConsume,BanMap,Unk112,DojangShield,
            ReverseInput,RespectPImmune,RespectMImmune,DefenseAtt,DefenseState,DojangBerserk,DojangInvincible,
            RepeatEffect,Unk176_483,StopPortion,StopMotion,Fear,MagicShield,Flying,Frozen,Frozen2,Web,DrawBack,FinalCut,
            OnCapsule,OnCapsule,PinkbeanMinibeenMove,BeastFormDamageUp,Mechanic,BlessingArmor,BlessingArmorIncPAD,
            Inflation,Explosion,DarkTornado,AmplifyDamage,HideAttack,HolyMagicShell,DevilishPower,SpiritLink,Event,
            VampiricTouch,DeathMark,PainMark,Lapidification,VampDeath,VampDeathSummon,VenomSnake,PyramidEffect,
            KillingPoint,PinkbeanRollingGrade,IgnoreTargetDEF,Invisible,Judgement,KeyDownAreaMoving,StackBuff,
            BlessOfDarkness,Larkness,ReshuffleSwitch,SpecialAction,StopForceAtomInfo,SoulGazeCriDamR,PowerTransferGauge,
            BlitzShield,AffinitySlug,SoulExalt,HiddenPieceOn,SmashStack,MobZoneState,GiveMeHeal,TouchMe,Contagion,
            Contagion,ComboUnlimited,IgnorePCounter,IgnoreAllCounter,IgnorePImmune,IgnoreAllImmune,FinalJudgement,
            Unk188_283,KnightsAura,IceAura,FireAura,VengeanceOfAngel,HeavensDoor,DamAbsorbShield,AntiMagicShell,
            NotDamaged,BleedingToxin,WindBreakerFinal,IgnoreMobDamR,Asura,OmegaBlaster,UnityOfPower,Stimulate,
            ReturnTeleport,CapDebuff,OverloadCount,FireBomb,SurplusSupply,NewFlying,NaviFlying,AmaranthGenerator,
            CygnusElementSkill,StrikerHyperElectric,EventPointAbsorb,EventAssemble,Albatross,Translucence,PoseType,
            LightOfSpirit,ElementSoul,GlimmeringTime,Reincarnation,Beholder,QuiverCatridge,ArmorPiercing,UserControlMob,
            ZeroAuraStr,ZeroAuraSpd,ImmuneBarrier,ImmuneBarrier,FullSoulMP,AntiMagicShell,Dance,SpiritGuard,MastemasMark,
            ComboTempest,HalfstatByDebuff,ComplusionSlant,JaguarSummoned,BMageAura,BombTime,MeltDown,SparkleBurstStage,
            LightningCascade,BulletParty,Unk188_479,AuraScythe,Benediction,DarkLighting,AttackCountX,FireBarrier,
            KeyDownMoving,MichaelSoulLink,KinesisPsychicEnergeShield,BladeStance,BladeStance,Fever,AdrenalinBoost,
            RWBarrier,Unk188_460,RWMagnumBlow,GuidedArrow,SpiritFlow,LucentBrand,ExtraSkillCTS,Unk199_256,Stigma,
            DivineEcho,RhoAias,PsychicTornado,MahasFury,ManaOverload,Unk203_374,Unk188_500,SpotLight,OverloadMode,
            FreudBlessing,BigHammerOfTheRighteous,Overdrive,EtherealForm,LastResort,ViciousShot,Unk176_466,Unk200_527,
            Unk200_528,ImpenetrableSkin,WingsOfGlory,Unk199_520,Unk199_520,Unk199_521,Resonance,FlashCrystalBattery,
            SpecterState,ImpendingDeath,Unk199_545,Unk199_521,SwordOfLight,GrandGuardian,Unk205_584,Unk205_585,
            Unk205_588,Unk205_589,Unk205_590,Unk205_591,Unk199_555,BeastMode,TeamRoar,HayatoStance,HayatoStance,
            HayatoBooster,HayatoStanceBonus,HayatoPAD,HayatoHPR,HayatoMPR,HayatoCr,HayatoBoss,Stance,BattoujutsuAdvance,
            Unk176_484,BlackHeartedCurse,EyeForEye,Unk176_489,Unk176_493,WaterSplashEventMarking,
            WaterSplashEventMarking2,WaterSplashEventWaterDripping,Unk188_539,YukiMusumeShoukan,Unk199_559,Unk199_559,
            SplitShot


    );



    CharacterTemporaryStat(int val, int pos) {
        this.val = val;
        this.pos = pos;
    }

    CharacterTemporaryStat(int bitPos) {
        this.bitPos = bitPos;
        this.val = 1 << (31 - bitPos % 32);
        this.pos = bitPos / 32;
    }

    public static CharacterTemporaryStat getByBitPos(int bitPos) {
        return Arrays.stream(values()).filter(v -> v.getBitPos() == bitPos).findAny().orElse(null);
    }

    public boolean isEncodeInt() {
        switch (this) {
            case CarnivalDefence:
            case SpiritLink:
            case DojangLuckyBonus:
            case SoulGazeCriDamR:
            case PowerTransferGauge:
            case ReturnTeleport:
            case ShadowPartner:
            case IncMaxDamage:
            case Unk176_493:
            case SetBaseDamage:
            case QuiverCatridge:
            case ImmuneBarrier:
            case NaviFlying:
            case Dance:
            case SetBaseDamageByBuff:
            case DotHealHPPerSecond:
            case MagnetArea:
            case BlitzShield:
            case Unk199_527:
            case Unk199_528:
            case OmegaBlaster:
            case RWBarrier:
            case IndieKeyDownTime:
            case DivineEcho:
            case EnergyCharged:
            case DashSpeed:
            case DashJump:
            case RideVehicle:
            case PartyBooster:
            case GuidedBullet:
            case Undead:
            case RideVehicleExpire:
            case AranSmashSwing:
            case DebuffTolerance:
            case VampDeath:
            case Unk205_584:
                return true;
            default:
                return false;
        }
    }

    public boolean isIndie() {
        return ordinal() < IndieStatCount.ordinal();
    }

    public boolean isMovingEffectingStat() {
        switch (this) {
            case Speed:
            case Jump:
            case Stun:
            case Weakness:
            case Slow:
            case Morph:
            case Ghost:
            case BasicStatUp:
            case Attract:
            case DashSpeed:
            case DashJump:
            case Flying:
            case Frozen:
            case Frozen2:
            case Lapidification:
            case IndieSpeed:
            case IndieJump:
            case IndieSummon:
            case KeyDownMoving:
            case Mechanic:
            case Magnet:
            case MagnetArea:
            case VampDeath:
            case VampDeathSummon:
            case GiveMeHeal:
            case DarkTornado:
            case NewFlying:
            case NaviFlying:
            case UserControlMob:
            case Dance:
            case SelfWeakness:
            case BattlePvPHelenaWindSpirit:
            case BattlePvPLeeMalNyunScaleUp:
            case TouchMe:
            case IndieForceSpeed:
            case IndieForceJump:
            case RideVehicle:
            case RideVehicleExpire:
            case EtherealForm:
            case MeltDown:
            case Unk205_587:
            case Unk205_588:
                return true;
            default:
                return false;
        }
    }

    public int getVal() {
        return val;
    }

    public int getPos() {
        return pos;
    }

    public int getOrder() {
        return ORDER.indexOf(this);
    }

    public int getRemoteOrder() {
        return REMOTE_ORDER.indexOf(this);
    }

    public boolean isRemoteEncode4() {
        switch (this) {
            case NoBulletConsume:
            case RespectPImmune:
            case RespectMImmune:
            case DefenseAtt:
            case DefenseState:
            case MagicShield:
            case PyramidEffect:
            case BlessOfDarkness:
            case ImmuneBarrier:
            case Dance:
            case OmegaBlaster:
            case SpiritGuard:
            case KinesisPsychicEnergeShield:
            case AdrenalinBoost:
            case RWBarrier:
            case RWMagnumBlow:
            case Unk176_493:
            case MahasFury:
            case ManaOverload:
            case PsychicTornado:
            case Unk199_521:
            case WaterSplashEventMarking:
            case WaterSplashEventMarking2:
        //    case Mechanic:
            case DivineEcho:
            case RhoAias:
          //  case FullSoulMP:
                return true;
            default:
                return false;
        }
    }

    public boolean isRemoteEncode1() {
        switch (this) {
            case Speed:
            case ComboCounter:
           // case WeaponCharge:
            case WorldReaver:
            case Shock:
            case Team:
            case OnCapsule:
            case KillingPoint:
            case PinkbeanRollingGrade:
            case ReturnTeleport:
            case FireBomb:
            case SurplusSupply:
            case Unk176_466:
                return true;
            default:
                return false;
        }
    }

    public boolean isNotEncodeReason() {
        switch (this) {
            case Speed:
            case ComboCounter:
            //case WeaponCharge:
            case ElementalCharge:
            case WorldReaver:
            case Shock:
            case Team:
            case Ghost:
            case NoBulletConsume:
            case RespectPImmune:
            case RespectMImmune:
            case DefenseAtt:
            case DefenseState:
            case MagicShield:
            case OnCapsule:
            case PyramidEffect:
            case KillingPoint:
            case PinkbeanRollingGrade:
            case StackBuff:
            case BlessOfDarkness:
            case SurplusSupply:
            case ImmuneBarrier:
            case AdrenalinBoost:
            case RWBarrier:
            case RWMagnumBlow:
            case ManaOverload:
            case MahasFury:
            case PsychicTornado:
            case Unk199_521:
            case WaterSplashEventMarking:
            case WaterSplashEventMarking2:
            case Unk176_466:
                return true;
            default:
                return false;
        }
    }

    public boolean isNotEncodeAnything() {
        switch (this) {
            case DarkSight:
            case SoulArrow:
            case DojangInvincible:
            case Flying:
            case PinkbeanMinibeenMove:
            case BeastFormDamageUp:
            case BlessingArmor:
            case BlessingArmorIncPAD:
            case HolyMagicShell:
            case VengeanceOfAngel:
            case SwordOfLight:
            case None:
                return true;
            default:
                return isSeperatedDuplicate();
        }
    }

    public static final CharacterTemporaryStat[] isAuraCTS = new CharacterTemporaryStat[] {
            BMageAura,
            Benediction,
            KnightsAura,
            MichaelSoulLink,
    };

    @Override
    public int compare(CharacterTemporaryStat o1, CharacterTemporaryStat o2) {
        if (o1.getPos() < o2.getPos()) {
            return -1;
        } else if (o1.getPos() > o2.getPos()) {
            return 1;
        }
        // hacky way to circumvent java not having unsigned ints
        int o1Val = o1.getVal();
        if (o1Val == 0x8000_0000) {
            o1Val = Integer.MAX_VALUE;
        }
        int o2Val = o2.getVal();
        if (o2Val == 0x8000_0000) {
            o2Val = Integer.MAX_VALUE;
        }

        if (o1Val > o2Val) {
            // bigger value = earlier in the int => smaller
            return -1;
        } else if (o1Val < o2Val) {
            return 1;
        }
        return 0;
    }

    public int getBitPos() {
        return bitPos;
    }

    public static void main(String[] args) {
//        getBitPosByString("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 10 40 21 00 01 00 87 93 03 00 87 00 00 00 00 00 00 00 00 00 00 00"
//        );
        getBitPosByString("00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 20 00 6D F0 A7 09 00 00 00 00 00 00 00 00 00 00 00 00 00 B4 00 00 00 01 01 01 00 00 00 00 01");
        //changeCts();
        //getBitPos(0x2000, 12);
    }

    private static void getBitPos(int val, int pos) {
        int a = Stigma.bitPos;
        log.debug(String.format("value 0x%04x, pos %d", val, pos));
        for(CharacterTemporaryStat cts : values()) {
            if(cts.getVal() == val && cts.getPos() == pos) {
                log.debug("Corresponds to " + cts);
            }
        }
    }

    private static void getBitPosByString(String str) {
        byte[] bArr = Util.getByteArrayByString(str);
        int[] iArr = new int[bArr.length / 4];
        for (int i = 0; i < bArr.length; i += 4) {
            if (i + 3 >= bArr.length) {
                break;
            }
            iArr[i / 4] |= bArr[i];
            iArr[i / 4] |= bArr[i + 1] << 8;
            iArr[i / 4] |= bArr[i + 2] << 16;
            iArr[i / 4] |= bArr[i + 3] << 24;
        }
        for (int i = 0; i < iArr.length; i++) {
            int mask = iArr[i];
            log.debug(String.format("value 0x%04x, pos %d", i, mask));
            for (CharacterTemporaryStat cts : CharacterTemporaryStat.values()) {
                if (cts.getPos() == i && (cts.getVal() & mask) != 0) {
                    log.error(String.format("Contains stat %s", cts.toString()));
                }
            }
        }
    }

    public boolean requiresDuplicate() {
        return this == AntiMagicShell || this == Unk199_521;
    }

    public boolean isSeperatedDuplicate() {
        switch (this) {
            case AntiMagicShellEx:
            case Unk199_521Ex:
                return true;
        }
        return false;
    }

    public CharacterTemporaryStat getDuplicateCts() {
        switch (this) {
            case AntiMagicShell:
                return AntiMagicShellEx;
            case Unk199_521:
                return Unk199_521Ex;
        }
        return null;
    }

    public CharacterTemporaryStat getOriginalFromDuplicate() {
        switch (this) {
            case AntiMagicShellEx:
                return AntiMagicShell;
            case Unk199_521Ex:
                return Unk199_521;
        }
        return null;
    }


    private static void changeCts() {
        File file = new File("D:\\Maplestory\\MaplestoryServer\\myserver\\TMS248\\src\\main\\java\\net\\swordie\\ms\\client\\character\\skills\\temp\\CharacterTemporaryStat.java");
        int change = 11;
        CharacterTemporaryStat checkOp = null;
        try(Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains(",") && line.contains("(")) {
                    String[] split = line.split("[()]");
                    String name = split[0];
                    if (!Util.isNumber(split[1])) {
                        System.out.println(line);
                        continue;
                    }
                    int val = Integer.parseInt(split[1]);
                    CharacterTemporaryStat ih = Arrays.stream(CharacterTemporaryStat.values()).filter(o -> o.toString().equals(name.trim())).findFirst().orElse(null);
                    if (ih != null) {
                        CharacterTemporaryStat start = HayatoStance;
                        if (ih.ordinal() >= start.ordinal() && ih.ordinal() < None.ordinal()) {
                            if (line.contains("*")) {
                                checkOp = ih;
                            }
                            val += change;
                            System.out.println(String.format("%s(%d), %s", name, val, start == ih ? "// *" : ""));
                        } else {
                            System.out.println(line);
                        }
                    } else {
                        System.out.println(line);
                    }
                } else {
                    System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (checkOp != null) {
            System.err.println(String.format("Current op (%s) contains a * (= updated). Be sure to check for overlap.", checkOp));
        }
    }
}