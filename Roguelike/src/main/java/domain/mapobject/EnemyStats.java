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
    private int exp;
    

    public EnemyStats(int level, int str, int con, int intel, int dex, EnemyType type, Weapon weapon, Armor armor, int exp) {
        super(level, str, con, intel, dex, weapon, armor);
        this.type = type;
        this.exp = exp;
    }

    @Override
    public int getMaxHP() {
        return f.getEnemyMaxHP(type, con);
    }

    public EnemyType getType() {
        return type;
    }
    
    public int getExp() {
        return this.exp;
    }

    @Override
    public int getCurrentHP() {
        return this.getMaxHP() - this.damage;
    }
    
    
    
}
