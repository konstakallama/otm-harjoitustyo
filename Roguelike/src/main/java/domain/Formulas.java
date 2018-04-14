/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

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
        return 10 + currentLevel;
    }

    boolean attackHits(Stats atkStats, Stats defStats) {
        return r.nextDouble() < this.hitProb(atkStats, defStats);
    }

    double hitProb(Stats atkStats, Stats defStats) {
        double hit = (atkStats.getWeapon().getHit() + (0.05 * (atkStats.getDex() - defStats.getDex())));

        if (hit >= 1.0) {
            return 1.0;
        } else if (hit <= 0) {
            return 0.0;
        } else {
            return hit;
        }
    }

    boolean damageCalculation(Stats atkStats, Stats defStats) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    AttackResult playerDamageCalculation(Player player, Enemy enemy) {
        EnemyStats enemyStats = enemy.getStats();
        PlayerStats playerStats = player.getStats();
        AttackResult result;

        int dmg = this.getDamage(playerStats, enemyStats);

        enemy.getStats().takeDamage(dmg);

        if (enemyStats.isDead()) {
            if (playerStats.gainExp(enemyStats.getExp())) {
                result = new AttackResult(AttackResultType.KILL, dmg, player, enemy, true, (this.hitProb(playerStats, enemyStats)), enemyStats.getExp());
            } else {
                result = new AttackResult(AttackResultType.KILL, dmg, player, enemy, false, (this.hitProb(playerStats, enemyStats)), enemyStats.getExp());
            }
            enemy.die();
        } else {
            result = new AttackResult(AttackResultType.HIT, dmg, player, enemy, (this.hitProb(playerStats, enemyStats)));
        }

        return result;

    }

    AttackResult enemyDamageCalculation(Enemy enemy, Player player) {
        EnemyStats enemyStats = enemy.getStats();
        PlayerStats playerStats = player.getStats();
        AttackResult result;

        int dmg = this.getDamage(enemyStats, playerStats);

        playerStats.takeDamage(dmg);

        if (playerStats.isDead()) {
            result = new AttackResult(AttackResultType.KILL, dmg, enemy, player, (this.hitProb(enemyStats, playerStats)));

        } else {
            result = new AttackResult(AttackResultType.HIT, dmg, enemy, player, (this.hitProb(enemyStats, playerStats)));
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

        while (map.isOccupied(x, y) || map.getTerrain(x, y) == Terrain.CORRIDOR) {
            x = r.nextInt(50);
            y = r.nextInt(50);
        }
        return new Location(x, y);
    }

    public int getDamage(Stats atkStats, Stats defStats) {
        return Math.max(0, atkStats.getStr() + atkStats.getWeapon().getAtk() - defStats.getArmor().getDef());
    }


}
