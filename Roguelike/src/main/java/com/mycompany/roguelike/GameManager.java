/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

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

    String defaultFailMessage = "";

    int mapW = 50;
    int mapH = 50;

    public GameManager(PlayerStats stats, Inventory i) {
        this.map = m.createTestMap(mapW, mapH);
        Location l = f.createPlayerStartLocation(map);
        
        this.p = new Player(this.map, l.getX(), l.getY(), stats, i);

        this.gmStats = new GameManagerStatistics(f.getEnemySpawnInterval(map.getFloor()));
    }

    public ArrayList<CommandResult> playCommand(PlayerCommand c) {
        ArrayList<CommandResult> results = new ArrayList<>();

        if (c.getType() == PlayerCommandType.COMMAND_NOT_FOUND) {
            results.add(new CommandResult(false, false, this.defaultFailMessage, new AttackResult(AttackResultType.FAIL, 0, null, null)));
        }

        if (c.getType() == PlayerCommandType.MOVE) {
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

        if (c.getType() == PlayerCommandType.WAIT) {
            results.add(new CommandResult(true, false, "", new AttackResult(AttackResultType.FAIL, 0, null, null)));
            this.playRound(results);
        }

        return results;
    }

    public CommandResult movePlayer(Direction d) {
        return p.move(d);
    }

    public CommandResult playerAttack(Direction d) {
        AttackResult ar = p.attack(d);

        if (ar.getType() == AttackResultType.FAIL) {
            return new CommandResult(false, false, "", ar);
        } else {
            return new CommandResult(true, true, this.parsePlayerAttackResult(ar), ar);
        }
    }

    public void playRound(ArrayList<CommandResult> results) {
        for (AttackResult r : map.takeTurns()) {
            if (r.getType() != AttackResultType.FAIL) {
                results.add(new CommandResult(true, true, this.parseEnemyAttackResult(r), r));
            }
        }

        this.manageRoundStats();
    }

    public String parsePlayerAttackResult(AttackResult result) {
        if (result.getType() == AttackResultType.HIT) {
            return ("You hit the " + result.getTarget().getName() + " dealing " + result.getDamageDealt() + " damage.");
        }
        if (result.getType() == AttackResultType.MISS) {
            return ("You miss the " + result.getTarget().getName() + ".");
        }
        if (result.getType() == AttackResultType.KILL) {
            this.gmStats.increaseEnemiesKilled();
            return ("You kill the " + result.getTarget().getName() + ".");
        }
        return "";
    }

    public String parseEnemyAttackResult(AttackResult result) {
        if (result.getType() == AttackResultType.HIT) {
            return ("The " + result.getAttacker().getName() + " attacks you and deals " + result.getDamageDealt() + " damage.");
        }
        if (result.getType() == AttackResultType.MISS) {
            return ("The " + result.getAttacker().getName() + " misses you.");
        }
        if (result.getType() == AttackResultType.KILL) {
            return ("The " + result.getAttacker().getName() + " attacks and kills you.");
        }
        return "";
    }

    public void manageRoundStats() {
        this.gmStats.increaseTurns();
        if (this.gmStats.increaseEnemySpawnCounter()) {
            this.addEnemy();
        }
    }

    public void addEnemy() {
        Location l = f.createRandomFreeLocation(map);

        map.addEnemy(l.getX(), l.getY(), this.createEnemy(l.getX(), l.getY()));
    }

    public Enemy createEnemy(int x, int y) {
        return this.createTestEnemy(x, y);     
    }
    
    public Enemy createTestEnemy(int x, int y) {
        return new Enemy(x, y, new EnemyType("test enemy " + this.gmStats.getEnemiesCreated()), map, new EnemyStats(2, 2, 2, 2, 2, null, null, null, 0), true);
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

}
