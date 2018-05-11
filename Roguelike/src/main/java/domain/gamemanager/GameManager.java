/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.gamemanager;

import domain.items.Armor;
import domain.support.Direction;
import domain.mapobject.Enemy;
import domain.mapobject.EnemyStats;
import domain.mapobject.EnemyType;
import domain.support.Formulas;
import domain.mapobject.player.Inventory;
import dao.ItemDb;
import domain.support.Location;
import domain.map.Map;
import domain.map.MapGenerator;
import domain.mapobject.player.Player;
import domain.mapobject.player.PlayerStats;
import domain.items.Weapon;
import domain.items.WeaponType;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author konstakallama
 */
public class GameManager {

    private Map map;
    private Player p;
    private GameManagerStatistics gmStats;
    private Formulas f = new Formulas();
    private MapGenerator m = new MapGenerator();
    private ItemDb itemDb = new ItemDb();

    String defaultFailMessage = "";

    int mapW = 50;
    int mapH = 50;

    public GameManager(PlayerStats stats, Inventory i) {
        this.map = m.createTestMap(mapW, mapH, 0);
        Location l = f.createPlayerStartLocation(map);

        this.p = new Player(this.map, l.getX(), l.getY(), stats, i);

        this.gmStats = new GameManagerStatistics(f.getEnemySpawnInterval(map.getFloor()));
    }
    
    public GameManager(PlayerStats stats, Inventory i, String name) {
        this.map = m.createTestMap(mapW, mapH, 0);
        Location l = f.createPlayerStartLocation(map);

        this.p = new Player(this.map, l.getX(), l.getY(), stats, i, name);

        this.gmStats = new GameManagerStatistics(f.getEnemySpawnInterval(map.getFloor()));
    }

    public GameManager() {
        this(new PlayerStats(1, 1, 1, 1, 1, new Weapon(3, 0.75, WeaponType.SWORD, "test weapon"), new Armor(1, "test armor")), new Inventory(10));
    }
    /**
     * Plays out the next round of the game with the given player command. Returns an Arraylist of CommandResults detailing the events of the round.
     * @param c The command for the player's action this round.
     * @return an Arraylist of CommandResults detailing the events of the round.
     */
    public ArrayList<CommandResult> playCommand(PlayerCommand c) {
        ArrayList<CommandResult> results = new ArrayList<>();

        if (c.getType() == PlayerCommandType.COMMAND_NOT_FOUND) {
            this.playCommandNotFound(results);
        }

        if (c.getType() == PlayerCommandType.MOVE) {
            this.playMove(results, c);
        }

        if (c.getType() == PlayerCommandType.WAIT) {
            this.playWait(results);
        }

        if (c.getType() == PlayerCommandType.NEXT_FLOOR) {
            this.nextFloor(results);
        }

        return results;
    }

    private void playCommandNotFound(ArrayList<CommandResult> results) {
        results.add(new CommandResult(false, false, this.defaultFailMessage, new AttackResult(AttackResultType.FAIL, 0, null, null)));

    }

    private void playMove(ArrayList<CommandResult> results, PlayerCommand c) {
        CommandResult cr = this.movePlayer(c.getDirection());

        if (cr.isSuccess()) {
            results.add(cr);
            this.playRound(results);
        } else {
            cr = this.playerAttack(c.getDirection());
            results.add(cr);
            if (cr.isSuccess()) {
                this.playRound(results);
            }
        }
    }

    private void playWait(ArrayList<CommandResult> results) {
        if (map.playerIsOnStairs()) {
            results.add(new CommandResult(true, true, map.nextFloorMessage(), new AttackResult(AttackResultType.FAIL, 0, p, null), map.playerIsOnStairs()));
        }
        results.add(new CommandResult(true, false, "", new AttackResult(AttackResultType.FAIL, 0, null, null)));
        this.playRound(results);
    }

    private CommandResult movePlayer(Direction d) {
        return p.move(d);
    }

