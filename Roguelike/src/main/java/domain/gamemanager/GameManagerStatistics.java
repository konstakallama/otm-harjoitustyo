/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.gamemanager;
import domain.map.Map;
import java.util.*;

/**
 * Encapsulates a number of statistics about the game's current state.
 * @author konstakallama
 */
public class GameManagerStatistics {
    int turns;
    int enemiesKilled;
    int enemiesCreated;
    int enemySpawnCounter;
    int enemySpawnInterval;
    ArrayList<Map> oldMaps;
    ArrayList<String> logHistory;
    

    public GameManagerStatistics(int enemySpawnInterval) {
        this.turns = 0;
        this.enemiesKilled = 0;
        this.enemiesCreated = 0;
        this.enemySpawnCounter = 0;
        this.oldMaps = new ArrayList<>();
        this.enemySpawnInterval = enemySpawnInterval;
        this.logHistory = new ArrayList<>();
    }

    public int getTurns() {
        return turns;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public int getEnemiesCreated() {
        return enemiesCreated;
    }

    public int getEnemySpawnCounter() {
        return enemySpawnCounter;
    }

    public ArrayList<domain.map.Map> getOldMaps() {
        return oldMaps;
    }
    
    public void increaseTurns() {
        this.turns++;
    }
    
    public void increaseEnemiesKilled() {
        this.enemiesKilled++;
    }
    
    public void increaseEnemiesCreated() {
        this.enemiesCreated++;
    }
    /**
     * Increases the counter towards the spawn time of the next enemy. Returns true if an enemy should be spawned this turn, based on this object's enemySpawnInterval variable. The counter is the reset.
     * @return true if an enemy should be spawned this turn
     */
    public boolean increaseEnemySpawnCounter() {
        this.enemySpawnCounter++;
        if (this.enemySpawnCounter == this.getEnemySpawnInterval()) {
            this.enemySpawnCounter = 0;
            this.enemiesCreated++;
            return true;
        }
        return false;
    }

    public int getEnemySpawnInterval() {
        return enemySpawnInterval;
    }

    public void setEnemySpawnInterval(int enemySpawnInterval) {
        this.enemySpawnInterval = enemySpawnInterval;
    }
    
    public void addOldMap(Map map) {
        this.oldMaps.add(map);
    }

    public ArrayList<String> getLogHistory() {
        return logHistory;
    }
    
    public void addLog(String log) {
        this.logHistory.add(log);
    }
}
