/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject.player;

import domain.items.Weapon;
import domain.items.Armor;
import domain.mapobject.Stats;
import java.util.ArrayList;

/**
 *
 * @author konstakallama
 */
public class PlayerStats extends Stats {
    int staminaDmg;
    private ArrayList<Spell> spells;
//    private Player owner;

    public PlayerStats(int level, int str, int con, int intel, int dex, Weapon weapon, Armor armor) {
        super(level, str, con, intel, dex, weapon, armor);
        staminaDmg = 0;
        this.spells = new ArrayList<>();
        
//        this.owner = player;
    }

    @Override
    public int getMaxHP() {
        return f.getPlayerMaxHP(con);
    }
    
    /**
     * Has the player gain the specified amount of exp. If the player levels up, the method returns true and leftover exp is carried over to count towards the next level.
     * @param gain
     * @return True if the player levels up.
     */
    public boolean gainExp(int gain) {
        if (this.exp + gain >= f.expToNextLevel(this.level)) {
            this.exp = this.exp + gain - f.expToNextLevel(this.level);
            this.levelUp();
            return true;
        }
        this.exp += gain;
        return false;
    }
    /**
     * Returns the player's current hp.
     * @return the player's current hp.
     */
    @Override
    public int getCurrentHP() {
        return this.getMaxHP() - this.damage;
    }
    /**
     * Increases the player's level by one. If the new level is a multiple of 5, all stats will increase by 1.
     */
    public void levelUp() {
        this.increaseLevel();
        if (this.level % 5 == 0) {
            this.increaseCon();
            this.increaseDex();
            this.increaseInt();
            this.increaseStr();
        }

    }
    /**
     * Returns the full exp required to reach the next level using Formulas.expToNextLevel().
     * @return 
     */
    public int expToNextLevel() {
        return f.expToNextLevel(this.level);
    }
    /**
     * Equips the given weapon, removes it from the given inventory and adds the old weapon to the given inventory. Assumes that the given inventory contains the given weapon. Returns true if the equip is successful.
     * @param w Weapon to equip. Should be contained in Inventory i.
     * @param i Inventory to remove w from and add old weapon to.
     * @return true if the equip is successful.
     */
    public boolean equipWeapon(Weapon w, Inventory i) {
        if (str < w.getStrRec()) {
            return false;
        }
        Weapon oldW = this.weapon;
        this.weapon = w;
        i.removeItem(w);
        if (oldW != null) {
            i.addItem(oldW);
        }
        return true;
    }
    /**
     * Equips the given armor, removes it from the given inventory and adds the old armor to the given inventory. Assumes that the given inventory contains the given armor. Returns true if the equip is successful.
     * @param a Armor to equip. Should be contained in Inventory i.
     * @param i Inventory to remove a from and add old armor to.
     * @return true if the equip is successful.
     */
    public boolean equipArmor(Armor a, Inventory i) {
        Armor oldA = this.armor;
        this.armor = a;
        i.removeItem(a);
        if (oldA != null) {
            i.addItem(oldA);
        }
        return true;
    }
    /**
     * Returns the player's current stamina.
     * @return the player's current stamina.
     */
    public int getCurrentStamina() {
        return this.getMaxStamina() - this.staminaDmg;
    }

    public void setStaminaDmg(int stamina) {
        this.staminaDmg = stamina;
    }
    /**
     * Decreases the player's current stamina by 1. Stamina will not be lowered to lower than 0.
     */
    public void decreaseStamina() {
        this.staminaDmg++;
        if (this.staminaDmg > this.getMaxStamina()) {
            this.staminaDmg = this.getMaxStamina();
        }
    }
    /**
     * Increases the player's stamina by the given amount. Stamina will not be increased higher than the max stamina.
     * @param amount 
     */
    public void increaseStamina(int amount) {
        staminaDmg -= amount;
        if (staminaDmg < 0) {
            staminaDmg = 0;
        }
    }
    
    public int getMaxStamina() {
        return f.getMaxStamina(con);
    }
    
    @Override
    public void increaseCon() {
        con++;
//        this.increaseStamina(f.getStaminaPerCon());
//        this.heal(f.getHpPerCon());
    }

    public int getStaminaDmg() {
        return staminaDmg;
    }
    
    public int getMaxSpellbookSlots() {
        return f.getMaxSpellbookSlots(intel);
    }
    /**
     * Returns the amount of empty spellbook slots the player has.
     * @return the amount of empty spellbook slots the player has.
     */
    public int getFreeSpellBookSlots() {
        return this.getMaxSpellbookSlots() - this.spells.size();
    }
    /**
     * Adds the given spell to the player's list of spells if he has a free spellbook slot. Returns true if the spell is learned.
     * @param s Spell to learn
     * @return true if the spell is learned.
     */
    public boolean learnSpell(Spell s) {
        if (this.getFreeSpellBookSlots() > 0) {
            this.spells.add(s);
            return true;
        }
        return false;
    }
    /**
     * Removes the given Spell from the player's list of spells. Returns true if the remove is successful.
     * @param s
     * @return 
     */
    public boolean forgetSpell(Spell s) {
        return this.spells.remove(s);
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }
    /**
     * Reduces the cooldown counter for all the player's spells by 1.
     */
    public void advanceCooldowns() {
        for (Spell s : this.spells) {
            s.advanceCooldown();
        }
    }

    @Override
    public int getDex() {
        return Math.max(dex - armor.getDexPenalty(), 1);
    }

}
