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
    
}
