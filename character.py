import random
 
class character:   
# Function: Class Assignment for Player
# Made for deciding which class the Player will start with and they can choose from
# Warrior/Mage/Rogue
    def class_assn(self, characterClass):
 
        self.playerClass = characterClass
        self.critical = False
 
        # WARRIOR
        # Stat values were added with the class' archetypes in mind,
        # such as Warrior having higher STR base values and modifiers
        if (self.playerClass == 1):
            self.HP = 20
            self.Str = 7
            self.Def = 5
            self.Mag = 4
            self.Res = 2
            self.Sta = 10
            self.StrMOD = 3
            self.DefMOD = 2
            self.MagMOD = 1
            self.ResMOD = 1
            self.StaMOD = 5
            self.critChance = 5
            self.assnClass = "Warrior"
 
        # MAGE
        # Mage has naturally higher Mag values and a higher resistance
        # to Mag damage due to high res values
        if (self.playerClass == 2):
            self.HP = 15
            self.Str = 4
            self.Def = 2
            self.Mag = 10
            self.Res = 7
            self.Sta = 6
            self.StrMOD = 2
            self.DefMOD = 1
            self.MagMOD = 5
            self.ResMOD = 3
            self.StaMOD = 10
            self.critChance = 3
            self.assnClass = "Mage"
            
        # ROGUE
        # Rogue has somewhat of a middle ground with lacking base stats
        # but high modifiers for certain stats. The key distinction being 
        # the high Stamina value and modifier
        if (self.playerClass == 3):
            self.HP = 17
            self.Str = 6
            self.Def = 3
            self.Mag = 6
            self.Res = 3
            self.Sta = 10
            self.StrMOD = 5
            self.DefMOD = 2
            self.MagMOD = 5
            self.ResMOD = 2
            self.StaMOD = 15
            self.critChance = 2
            self.assnClass = "Rogue"
# Function: Used for attacking and calculating damage done
# This is done through factoring in the Player's STR value and RNG
# while also taking CritChance into account
    def attack(self):
        self.StrTEMP = self.Str + random.randint(1, self.StrMOD)
        self.critHandler = random.randint(1, self.critChance)
        if (self.critHandler == 1):
            self.critical = True
            self.StrTEMP = self.StrTEMP * 2
        else: 
            self.critical = False
        self.Sta -= 2
        return self.StrTEMP
# Function: Used for defending and calculating damage reduced
# This is done through factoring in the Player's DEF value and RNG
# while also taking CritChance into account
    def defend(self):
        self.DefTEMP = self.Def + random.randint(1, self.DefMOD)
        self.critHandler = random.randint(1, self.critChance)
        if (self.critHandler == 1):
            self.critical = True
            self.DefTEMP = self.DefTEMP * 2
        else: 
            self.critical = False
        self.Sta -= 1
        return self.DefTEMP
# Function: Used to deal magic damage and calculate outgoing damage
# Calculated based on MAG stat with RNG and crit chance
    def magic(self):
        self.MagTEMP = self.Mag + random.randint(1, self.MagMOD)
        self.critHandler = random.randint(1, self.critChance)
        if (self.critHandler == 1):
            self.critical = True
            self.MagTEMP = self.MagTEMP * 2
        else: 
            self.critical = False
        self.Sta -= 2
        return self.MagTEMP
# Function: Used to guard against Magic-type damage and calculate reduced damage
# Where "defend" is used for physical damage, "resist" is used for magical damage
    def resist(self):
        self.ResTEMP = self.Res + random.randint(1, self.ResMOD)
        self.critHandler = random.randint(1, self.critChance)
        if (self.critHandler == 1):
            self.critical = True
            self.ResTEMP = self.ResTEMP * 2
        else: 
            self.critical = False
        self.Sta -= 2
        return self.ResTEMP
# Function: The previous actions used STA or stamina to perform actions
# This action regenerates and allows the player to gain stamina back
# Much like the previous actions, it can also crit, based on the chance and multiplier
    def meditate(self):
        self.StaTEMP = random.randint(1, self.StaMOD)
        self.critHandler = random.randint(1, self.critChance)
        if (self.critHandler == 1):
            self.critical = True
            self.StaTEMP = self.StaTEMP * 2
        else:
            self.critical = False
        self.Sta = self.Sta + self.StaTEMP
        return
# Function: In terms of gameplay, this is used to reset the stat bonuses
# that the player might receive from items or other means
# This only affects the Str,Mag, and Sta stats and the defense related
# stats are left untouched
    def tempReset(self):
        self.StrTEMP = 0
        self.DefTEMP = self.Def
        self.MagTEMP = 0
        self.ResTEMP = self.Res
        self.StaTEMP = 0
        return