    private CommandResult playerAttack(Direction d) {
        AttackResult ar = p.attack(d);

        if (ar.getType() == AttackResultType.FAIL) {
            return new CommandResult(false, false, "", ar);
        } else {
            return new CommandResult(true, true, this.parsePlayerAttackResult(ar), ar);
        }
    }

    private void playRound(ArrayList<CommandResult> results) {
        p.getStats().advanceCooldowns();
        
        p.getStats().decreaseStamina();
        if (p.getStats().getCurrentStamina() == 0) {
            p.getStats().takeDamage(1);
            if (p.getStats().isDead()) {
                results.add(new CommandResult(true, false, "", null, false, true));
                return;
            }
        }
        for (AttackResult r : map.takeTurns()) {
            if (r.getType() != AttackResultType.FAIL) {
                results.add(new CommandResult(true, true, this.parseEnemyAttackResult(r), r));
            }
        }

        this.manageRoundStats();
    }

    private String parsePlayerAttackResult(AttackResult result) {
        String r = "";
        if (result.getType() == AttackResultType.HIT) {
            r = ("You hit the " + result.getTarget().getName() + " dealing " + result.getDamageDealt() + " damage");
        }
        if (result.getType() == AttackResultType.MISS) {
            r = ("You miss the " + result.getTarget().getName());
        }
        if (result.getType() == AttackResultType.KILL) {
            this.gmStats.increaseEnemiesKilled();
            r = ("You kill the " + result.getTarget().getName() + " and gain " + result.getExpGained() + " exp");
        }
        r += this.getToHitMessage(result) + ".";

        if (result.isLevelUp()) {
            r += " You have leveled up!";
        }
        return r;
    }

    private String parseEnemyAttackResult(AttackResult result) {
        String r = "";
        if (result.getType() == AttackResultType.HIT) {
            r = ("The " + result.getAttacker().getName() + " attacks you and deals " + result.getDamageDealt() + " damage");
        }
        if (result.getType() == AttackResultType.MISS) {
            r = ("The " + result.getAttacker().getName() + " misses you");
        }
        if (result.getType() == AttackResultType.KILL) {
            r = ("The " + result.getAttacker().getName() + " attacks and kills you");
        }
        r += this.getToHitMessage(result) + ".";
        return r;
    }

    private void manageRoundStats() {
        this.gmStats.increaseTurns();
        if (this.gmStats.increaseEnemySpawnCounter()) {
            this.addEnemy();
        }
    }

    public void addEnemy() {
        Location l = f.createEnemySpawnLocation(map);
        
        Enemy e = f.createRandomEnemy(map, l, this.gmStats.enemiesCreated);

        map.addEnemy(l.getX(), l.getY(), e);
    }

    private Enemy createEnemy(int x, int y) {
        return this.createTestEnemy(x, y);
    }

    public Enemy createTestEnemy(int x, int y) {
        int i = this.gmStats.getEnemiesCreated() / 20;
        
        return new Enemy(x, y, "test enemy " + this.gmStats.getEnemiesCreated(), map, new EnemyStats(1 + i, new EnemyType("test enemy"), itemDb.createEnemyTestWeapon(), itemDb.createEnemyTestArmor()), true);
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return p;
    }

    public GameManagerStatistics getGmStats() {
        return gmStats;
    }

    public int getMapW() {
        return mapW;
    }

    public int getMapH() {
        return mapH;
    }

    private void nextFloor(ArrayList<CommandResult> results) {
        this.map = m.createTestMap(mapW, mapH, map.getFloor() + 1);
        Location l = f.createPlayerStartLocation(map);
        p.setX(l.getX());
        p.setY(l.getY());
        p.setMap(map);
        map.setPlayer(p);
        this.addEnemy();
        results.add(new CommandResult(true, false, "", true));
    }

    private String getToHitMessage(AttackResult ar) {
        return " (" + Math.round(ar.getToHit() * 100) + "% to hit)";
    }

}
