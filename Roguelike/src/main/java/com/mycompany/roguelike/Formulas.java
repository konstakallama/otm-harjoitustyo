/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;
import java.util.*;

/**
 *
 * @author konstakallama
 */
public class Formulas {
    Random r = new Random();

    public int getEnemyMaxHP(EnemyType type, int con) {
        return 10;
    }

    public int getPlayerMaxHP(int con) {
        return 10;

    }

    int expToNextLevel(int currentLevel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean attackHits(Stats atkStats, Stats defStats) {
        return r.nextDouble() < 0.75;
    }

    boolean damageCalculation(Stats atkStats, Stats defStats) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    AttackResult playerDamageCalculation(Player player, Enemy enemy) {
        EnemyStats enemyStats = enemy.getStats();
        PlayerStats playerStats = player.getStats();
        AttackResult result;
        
        enemy.getStats().takeDamage(5);

        if (enemyStats.isDead()) {           
//            playerStats.gainExp(enemyStats.getExp());
            result = new AttackResult(AttackResultType.KILL, 5, player, enemy);
            enemy.die();
        } else {
            result = new AttackResult(AttackResultType.HIT, 5, player, enemy);
        }
        
        return result;
        
        
    }

    AttackResult enemyDamageCalculation(Enemy enemy, Player player) {
        EnemyStats enemyStats = enemy.getStats();
        PlayerStats playerStats = player.getStats();
        AttackResult result;
        
        playerStats.takeDamage(1);
        
        if (playerStats.isDead()) {
            result = new AttackResult(AttackResultType.KILL, 1, enemy, player);
            
        } else {
            result = new AttackResult(AttackResultType.HIT, 1, enemy, player);
        }
        return result;
    }

    int getEnemySpawnInterval(int floor) {
        return 15;
    }

    Location createPlayerStartLocation(Map map) {
        return this.createRandomFreeLocation(map);
    }
    
    Location createRandomFreeLocation(Map map) {
        int x = r.nextInt(50);
        int y = r.nextInt(50);

        while (map.isOccupied(x, y)) {
            x = r.nextInt(50);
            y = r.nextInt(50);
        }
        return new Location(x, y);
    }
}
