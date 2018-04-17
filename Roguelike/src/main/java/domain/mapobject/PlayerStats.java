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
//    private Player owner;

    public PlayerStats(int level, int str, int con, int intel, int dex, Weapon weapon, Armor armor) {
        super(level, str, con, intel, dex, weapon, armor);
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
        con++;
        str++;
        intel++;
        dex++;
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

}
