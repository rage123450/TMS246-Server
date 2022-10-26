# Random Damage Skin Box (2435163)

import random

items = [ # TODO: Test the items below
    2431965,  # Basic Damage Skin
    2433271,  # Basic Damage Skin
    2438159,  # Basic Damage Skin
    2438872,  # Basic Damage Skin
    2438871,  # Basic Damage Skin
    2431966,  # Digitized Damage Skin
    2432084,  # Digitized Damage Skin
    2433260,  # Digitized Damage Skin
    2434239,  # Digitized Damage Skin
    2435172,  # Digitized Damage Skin
    2438160,  # Digitized Damage Skin
    2431967,  # Kritias Damaged Skin
    2438161,  # Kritias Damage Skin
    2432131,  # Party Quest Damage Skin
    2437009,  # Party Quest Damage Skin
    2438162,  # Party Quest Damage Skin
    2432153,  # Hard-Hitting Damage Skin
    2432638,  # Hard-hitting Damage Skin
    2432659,  # Hard-hitting Damage Skin
    2433261,  # Hard-Hitting Damage Skin
    2436688,  # Hard-hitting Damage Skin
    2438163,  # Hard-hitting Damage Skin
    2432154,  # Sweet Tea Cake Damage Skin
    2432637,  # Sweet Tea Cake Damage Skin
    2432658,  # Sweet Tea Cake Damage Skin
    2433264,  # Sweet Tea Cake Damage Skin
    2435045,  # Sweet Tea Cake Damage Skin
    2436100,  # Sweet Tea Cake Damage Skin
    2438164,  # Sweet Tea Cake Damage Skin
    2438165,  # Club Henesys Damage Skin
    2432207,  # Club Henesys' damage Skin,
    2432354,  # Merry Christmas Damage Skin
    2434975,  # Merry Christmas Damage Skin
    2432355,  # Snowflake Damage Skin
    2432972,  # Snowflake Damage Skin
    2434976,  # Snowflake Damage Skin
    2435173,  # Snowflake Damage Skin
    2438167,  # Snowflake Damage Skin
    2432465,  # Alicia's Damage Skin
    2438168,  # Alicia's Damage Skin
    2432479,  # Dorothy's Damage Skin
    2438169,  # Dorothy's Damage Skin
    2432526,  # Keyboard Warrior Damage Skin
    2432639,  # Keyboard Warrior Damage Skin
    2432660,  # Keyboard Warrior Damage Skin
    2433262,  # Keyboard Warrior Damage Skin
    2435174,  # Keyboard Warrior Damage Skin
    2436038,  # Keyboard Warrior Damage Skin
    2438170,  # Keyboard Warrior Damage Skin
    2432532,  # Gentle Springtime Breeze Damage Skin
    2435102,  # Gentle Springtime Breeze Damage Skin
    2435478,  # Gentle Springtime Breeze Damage Skin
    2438171,  # Gentle Springtime Breeze Damage Skin
    2433107,  # Damage Skin - Springtime Breeze
    2432592,  # Singles Army Damage Skin
    2433263,  # Singles Army Damage Skin
    2438172,  # Singles Army Damage Skin
    2433160,  # Lonely Single Damage Skin
    2432640,  # Reminiscence Damage Skin
    2432661,  # Reminiscence Damage Skin
    2433265,  # Reminiscence Damage Skin
    2435175,  # Reminiscence Damage Skin
    2438173,  # Reminiscence Damage Skin
    2438461,  # Reminiscence Damage Skin
    2432710,  # Orange Mushroom Damage Skin
    2433266,  # Orange Mushroom Damage Skin
    2433919,  # Orange Mushroom Damage Skin
    2435170,  # Orange Mushroom Damage Skin
    2436035,  # Orange Mushroom Damage Skin
    2438174,  # Orange Mushroom Damage Skin
    2432836,  # Crown Damage Skin
    2434980,  # Crown Damage Skin
    2435176,  # Crown Damage Skin
    2438175,  # Crown Damage Skin
    2432973,  # Monotone Damage Skin
    2433158,  # Monotone Damage Skin
    2433897,  # Monotone Damage Skin
    2435177,  # Monotone Damage Skin
    2435834,  # Monotone Damage Skin
    2438176,  # Monotone Damage Skin
    2433063,  # Star Planet Damage Skin
    2435510,  # Star Planet Damage Skin
    2438177,  # Star Planet Damage Skin
    2439572,  # Star Planet Damage Skin
    2433456,  # Hangul Day Damage Skin
    2438179,  # Hangul Day Damage Skin
    2433715,  # Striped Damage Skin
    2434979,  # Striped Damage Skin
    2435024,  # Striped Damage Skin
    2435571,  # Striped Damage Skin
    2436101,  # Striped Damage Skin
    2438181,  # Striped Damage Skin
    2433804,  # Couples Army Damage Skin
    2435101,  # Couples Army Damage Skin
    2435168,  # Couples Army Damage Skin
    2438182,  # Couples Army Damage Skin
    2433913,  # Yeti and Pepe Damage Skin
    2435025,  # Yeti and Pepe Damage Skin
    2436036,  # Yeti and Pepe Damage Skin
    2438184,  # Yeti and Pepe Damage Skin
    2433980,  # Slime and Mushroom Damage Skin
    2434741,  # Slime and Mushroom Damage Skin
    2435026,  # Slime and Mushroom Damage Skin
    2437527,  # Slime and Mushroom Damage Skin
    2438185,  # Slime and Mushroom Damage Skin
    2433981,  # Pink Bean Damage Skin
    2434742,  # Pink Bean Damage Skin
    2438186,  # Pink Bean Damage Skin
    2438421,  # Pink Bean Damage Skin
    2434248,  # Rainbow Boom Damage Skin
    2435027,  # Rainbow Boom Damage Skin
    2435117,  # Rainbow Boom Damage Skin
    2435477,  # Rainbow Boom Damage Skin
    2438188,  # Rainbow Boom Damage Skin
    2433362,  # Night Sky Damage Skin
    2433666,  # Mashmellow Damage Skin
    2434274,  # Marshmallow Damage Skin
    2435029,  # Marshmallow Damage Skin
    2435490,  # Marshmallow Damage Skin
    2438190,  # Marshmallow Damage Skin
    2434289,  # Mu Lung Dojo Damage Skin
    2438191,  # Mu Lung Dojo Damage Skin
    2434390,  # Teddy Damage Skin
    2436099,  # Teddy Damage Skin
    2437712,  # Teddy Damage Skin
    2438192,  # Teddy Ursus Damage Skin
    2434391,  # Mighty Ursus Damage Skin
    2436034,  # Mighty Ursus Damage Skin
    2438193,  # Mighty Ursus Damage Skin
    2438194,  # Scorching Heat Damage Skin
    5680395,  # Scorching Heat Damage Skin
    2434528,  # USA Damage Skin
    2438195,  # USA Damage Skin
    2434529,  # Churro Damage Skin
    2438196,  # Churro Damage Skin
    2434530,  # Singapore Night Damage Skin
    2438197,  # Singapore Night Damage Skin
    2438198,  # Scribbler Damage Skin
    2436531,  # Scribbler Damage Skin
    2434546,  # Scribbler Damage Skin
    2433571,  # Scribble Crush Damage Skin
    2434374,  # Autumn Festival Full Moon Damage Skin
    2434574,  # Full Moon Damage Skin
    2438199,  # Full Moon Damage Skin
    2434575,  # Sunny Damage Skin
    2435171,  # Sunny Damage Skin
    2435727,  # Sunny Damage Skin
    2438200,  # Sunny Damage Skin
    2432804,  # Princess No Damage Skin
    2434654,  # Murgoth Damage Skin
    2435325,  # Murgoth Damage Skin
    2437727,  # Murgoth Damage Skin
    2438202,  # Murgoth Damage Skin
    2435326,  # Nine-Tailed Fox Damage Skin
    2432749,  # Zombie Damage Skin,
    2434710,  # MVP Special Damage Skin
    2438205,  # MVP Special Damage Skin
    2434824,  # Monster Park Damage Skin
    2438207,  # Monster Park Damage Skin
    2436041,  # Heroes Phantom Damage Skin
    2435665,  # Heroes Phantom Damage Skin
    2435043,  # Heroes Phantom Damage Skin
    2438211,  # Heroes Phantom Damage Skin
    2436042,  # Heroes Mercedes Damage Skin
    2435666,  # Heroes Mercedes Damage Skin
    2435044,  # Heroes Mercedes Damage Skin
    2438212,  # Heroes Mercedes Damage Skin
    2435046,  # Fireworks Damage Skin
    2436097,  # Fireworks Damage Skin
    2437710,  # Fireworks Damage Skin
    2434499,  # Autumn Festival Fireworks Damage Skin
    2435047,  # Heart Balloon Damage Skin
    2435140,  # Neon Sign Damage Skin
    2435836,  # Neon Sign Damage Skin
    2438215,  # Neon Sign Damage Skin
    2435141,  # Freeze Tag Damage Skin
    2437244,  # Freeze Tag Damage Skin
    2438216,  # Freeze Tag Damage Skin
    2435179,  # Candy Damage Skin
    2436096,  # Candy Damage Skin
    2438217,  # Candy Damage Skin
    2435162,  # Antique Gold Damage Skin
    2435157,  # Calligraphy Damage Skin
    2436098,  # Calligraphy Damage Skin
    2438219,  # Calligraphy Damage Skin
    2435158,  # Explosion Damage Skin
    2435835,  # Explosion Damage Skin
    2438220,  # Explosion Damage Skin
    2435159,  # Snow-wing Damage Skin
    2436687,  # Snow-wing Damage Skin
    2438221,  # Snow-wing Damage Skin
    2435160,  # Miho Damage Skin
    2436044,  # Miho Damage Skin
    2438222,  # Miho Damage Skin
    2435182,  # Musical Score Damage Skin
    2435725,  # Musical Score Damage Skin
    2438224,  # Musical Score Damage Skin
    2433709,  # Moon Bunny Damage Skin
    2434081,  # Moon Bunny Damage Skin
    2435166,  # Moon Bunny Damage Skin
    2435850,  # Moon Bunny Damage Skin
    2438225,  # Moon Bunny Damage Skin
    2435184,  # Forest of Tenacity Damage Skin
    2438226,  # Forest of Tenacity Damage Skin
    2435222,  # Festival Tortoise Damage Skin
    2436530,  # Festival Tortoise Damage Skin
    2438227,  # Festival Tortoise Damage Skin
    2438228,  # April Fools' Damage Skin
    2435293,  # April Fools' Damage Skin
    2438229,  # Blackheart Day Damage Skin
    2435313,  # Blackheart Day Damage Skin
    2438233,  # Sparkling April Fools' Damage Skin
    2435473,  # Bubble April Fools' Damage Skin
    2435331,  # Bubble April Fools' Damage Skin
    2438231,  # Retro April Fools' Damage Skin
    2435332,  # Retro April Fools' Damage Skin
    2438232,  # Monochrome April Fools' Damage Skin
    2435849,  # Monochrome April Fools' Damage Skin
    2435333,  # Monochrome April Fools' Damage Skin
    2435334,  # Sparkling April Fools' Damage Skin
    2435474,  # Sparkling April Fools' Damage Skin
    2435316,  # Haste Damage Skin
    2438234,  # Haste Damage Skin
    2438235,  # 12th Anniversary Maple Leaf Damage Skin
    2435408,  # 12th Anniversary Maple Leaf Damage Skin
    2438759,  # 12th Anniversary Damage Skin
    2435427,  # Cyber Damage Skin
    2438236,  # Cyber Damage Skin
    2435428,  # Cosmic Damage Skin
    2435839,  # Cosmic Damage Skin
    2438237,  # Cosmic Damage Skin
    2438238,  # Choco Donut Damage Skin
    2435429,  # Choco Donut Damage Skin
    2438240,  # Monster Balloon Damage Skin
    2435456,  # Lovely Damage Skin,
    2435461,  # Balloon Damage Skin
    2435493,  # Monster Balloon Damage Skin
    2438241,  # Bubble April Fools' Damage Skin
    2438242,  # Sparkling April Fools' Damage Skin
    2435424,  # Henesys Damage Skin
    2438243,  # Henesys Damage Skin
    2435959,  # Henesys Damage Skin
    2435958,  # Leafre Damage Skin
    2435425,  # Leafre Damage Skin
    2438244,  # Leafre Damage Skin
    2435431,  # Algebraic Damage Skin
    2438245,  # Algebraic Damage Skin
    2435430,  # Blue Flame Damage Skin
    2438246,  # Blue Flame Damage Skin
    2438761,  # Blue Flame Damage Skin
    2435432,  # Purple Damage Skin
    2438247,  # Purple Damage Skin
    2435433,  # Nanopixel Damage Skin
    2438248,  # Nanopixel Damage Skin
    2435521,  # Crystal Damage Skin
    2435523,  # Chocolate Damage Skin
    2435524,  # Spark Damage Skin
    2436561,  # Spark Damage Skin
    2438249,  # Royal Damage Skin
    2435538,  # Royal Damage Skin
    2435832,  # Chrome Damage Skin (Ver. 1)
    2438250,  # Chrome Damage Skin (Ver. 1)
    2435833,  # Neon Lights Damage Skin
    2436360,  # Neon Lights Damage Skin
    2438251,  # Neon Lights Damage Skin
    2438252,  # Spades Damage Skin
    2435840,  # Gilded Damage Skin
    2438253,  # Gilded Damage Skin
    2435841,  # Batty Damage Skin
    2438254,  # Batty Damage Skin
    2438255,  # Monochrome April Fools' Damage Skin
    2435972,  # Vanishing Journey Damage Skin
    2438256,  # Vanishing Journey Damage Skin
    2436023,  # Chu Chu Damage Skin
    2438257,  # Chu Chu Damage Skin
    2436024,  # Lachelein Damage Skin
    2438258,  # Lachelein Damage Skin
    2436026,  # Poison Flame Damage Skin
    2438259,  # Poison Flame Damage Skin
    2436027,  # Blue Shock Damage Skin
    2438260,  # Blue Shock Damage Skin
    2436028,  # Music Power Damage Skin
    2438261,  # Music Power Damage Skin
    2436029,  # Collage Power Damage Skin
    2436045,  # Starlight Aurora Damage Skin
    2438263,  # Starlight Aurora Damage Skin
    2436085,  # Chestnut Damage Skin
    2437709,  # Chestnut Damage Skin
    2438264,  # Chestnut Damage Skin
    2438265,  # Twilight Damage Skin
    2436083,  # Twilight Damage Skin
    2437707,  # Twilight Damage Skin
    2438266,  # Unyielding Fury Damage Skin
    2436084,  # Unyielding Fury Damage Skin
    2437708,  # Unyielding Fury Damage Skin
    2436103,  # Gilded Moonlight Damage Skin
    2438158,  # Gilded Moonlight Damage Skin
    2438267,  # Gilded Moonlight Damage Skin
    2438268,  # Hangul Day Traditional Damage Skin
    2436131,  # Hangul Day Traditional Damage Skin
    2438269,  # Gingko Leaf Damage Skin
    2436140,  # Gingko Leaf Damage Skin
    2438270,  # Detective Damage Skin
    2436206,  # Detective Damage Skin
    2436182,  # Silly Ghost Damage Skin
    2436212,  # Hallowkitty Damage Skin
    2437851,  # Hallowkitty Damage Skin
    2438272,  # Pew Pew Damage Skin
    2436268,  # Steamed Bun Damage Skin
    2436258,  # Relic Damage Skin
    2438274,  # Relic Damage Skin
    2438275,  # Hieroglyph Damage Skin
    2436259,  # Hieroglyph Damage Skin
    2438276,  # Breakthrough Damage Skin
    2436400,  # Breakthrough Damage Skin
    2438283,  # Mecha Damage Skin
    2436560,  # Mecha Damage Skin
    2438284,  # Foamy Friends Damage Skin
    2436578,  # Foamy Friends Damage Skin
    2437767,  # Foamy Friends Damage Skin
    2438285,  # Magpie's Feather Damage Skin
    2436611,  # Magpie's Feather Damage Skin
    2438286,  # Persimmon Tree Damage Skin
    2436596,  # Crystalline Damage Skin
    2436679,  # Arcana Damage Skin
    2438287,  # Arcana Damage Skin
    2438288,  # Imperial Damage Skin
    2436680,  # Imperial Damage Skin
    2438289,  # Fafnir Damage Skin
    2436681,  # Fafnir Damage Skin
    2436682,  # AbsoLab Damage Skin
    2438290,  # AbsoLab Damage Skin
    2438293,  # Honeybee Damage Skin
    2436785,  # Honeybee Damage Skin
    2438294,  # Evolution Damage Skin
    2436810,  # Evolution Damage Skin
    2436951,  # Constellation Damage Skin
    2438295,  # Constellation Damage Skin
    2437768,  # Constellation Damage Skin
    2438296,  # Extraterrestrial Damage Skin
    2436952,  # Extraterrestrial Damage Skin
    2438297,  # Frozen Treat Damage Skin
    2436953,  # Frozen Treat Damage Skin
    2438298,  # Solar Eclipse Damage Skin
    2437022,  # Solar Eclipse Damage Skin
    2437769,  # Solar Eclipse Damage Skin
    2438299,  # Prism Damage Skin
    2437023,  # Prism Damage Skin
    2437024,  # Starry Sky Damage Skin
    2438300,  # Starry Sky Damage Skin
    2438301,  # Party Quest Damage Skin
    2438302,  # Cadena Damage Skin
    2437164,  # Cadena Damage Skin
    2438032,  # Cadena Damage Skin
    2438925,  # Cadena Damage Skin
    2438303,  # Black Rose Damage Skin
    2437238,  # Black Rose Damage Skin
    2438306,  # Stormcloud Damage Skin
    2437495,  # Stormcloud Damage Skin
    2438307,  # Drizzly Damage Skin
    2437496,  # Rainfall Damage Skin
    2438308,  # Luxe Damage Skin
    2438309,  # Palm Frond Damage Skin
    2437515,  # Palm Frond Damage Skin
    2438310,  # Illium Damage Skin
    2437482,  # Illium Damage Skin
    2438926,  # Illium Damage Skin
    2438311,  # Lightning Damage Skin
    2437691,  # Lightning Damage Skin
    2438604,  # Lightning Damage Skin
    2438312,  # MapleStory Damage Skin
    2437716,  # MapleStory Damage Skin (KR)
    2438313,  # Rice Cake Damage Skin
    2437735,  # Rice Cake Damage Skin
    2438314,  # Popcorn Damage Skin
    2437736,  # Popcorn Damage Skin
    2438315,  # Pew Pew Damage Skin (Ver. 2)
    2437854,  # Pew Pew Damage Skin (Ver. 2)
    2437877,  # Master Crimson Damage Skin
    2438143,  # Transcendent of Time Damage Skin
    2438144,  # Superstar Damage Skin
    2438352,  # Ark Damage Skin
    2438353,  # Ark Damage Skin
    2438924,  # Ark Damage Skin
    2438378,  # Woof Woof Damage Skin
    2438379,  # Heartthrob Damage Skin
    2438413,  # Discovery Damage Skin
    2438414,  # Discovery Damage Skin
    2438415,  # Esfera Damage Skin
    2438416,  # Esfera Damage Skin
    2438417,  # Heavenly Damage Skin
    2438418,  # Heavenly Damage Skin
    2438419,  # Hybrid Damage Skin
    2438420,  # Hybrid Damage Skin
    2438460,  # Red Circuit Damage Skin
    2438485,  # Red Circuit Damage Skin
    2438491,  # Choco Bonbon Damage Skin
    2438492,  # Choco Bonbon Damage Skin
    2438529,  # Twelve Branches Damage Skin
    2438530,  # Twelve Branches Damage Skin
    2438637,  # Doodle Damage Skin
    2438676,  # Soccer Uniform Damage Skin
    2438713,  # Soccer Uniform Damage Skin
    2438881,  # Monster Damage Skin
    2438880,  # Monster Damage Skin
    2438884,  # 14th Street Damage Skin
    2438885,  # 14th Street Damage Skin
    2439256,  # Shinsoo Damage Skin
    2439298,  # Shinsoo Damage Skin
    2439264,  # Foggy Damage Skin
    2439265,  # Foggy Damage Skin
    2439336,  # Foggy Damage Skin
    2439337,  # Foggy Damage Skin
    2439277,  # Alliance Damage Skin
    2439338,  # Alliance Damage Skin
    2439381,  # Master Stellar Damage Skin
    2439392,  # Labyrinth Flame Damage Skin
    2439393,  # Labyrinth Flame Damage Skin
    2439394,  # Labyrinth Flame Damage Skin
    2439395,  # Labyrinth Flame Damage Skin
    2439407,  # Living Chain Damage Skin
    2439408,  # Living Chain Damage Skin
    2439616,  # Challenge Damage Skin, Season 1
    2439617,  # Challenge Damage Skin, Season 1
    2439652,  # Harvest Damage Skin
    2439665,  # Harvest Damage Skin
    2439683,  # Hangul Day Damage Skin (KR)
    2439684,  # Hangul Day Damage Skin (KR)
    2439685,  # Hangul Day Traditional Damage Skin (KR)
    2439686,  # Hangul Day Traditional Damage Skin (KR)
    2439769,  # Hallowkitty Damage Skin V2
    2439768,  # Hallowkitty Damage Skin V2
    2439925,  # Detective Yettson and Peplock Damage Skin
    2439926,  # Detective Yettson and Peplock Damage Skin
    2439927,  # Pew Pew Damage Skin (Ver. 3)
    2439928,  # Pew Pew Damage Skin (Ver. 3)
    2432591,  # Cherry Blossoms Damage Skin
    2436133,  # Chick Damage Skin
    2436645,  # Intense Damage Skin
    2436132,  # Illumination Damage Skin
    2436644,  # Color Pop Damage Skin (30 Day)
    2436134,  # Item has no name
    2436646,  # Intense Damage Skin (30 Day)
    2436643,  # Color Pop Damage Skin
    2436653,  # Reverse Damage Skin
    2436652,  # Ink Damage Skin (30 Day)
    2436655,  # Neon Easter Egg Damage Skin
    2437167,  # Note Damage Skin (30 Day)
    2436654,  # Reverse Damage Skin (30 Day)
    2437166,  # Note Damage Skin
    2434601,  # Invisible Damage Skin
    2436136,  # 'Magical' Bottle for Souls
    2436651,  # Ink Damage Skin
    2433588,  # Chinese Spring Fireworks Damage Skin
    2436657,  # Watercolor Damage Skin
    2437169,  # Crayon Damage Skin (30 Day)
    2436656,  # Neon Easter Egg Damage Skin (30 Day)
    2437168,  # Crayon Damage Skin
    2436658,  # Watercolor Damage Skin (30 Day)
    2433081,  # Halloween Damage Skin
    2434619,  # Nine-Tailed Fox Damage Skin
    2438661,  # Mustache Damage Skin
    2630153,  # Coral Reef Damage Skin
    3801113,  # Item has no name
    2630156,  # Shimmerlight Damage Skin
    2438659,  # Stamp Damage Skin
    2438671,  # High Noon Damage Skin
    2433038,  # 분필 �?�미지스킨
    2434570,  # Tot's Damage Skin
    2439700,  # Black Damage Skin
    2439697,  # 3D Effect Damage Skin
    2434663,  # Donut Damage Skin
    2434662,  # Jelly Bean Damage Skin
    2434147,  # Irena's Band Damage Skin
    2434157,  # Damien's Band Damage Skin
    2434664,  # Soft Serve Damage Skin
    2436721,  # Sheep Damage Skin
    2439805,  # Golden Tinsel Damage Skin
    2435196,  # Crow Damage Skin
    2435193,  # Krakian Damage Skin
    2439800,  # Jolly Holiday Damage Skin
    2435195,  # Corrupted Magician Damage Skin
    2435194,  # Crimson Knight Damage Skin
    2437701,  # Kaleidoscope Damage Skin (30 Day)
    2437700,  # Kaleidoscope Damage Skin
    2437703,  # Winter Night Skin (30 Day)
    2433113,  # Chinese Marshmallow Damage Skin
    2435673,  # Cygnus Water Warrior Damage Skin
    2435674,  # Resistance Water Warrior Damage Skin
    2433184,  # Wicked Witch Damage Skin
    2630304,  # Rock Spirit Damage Skin
    2433214,  # Noise Damage Skin
    2436229,  # Cozy Christmas Damage Skin (30 Day)
    2436741,  # Chinese Text Damage Skin (30 Day)
    2436228,  # Lucid Butterfly Damage Skin
    2436740,  # Japanese Kanji Character Damage Skin
    2436743,  # Chinese Text Damage Skin (30 Day)
    2436230,  # Cozy Christmas Damage Skin
    2436742,  # Chinese Text Damage Skin
    2436227,  # Lucid Butterfly Damage Skin (30 Day)
    2435213,  # Antellion Damage Skin
    2436749,  # Knife Wound Damage Skin (30 Day)
    2436748,  # Knife Wound Damage Skin
    2436745,  # Chinese Text Damage Skin (30 Day)
    2436744,  # Chinese Text Damage Skin
    2436747,  # Roman Numeral Damage Skin (30 Day)
    2436746,  # Roman Numeral Damage Skin
    2437269,  # Maple Damage Skin (30 Day)
    2437268,  # Maple Damage Skin
    2437271,  # Embroidery Damage Skin (30 Day)
    2437270,  # Embroidery Damage Skin
    2630301,  # Bunny Blossom Damage Skin
    2433183,  # Super Spooky Damage Skin
    2433182,  # Jack o' Lantern Damage Skin
    2437274,  # Dice Master Damage Skin
    2433252,  # Dragon's Fire Damage Skin
    2436832,  # aa
    2437856,  # Frigid Ice Damage Skin
    2433251,  # Violetta's Charming Damage Skin
    2433775,  # Orchid Damage Skin
    2433269,  # Golden Damage Skin
    2433268,  # Zombie Damage Skin
    2630393,  # 모멘텀 �?�미지스킨 (보존용)
    2433270,  # Jett Damage Skin
    2433777,  # Black Heaven Damage Skin
    2433776,  # Lotus Damage Skin
    2433267,  # Blood Damage Skin
    2630390,  # 빙고 �?�미지스킨 (보존용)
    2439373,  # Bitty Baby Feet Damage Skin
    2436300,  # Sweetheart Choco Damage Skin
    2438348,  # Baseball Jacket Damage Skin
    2436808,  # Aspire Industries Damage Skin
    2438347,  # Tweed Damage Skin
    2433236,  # Chalk Damage Skin
    2439894,  # Lunar New Year Damage Skin
    2439376,  # Pink Princess Damage Skin
    2436831,  # Petal Damage Skin
    2436830,  # Gifts of the Ryuul Damage Skin
    2439897,  # Valentine Damage Skin
    2435802,  # Dragon Fireworks Damage Skin
    2433829,  # White Heaven Rain Damage Skin
    2433828,  # White Heaven Sun Damage Skin
    2433831,  # White Heaven Snow Damage Skin
    2433830,  # White Heaven Rainbow Damage Skin
    2432803,  # Princess No Damage Skin (30-Days)
    2435374,  # Monkey Damage Skin
    2433833,  # White Heaven Wind Damage Skin
    2433832,  # White Heaven Lightning Damage Skin
    2434868,  # Christmas Lights Damage Skin
    2435380,  # Math Symbol Damage Skin
    2434871,  # Chess Damage Skin
    2435382,  # Secret Question Mark Damage Skin
    2434877,  # Secret Damage Skin_Special Character
    2434873,  # Secret Damage Skin_Music
    2435335,  # Candles Damage Skin
    2434817,  # Cube Damage Skin
    2434818,  # One Winter Night Damage Skin
    2435336,  # Cupcakes Damage Skin
    2438929,  # Droplet Damage Skin
    2438931,  # 14th Anniversary Damage Skin
    2438930,  # Gummy Bear Damage Skin
    2439442,  # Custom Puppy Damage Skin
    2439523,  # 8-Bit Damage Skin
    2433901,  # Beasts of Fury Damage Skin
    2435949,  # Too Spooky Damage Skin
    2433900,  # Night Sky Damage Skin
    2435948,  # Halloween Town Damage Skin
    2437484,  # Custom Kitty Damage Skin
    2433903,  # Lovely Damage Skin\r\n
    2435951,  # Item has no name
    2433902,  # Beasts of Fury Damage Skin
    2435950,  # Floofy Bichon Damage Skin
    2435957,  # Snow Monster Damage Skin
    2435956,  # War of Roses Damage Skin
    2433905,  # Heart Balloon Damage Skin
    2435953,  # Item has no name
    2437489,  # Ribbon Damage Skin (30 Day)
    2433904,  # Dried Out Damage Skin
    2435952,  # Item has no name
    2437488,  # Ribbon Damage Skin
    2433907,  # Antique Fantasy Damage Skin\r\n
    2435955,  # Wandering Soul Damage Skin
    2437491,  # Acorn Damage Skin (30 Day)
    2433906,  # Scribble Crush Damage Skin
    2435954,  # Masque's Puzzle Damage Skin
    2437490,  # Acorn Damage Skin
    2436477,  # XOXO Damage Skin (30 Day)
    2436476,  # Full of Stars Damage Skin
    2436479,  # Full of Stars Damage Skin (30 Day)
    2439551,  # Devil Font Damage Skin
    2436478,  # Full of Hearts Damage Skin (30 Day)
    2436984,  # Treasures of Eluna Damage Skin
    2436475,  # Full of Hearts Damage Skin
    2436474,  # XOXO Damage Skin
    2436986,  # Item has no name
    2438469,  # Skull Damage Skin
    2435908,  # Item has no name
    2434375,  # Bonfire Damage Skin
    2438471,  # Valentine's Day Damage Skin
    2435905,  # Cat Paw Damage Skin
    2435907,  # Item has no name
    2438467,  # Graffiti Damage Skin
    2435906,  # Cat Face Damage Skin
    2438477,  # Pastel Easter Egg Damage Skin
    2438473,  # White Chocolate Damage Skin
    2433883,  # Earth Day Damage Skin
    2435489,  # Sheriff Damage Skin
    2435488,  # Lingling Damage Skin
    3801003,  # Item has no name
    2435511,  # Remnant of the Goddess Damage Skin
    3800993,  # Item has no name
    2437052,  # Tropical Sunset Damage Skin (30 Day)
    2437049,  # Summer Sands Damage Skin
    2437051,  # Tropical Sunset Damage Skin
    2437050,  # Summer Sands Damage Skin (30 Day)
    2439554,  # Trick or Treat Damage Skin
    2437524,  # Snow Crystal Damage Skin (30 Day)
    2437521,  # Christmas Cane Damage Skin
    2437523,  # Snow Crystal Damage Skin
    2437522,  # Christmas Cane Damage Skin (30 Day)
    2435487,  # Nene Damage Skin
    2435486,  # TuTu Damage Skin
    2434533,  # Blood Damage Skin
    2434534,  # Zombie Damage Skin
    2435565,  # Heroes Aran Damage Skin
    2435567,  # Heroes Evan Damage Skin
    2435566,  # Heroes Luminous Damage Skin
    2439157,  # Abrup's Snowstorm Damage Skin
    2434545,  # Hayato Damage Skin
    2434544,  # Kanna Damage Skin
    2435568,  # Heroes Shade Damage Skin
    2439164,  # Fembris Damage Skin
    2438655,  # Golden Damage Skin
    2439167,  # Frostflail Yeti
    2436089,  # Highlighter Damage Skin
    2438085,  # Hong Bao Damage Skin
    2438596,  # Cake Icing Damage Skin
    2438087,  # Vengeful Nyen Damage Skin
    2438086,  # Nyen Damage Skin
    2438592,  # Round 'n' Round Damage Skin
    2438594,  # Garden Damage Skin
    2438089,  # Red-Orange Damage Skin
    2438088,  # Zodiac Dog Damage Skin
    2434004,  # Alishan Damage Skin
    2435543,  # Epic Lulz Damage Skin
    2435542,  # Item has no name
    2436563,  # Rocket Damage Skin
    2435549,  # Item has no name
    2435548,  # Item has no name
    2439132,  # Popsicle Damage Skin
    2435545,  # Summer Damage Skin
    2439129,  # Summer Sea Damage Skin
    2435544,  # Item has no name
    2435546,  # Blaster Damage Skin
]

question = sm.sendAskYesNo("Would you like to receive a random Damage Skin?")

randItem = random.choice(items)

if question and sm.canHold(randItem):
    sm.giveItem(randItem)
    sm.consumeItem(2435163)
else:
    sm.sendNext("Please make sure you have enough space in your inventory.")