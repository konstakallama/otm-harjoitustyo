/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

/**
 *
 * @author konstakallama
 */
public class EnemyStats extends Stats {
    private EnemyType type;
    

    public EnemyStats(int level, int str, int con, int intel, int dex, EnemyType type, Weapon weapon, Armor armor) {
        super(level, str, con, intel, dex, weapon, armor);
        this.type = type;
    }

    @Override
    public int getMaxHP() {
        return f.getEnemyHP(type, con);
    }

    public EnemyType getType() {
        return type;
    }
    
    
    
}