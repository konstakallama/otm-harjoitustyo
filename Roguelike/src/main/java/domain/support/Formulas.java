/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.support;

import domain.mapobject.Enemy;
import domain.mapobject.EnemyStats;
import domain.mapobject.EnemyType;
import domain.map.Map;
import domain.mapobject.Player;
import domain.mapobject.PlayerStats;
import domain.mapobject.Stats;
import domain.map.Terrain;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.AttackResult;
import domain.map.Room;
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
        return 7 + con*this.getHpPerCon();

    }

    public int expToNextLevel(int currentLevel) {
        return 10 + currentLevel;
    }

    public boolean attackHits(Stats atkStats, Stats defStats) {
        return r.nextDouble() < this.hitProb(atkStats, defStats);
    }

    public double hitProb(Stats atkStats, Stats defStats) {
        double hit = (atkStats.getWeapon().getHit() + (0.05 * (atkStats.getDex() - defStats.getDex())));

        if (hit >= 1.0) {
            return 1.0;
        } else if (hit <= 0) {
            return 0.0;
        } else {
            return hit;
        }
    }

    public boolean damageCalculation(Stats atkStats, Stats defStats) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AttackResult playerDamageCalculation(Player player, Enemy enemy) {
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

    public AttackResult enemyDamageCalculation(Enemy enemy, Player player) {
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

    public int getEnemySpawnInterval(int floor) {
        return 15;
    }

    public Location createPlayerStartLocation(Map map) {
        return this.createRandomFreeLocation(map);
    }

    public Location createRandomFreeLocation(Map map) {
        int x = r.nextInt(50);
        int y = r.nextInt(50);

        while (map.isOccupied(x, y) || map.getTerrain(x, y) == Terrain.CORRIDOR || map.getTerrain(x, y) == Terrain.STAIRS) {
            x = r.nextInt(50);
            y = r.nextInt(50);
        }
        return new Location(x, y);
    }
    
    public Location createEnemySpawnLocation(Map map) {
        int x = r.nextInt(50);
        int y = r.nextInt(50);
        
        Room room = map.getPlayerRoom();

        while (map.isOccupied(x, y) || map.getTerrain(x, y) == Terrain.CORRIDOR || map.getTerrain(x, y) == Terrain.STAIRS || room.isInside(new Location(x, y))) {
            x = r.nextInt(50);
            y = r.nextInt(50);
        }
        return new Location(x, y);
    }

    public int getDamage(Stats atkStats, Stats defStats) {
        return Math.max(0, atkStats.getStr() + atkStats.getWeapon().getAtk() - defStats.getArmor().getDef());
    }

    public int getMaxStamina(int con) {
        return Integer.MAX_VALUE;
//        return 450 + con*this.getStaminaPerCon();
    }

    public int getStaminaPerCon() {
        return 50;
    }
    
    public int getHpPerCon() {
        return 3;
    }


}
