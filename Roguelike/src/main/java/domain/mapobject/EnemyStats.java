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
public class EnemyStats extends Stats {
    private EnemyType type;
    

    public EnemyStats(int level, EnemyType type, Weapon weapon, Armor armor) {
        super(level, 0, 0, 0, 0 , weapon, armor);
        this.type = type;
    }

    @Override
    public int getMaxHP() {
        return this.type.getMaxHP(level);
    }

    public EnemyType getType() {
        return type;
    }
    
    @Override
    public int getExp() {
        return this.type.getExp(level);
    }

    @Override
    public int getCurrentHP() {
        return this.getMaxHP() - this.damage;
    }

    @Override
    public int getDex() {
        return this.type.getDex(level);
    }

    @Override
    public int getInt() {
        return this.type.getInt(level);
    }

    @Override
    public int getCon() {
        return this.type.getCon(level);
    }

    @Override
    public int getStr() {
        return this.type.getStr(level);
    }

    @Override
    public String toString() {
        return "Stats: {" + "Str:" + this.getStr() + "; " + "Con:" + this.getCon() + "; " + "Int:" + this.getInt() + "; " + "Dex:" + this.getDex() + "; " + "HP:" + this.getCurrentHP() + "/" + this.getMaxHP() + "; " + "WeaponType: " + this.weapon.getType().toString() + '}';
    }
    
    
    
}
