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
public class Formulas {

    public int getEnemyHP(EnemyType type, int con) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getPlayerMaxHP(int con) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    int expToNextLevel(int currentLevel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean attackHits(Stats atkStats, Stats defStats) {
        return true;
    }

    boolean damageCalculation(Stats atkStats, Stats defStats) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void playerDamageCalculation(Player player, Enemy enemy) {
//        EnemyStats enemyStats = enemy.getStats();
//        PlayerStats playerStats = player.getStats();
//        // Damage calculation
//        if (enemyStats.isDead()) {           
//            playerStats.gainExp(enemyStats.getExp());
//            enemy.die();
//        }
        enemy.die();
    }
}
