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
import dao.ItemDb;
import domain.items.Weapon;
import domain.items.WeaponType;
import domain.map.Room;
import dao.EnemyDb;
import dao.FileReader;
import domain.mapobject.player.Spell;
import dao.SpellDb;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author konstakallama
 */
public class Formulas {

    Random r = new Random();
    SpellDb sdb = new SpellDb();
    String fileName = "data/EncounterProbabilities.txt";
    String lastFloor = "20";
    ItemDb itemDb = new ItemDb();
    EnemyDb enemyDb = new EnemyDb();

    /**
     * Returns the maximum hp for an enemy of the given type and constitution.
     *
     * @param type
     * @param con
     * @return the maximum hp for an enemy of the given type and constitution.
     */
    public int getEnemyMaxHP(EnemyType type, int con) {
        return 7 + (3 * con);
    }

    /**
     * Returns the maximum hp for a player with the given constitution.
     *
     * @param con
     * @return
     */
    public int getPlayerMaxHP(int con) {
        return 7 + con * this.getHpPerCon();

    }

    /**
     * Returns the full exp required for a player of the given level to reach
     * the next level.
     *
     * @param currentLevel
     * @return the full exp required for a player of the given level to reach
     * the next level.
     */
    public int expToNextLevel(int currentLevel) {
        int lc = (int) Math.round(Math.floor(Math.log(currentLevel)));
        return 10 + (currentLevel * lc);
    }

    /**
     * Returns true if a regular attack from the owner of atkStats hits the
     * owner of defStats. This is random with the chance of formulas.hitProb()
     * to be true.
     *
     * @param atkStats attacker's stats
     * @param defStats attack target's stats
     * @return true if a regular attack from the owner of atkStats hits the
     * owner of defStats.
     */
    public boolean attackHits(Stats atkStats, Stats defStats) {
        return r.nextDouble() < this.hitProb(atkStats, defStats);
    }

