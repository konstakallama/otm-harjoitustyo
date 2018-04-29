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
import domain.mapobject.Player.Player;
import domain.mapobject.Player.PlayerStats;
import domain.mapobject.Stats;
import domain.map.Terrain;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.AttackResult;
import domain.gamemanager.CommandResult;
import domain.map.Room;
import domain.mapobject.Player.Spell;
import domain.mapobject.Player.SpellDb;
import java.util.*;

/**
 *
 * @author konstakallama
 */
public class Formulas {

    Random r = new Random();
    SpellDb sdb = new SpellDb();

    public int getEnemyMaxHP(EnemyType type, int con) {
        return 10;
    }

    public int getPlayerMaxHP(int con) {
        return 7 + con * this.getHpPerCon();

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

        int dmg = this.getRegularAttackDamage(playerStats, enemyStats);

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

        int dmg = this.getRegularAttackDamage(enemyStats, playerStats);

        playerStats.takeDamage(dmg);

        if (playerStats.isDead()) {
            result = new AttackResult(AttackResultType.KILL, dmg, enemy, player, (this.hitProb(enemyStats, playerStats)));

        } else {
            result = new AttackResult(AttackResultType.HIT, dmg, enemy, player, (this.hitProb(enemyStats, playerStats)));
        }
        return result;
    }

    public int getEnemySpawnInterval(int floor) {
        return 30 - Math.min(floor, 15);
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

    public int getRegularAttackDamage(Stats atkStats, Stats defStats) {
        return Math.max(0, atkStats.getStr() + atkStats.getWeapon().getAtk() - defStats.getArmor().getDef());
    }

    public int getMaxStamina(int con) {
//        return Integer.MAX_VALUE;
        return 450 + con * this.getStaminaPerCon();
    }

    public int getStaminaPerCon() {
        return 50;
    }
    
    public int getHpPerCon() {
        return 3;
    }

    public int getMaxSpellbookSlots(int i) {
        if (i < 3) {
            return 1;
        } else if (i < 6) {
            return 2;
        } else if (i < 10) {
            return 3;
        } else if (i < 15) {
            return 4;
        } else {
            return 5;
        }
    }

    public AttackResult magicDamageCalculation(PlayerStats playerStats, Enemy e, int power, String spellName) {
        EnemyStats enemyStats = e.getStats();
        
        int dmg = getMagicDamage(playerStats, enemyStats, power);
        AttackResult result;
        
        e.getStats().takeDamage(dmg);

        if (enemyStats.isDead()) {
            if (playerStats.gainExp(enemyStats.getExp())) {
                result = new AttackResult(AttackResultType.KILL, dmg, null, e, true, (this.getSpellToHit(playerStats, enemyStats, spellName)), enemyStats.getExp());
            } else {
                result = new AttackResult(AttackResultType.KILL, dmg, null, e, false, (this.getSpellToHit(playerStats, enemyStats, spellName)), enemyStats.getExp());
            }
            e.die();
        } else {
            result = new AttackResult(AttackResultType.HIT, dmg, null, e, (this.getSpellToHit(playerStats, enemyStats, spellName)));
        }

        return result;      
    }

    public boolean spellHits(PlayerStats casterStats, EnemyStats stats, String spellName) {
        return (r.nextDouble() < this.getSpellToHit(casterStats, stats, spellName));
    }

    public double getSpellToHit(PlayerStats casterStats, EnemyStats stats, String spellName) {
        Spell s = this.sdb.spellConverter(spellName, casterStats);
        
        double hit = (s.getAccuracy() + (0.05 * (casterStats.getInt() - stats.getInt())));
        
        if (hit >= 1.0) {
            return 1.0;
        } else if (hit <= 0) {
            return 0.0;
        } else {
            return hit;
        }
    }

    private int getMagicDamage(Stats atkStats, EnemyStats defStats, int power) {
        return Math.max(0, atkStats.getInt() + power - defStats.getInt());
    }


}
