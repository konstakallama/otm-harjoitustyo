/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject;

import domain.items.Weapon;
import domain.items.Armor;

/**
 *
 * @author konstakallama
 */
public class PlayerStats extends Stats {
    int staminaDmg;
//    private Player owner;

    public PlayerStats(int level, int str, int con, int intel, int dex, Weapon weapon, Armor armor) {
        super(level, str, con, intel, dex, weapon, armor);
        staminaDmg = 0;
//        this.owner = player;
    }

    @Override
    public int getMaxHP() {
        return f.getPlayerMaxHP(con);
    }

    public boolean gainExp(int gain) {
        if (this.exp + gain >= f.expToNextLevel(this.level)) {
            this.exp = this.exp + gain - f.expToNextLevel(this.level);
            this.levelUp();
            return true;
        }
        this.exp += gain;
        return false;
    }

    @Override
    public int getCurrentHP() {
        return this.getMaxHP() - this.damage;
    }

    public void levelUp() {
        this.increaseLevel();
        if (this.level % 5 == 0) {
            this.increaseCon();
            this.increaseDex();
            this.increaseInt();
            this.increaseStr();
        }

    }

    public int expToNextLevel() {
        return f.expToNextLevel(this.level);
    }

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

    public boolean equipArmor(Armor a, Inventory i) {
        Armor oldA = this.armor;
        this.armor = a;
        i.removeItem(a);
        if (oldA != null) {
            i.addItem(oldA);
        }
        return true;
    }

    public int getStamina() {
        return this.getMaxStamina() - this.staminaDmg;
    }

    public void setStaminaDmg(int stamina) {
        this.staminaDmg = stamina;
    }
    
    public void decreaseStamina() {
        this.staminaDmg++;
        if (this.staminaDmg > this.getMaxStamina()) {
            this.staminaDmg = this.getMaxStamina();
        }
    }
    
    public void increaseStamina(int amount) {
        staminaDmg -= amount;
        if (staminaDmg > f.getMaxStamina(con)) {
            staminaDmg = f.getMaxStamina(con);
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

}
