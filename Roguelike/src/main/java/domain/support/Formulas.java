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
import domain.mapobject.player.Player;
import domain.mapobject.player.PlayerStats;
import domain.mapobject.Stats;
import domain.map.Terrain;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.AttackResult;
import domain.gamemanager.CommandResult;
import domain.map.Room;
import domain.mapobject.player.Spell;
import domain.mapobject.player.SpellDb;
import java.util.*;

/**
 *
 * @author konstakallama
 */
public class Formulas {

    Random r = new Random();
    SpellDb sdb = new SpellDb();
    /**
     * Returns the maximum hp for an enemy of the given type and constitution.
     * @param type
     * @param con
     * @return the maximum hp for an enemy of the given type and constitution.
     */
    public int getEnemyMaxHP(EnemyType type, int con) {
        return 7 + (3 * con);
    }
    /**
     * Returns the maximum hp for a player with the given constitution.
     * @param con
     * @return 
     */
    public int getPlayerMaxHP(int con) {
        return 7 + con * this.getHpPerCon();

    }
    /**
     * Returns the full exp required for a player of the given level to reach the next level.
     * @param currentLevel
     * @return the full exp required for a player of the given level to reach the next level.
     */
    public int expToNextLevel(int currentLevel) {
        return 10 + currentLevel;
    }
    /**
     * Returns true if a regular attack from the owner of atkStats hits the owner of defStats. This is random with the chance of formulas.hitProb() to be true.
     * @param atkStats attacker's stats
     * @param defStats attack target's stats
     * @return true if a regular attack from the owner of atkStats hits the owner of defStats.
     */
    public boolean attackHits(Stats atkStats, Stats defStats) {
        return r.nextDouble() < this.hitProb(atkStats, defStats);
    }
    /**
     * Returns the probability of a regular attack from the owner of atkStats hitting the owner of defStats.
     * @param atkStats attacker's stats
     * @param defStats attack target's stats
     * @return the probability of a regular attack from the owner of atkStats hitting the owner of defStats.
     */
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

//    public boolean damageCalculation(Stats atkStats, Stats defStats) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
    /**
     * Does the damage calculations for a regular attack from the player against the given Enemy, assuming the attack hits. If the enemy dies, it will be removed from the map and the player will gain exp. Returns an AttackResult detailing the result of the attack.
     * @param player
     * @param enemy
     * @return an AttackResult detailing the result of the attack.
     */
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
    /**
     * Does the damage calculations for a regular attack from a given Enemy against the player, assuming the attack hits. If the player dies, this will have to be dealt with after calling this method. Returns an AttackResult detailing the result of the attack.
     * @param enemy
     * @param player
     * @return an AttackResult detailing the result of the attack.
     */
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
    /**
     * Returns the rate at which enemies spawn on a given floor.
     * @param floor
     * @return the rate at which enemies spawn on a given floor.
     */
    public int getEnemySpawnInterval(int floor) {
        return 20 - Math.min(floor, 10);
    }
    /**
     * Returns a random starting location for the player on the given map using formulas.createRandomFreeLocation().
     * @param map
     * @return a random starting location for the player on the given map.
     */
    public Location createPlayerStartLocation(Map map) {
        return this.createRandomFreeLocation(map);
    }
    /**
     * Returns a random location on the map which is inside a room, not on top of an enemy or the player, not on stairs, and not on an item.
     * @param map
     * @return a random free location on the map.
     */
    public Location createRandomFreeLocation(Map map) {
        int x = r.nextInt(50);
        int y = r.nextInt(50);

        while (map.isOccupied(x, y) || map.getTerrain(x, y) == Terrain.CORRIDOR || map.getTerrain(x, y) == Terrain.STAIRS || map.getItem(x, y) != null) {
            x = r.nextInt(50);
            y = r.nextInt(50);
        }
        return new Location(x, y);
    }
    /**
     * Returns a random free location on the map which is not in the same room the player is in.
     * @param map
     * @return a random free location on the map which is not in the same room the player is in.
     */
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
    /**
     * Returns the amount of damage a regular attack from the owner of atkStats would do to the owner of defStats, should it hit.
     * @param atkStats
     * @param defStats
     * @return the amount of damage a regular attack from the owner of atkStats would do to the owner of defStats.
     */
    public int getRegularAttackDamage(Stats atkStats, Stats defStats) {
        return Math.max(0, atkStats.getStr() + atkStats.getWeapon().getAtk() - defStats.getArmor().getDef());
    }
    /**
     * Returns the maximum amount of stamina for a player with the given amount of constitution.
     * @param con
     * @return the maximum amount of stamina for a player with the given amount of constitution.
     */
    public int getMaxStamina(int con) {
//        return Integer.MAX_VALUE;
        return 450 + con * this.getStaminaPerCon();
    }
    /**
     * Returns the amount of stamina a single point of constitution will increase the player's maximum stamina by.
     * @return the amount of stamina a single point of constitution will increase the player's maximum stamina by.
     */
    public int getStaminaPerCon() {
        return 50;
    }
    /**
     * Returns the amount of hp a single point of constitution will increase the player's maximum hp by.
     * @return the amount of hp a single point of constitution will increase the player's maximum hp by.
     */
    public int getHpPerCon() {
        return 3;
    }
    /**
     * Returns the amount of spellbook slots a player with the given intelligence will have.
     * @param i
     * @return the amount of spellbook slots a player with the given intelligence will have.
     */
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
    /**
     * Does the damage calculations for a magic attack of the specified power from the player against the given Enemy, assuming the attack hits. If the enemy dies, it will be removed from the map and the player will gain exp. Returns an AttackResult detailing the result of the attack.
     * @param playerStats the stats of the player casting the spell
     * @param e the target enemy
     * @param power the power of the spell
     * @param spellName the name of the spell
     * @return an AttackResult detailing the result of the attack.
     */
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
    /**
     * Returns true if a spell with the given name cast by the given player hits the given enemy. This is random with a chance of formulas.getSpellToHit to be true.
     * @param casterStats
     * @param stats
     * @param spellName
     * @return true if a spell with the given name cast by the given player hits the given enemy.
     */
    public boolean spellHits(PlayerStats casterStats, EnemyStats stats, String spellName) {
        return (r.nextDouble() < this.getSpellToHit(casterStats, stats, spellName));
    }
    /**
     * Returns the probability that a spell with the given name cast by the given player hits the given enemy.
     * @param casterStats
     * @param stats
     * @param spellName
     * @return the probability that a spell with the given name cast by the given player hits the given enemy.
     */
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