    /**
     * Returns the probability of a regular attack from the owner of atkStats
     * hitting the owner of defStats.
     *
     * @param atkStats attacker's stats
     * @param defStats attack target's stats
     * @return the probability of a regular attack from the owner of atkStats
     * hitting the owner of defStats.
     */
    public double hitProb(Stats atkStats, Stats defStats) {
        if (defStats.isFrozen() || defStats.isStunned()) {
            return 1.0;
        }
        double hit = (atkStats.getWeapon().getHit() + (0.05 * (atkStats.getDex() - defStats.getDex())));

        if (weaponTriangleAdvantage(atkStats, defStats)) {
            hit += 0.15;
        } else if (weaponTriangleAdvantage(defStats, atkStats)) {
            hit -= 0.15;
        }

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
     * Does the damage calculations for a regular attack from the player against
     * the given Enemy, assuming the attack hits. If the enemy dies, it will be
     * removed from the map and the player will gain exp. Returns an
     * AttackResult detailing the result of the attack.
     *
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
     * Does the damage calculations for a regular attack from a given Enemy
     * against the player, assuming the attack hits. If the player dies, this
     * will have to be dealt with after calling this method. Returns an
     * AttackResult detailing the result of the attack.
     *
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
     *
     * @param floor
     * @return the rate at which enemies spawn on a given floor.
     */
    public int getEnemySpawnInterval(int floor) {
        return 30 - Math.min(floor, 20);
    }

    /**
     * Returns a random starting location for the player on the given map using
     * formulas.createRandomFreeLocation().
     *
     * @param map
     * @return a random starting location for the player on the given map.
     */
    public Location createPlayerStartLocation(Map map) {
        return this.createRandomFreeLocation(map);
    }

    /**
     * Returns a random location on the map which is inside a room, not on top
     * of an enemy or the player, not on stairs, and not on an item.
     *
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
     * Returns a random free location on the map which is not in the same room
     * the player is in.
     *
     * @param map
     * @return a random free location on the map which is not in the same room
     * the player is in.
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
     * Returns the amount of damage a regular attack from the owner of atkStats
     * would do to the owner of defStats, should it hit.
     *
     * @param atkStats
     * @param defStats
     * @return the amount of damage a regular attack from the owner of atkStats
     * would do to the owner of defStats.
     */
    public int getRegularAttackDamage(Stats atkStats, Stats defStats) {
        return Math.max(0, atkStats.getStr() + atkStats.getWeapon().getAtk() - defStats.getArmor().getDef());
    }

    /**
     * Returns the maximum amount of stamina for a player with the given amount
     * of constitution.
     *
     * @param con
     * @return the maximum amount of stamina for a player with the given amount
     * of constitution.
     */
    public int getMaxStamina(int con) {
//        return Integer.MAX_VALUE;
        return 450 + con * this.getStaminaPerCon();
    }

    /**
     * Returns the amount of stamina a single point of constitution will
     * increase the player's maximum stamina by.
     *
     * @return the amount of stamina a single point of constitution will
     * increase the player's maximum stamina by.
     */
    public int getStaminaPerCon() {
        return 50;
    }

    /**
     * Returns the amount of hp a single point of constitution will increase the
     * player's maximum hp by.
     *
     * @return the amount of hp a single point of constitution will increase the
     * player's maximum hp by.
     */
    public int getHpPerCon() {
        return 3;
    }

    /**
     * Returns the amount of spellbook slots a player with the given
     * intelligence will have.
     *
     * @param i
     * @return the amount of spellbook slots a player with the given
     * intelligence will have.
     */
    public int getMaxSpellbookSlots(int i) {
        if (i < 6) {
            return 1;
        } else if (i < 9) {
            return 2;
        } else if (i < 13) {
            return 3;
        } else if (i < 18) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * Does the damage calculations for a magic attack of the specified power
     * from the player against the given Enemy, assuming the attack hits. If the
     * enemy dies, it will be removed from the map and the player will gain exp.
     * Returns an AttackResult detailing the result of the attack.
     *
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
     * Returns true if a spell with the given name cast by the given player hits
     * the given enemy. This is random with a chance of formulas.getSpellToHit
     * to be true.
     *
     * @param casterStats
     * @param stats
     * @param spellName
     * @return true if a spell with the given name cast by the given player hits
     * the given enemy.
     */
    public boolean spellHits(PlayerStats casterStats, EnemyStats stats, String spellName) {
        return (r.nextDouble() < this.getSpellToHit(casterStats, stats, spellName));
    }

    /**
     * Returns the probability that a spell with the given name cast by the
     * given player hits the given enemy.
     *
     * @param casterStats
     * @param stats
     * @param spellName
     * @return the probability that a spell with the given name cast by the
     * given player hits the given enemy.
     */
    public double getSpellToHit(PlayerStats casterStats, EnemyStats stats, String spellName) {
        if (stats.isFrozen() || stats.isStunned()) {
            return 1.0;
        }

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

    public Enemy createRandomEnemy(Map map, Location l, int enemiesCreated) {
        String[] firstLine = null;
        FileReader fr = new FileReader(fileName);
        
        firstLine = fr.readLineByName("Floor");

//        try {
//            firstLine = Files.readAllLines(Paths.get(this.fileName)).get(0).split("\t");
//        } catch (IOException ex) {
//            try {
//                firstLine = Files.readAllLines(Paths.get("src/main/resources/" + this.fileName)).get(0).split("\t");
//            } catch (IOException ex1) {
//            }
//        }

        
        String[] line = fr.readLineByName(Math.min(map.getFloor(), 19) + "");
        //String[] line = this.readLineFromFileWrapper(Math.min(map.getFloor(), 19) + "");
        double random = r.nextDouble();
        double d = 0;
        EnemyType et = null;

        for (int i = 1; i < firstLine.length; i++) {
            d += Double.parseDouble(line[i]);
            if (random < d) {
                et = new EnemyType(firstLine[i]);
                break;
            }
        }
        EnemyStats es = new EnemyStats(enemiesCreated / 10, et, itemDb.createEnemyTestWeapon(enemyDb.getWeaponType(et.getName())), itemDb.createEnemyTestArmor());

        return new Enemy(l.getX(), l.getY(), map, es, true);
    }

//    private String[] readLineFromFileWrapper(String name) {
//        try {
//            return this.readLineFromFile(name, fileName);
//
//        } catch (Exception ex) {
//            try {
//                return this.readLineFromFile(name, "src/main/resources/" + fileName);
//            } catch (Exception e) {
//                
//            }
//        }
//
//        return null;
//    }

//    private String[] readLineFromFile(String name, String filename) throws Exception {
//
//        List<String> l = Files.readAllLines(Paths.get(filename));
//
//        for (String line : l) {
//            String[] s = line.split("\t");
//            if (s[0].equals(name)) {
//                return (s);
//            } else if (s[0].equals(lastFloor)) {
//                return s;
//            }
//        }
//
//        throw new Exception("Error");
//    }

    private boolean weaponTriangleAdvantage(Stats atkStats, Stats defStats) {
        WeaponType aw = atkStats.getWeapon().getType();
        WeaponType dw = defStats.getWeapon().getType();
        return (aw == WeaponType.SWORD && dw == WeaponType.AXE) || (aw == WeaponType.LANCE && dw == WeaponType.SWORD) || (aw == WeaponType.AXE && dw == WeaponType.LANCE);
    }

}
