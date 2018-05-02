/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject;

import domain.items.Weapon;
import domain.items.Armor;
import domain.support.Formulas;

/**
 *
 * @author konstakallama
 */
public abstract class Stats {
    public int level;
    public int exp;
    public int str;
    public int con;
    public int intel;
    public int dex;
    public int damage;
    public Formulas f = new Formulas();
    public Weapon weapon;
    public Armor armor;

    public Stats(int level, int str, int con, int intel, int dex, Weapon weapon, Armor armor) {
        this.level = level;
        this.exp = 0;
        this.str = str;
        this.con = con;
        this.intel = intel;
        this.dex = dex;
        this.damage = 0;
        this.weapon = weapon;
        this.armor = armor;
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        this.level++;
    }
    
    public void increaseLevelBy(int amount) {
        this.level += amount;
    }

    public int getExp() {
        return exp;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getCon() {
        return con;
    }

    public void setCon(int con) {
        this.con = con;
    }

    public int getInt() {
        return intel;
    }

    public void setInt(int intel) {
        this.intel = intel;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getDamage() {
        return damage;
    }
    /**
     * Takes the specified amount of damage. Returns true if the sufferer dies as a result.
     * @param dmg
     * @return true if the sufferer dies as a result.
     */
    public boolean takeDamage(int dmg) {
        this.damage += dmg;
        if (this.damage > this.getMaxHP()) {
            this.damage = this.getMaxHP();
        }
        return this.isDead();
    }
    /**
     * Reduces the amount of damage the owner ow the stats has taken by amount. Damage may never be negative; if the amount of damage healed is greater the damage suffered, damage will be set to 0. Returns true if some damage is healed, false if not (ie the owner of the stats is at full hp).
     * @param amount
     * @return true if some damage is healed.
     */
    public boolean heal(int amount) {
        if (this.damage <= 0) {
            return false;
        }
        this.damage -= amount;
        if (this.damage < 0) {
            this.damage = 0;
        }
        return true;
    }
    
    public abstract int getMaxHP();
    /**
     * Returns true if current hp is 0 (or lower).
     * @return true if character is dead
     */
    public boolean isDead() {
        return this.getMaxHP() - this.damage <= 0;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }
    /**
     * Increases strength by 1.
     */
    public void increaseStr() {
        str++;
    }
    /**
     * Increases constitution by 1.
     */
    public void increaseCon() {
        con++;
    }
    /**
     * Increases intelligence by 1.
     */
    public void increaseInt() {
        intel++;
    }
    /**
     * Increases dexterity by 1.
     */
    public void increaseDex() {
        dex++;
    }
    
    public abstract int getCurrentHP();
    
    
    
}
