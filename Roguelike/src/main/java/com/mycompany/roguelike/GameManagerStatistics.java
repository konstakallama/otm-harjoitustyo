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

    public ArrayList<com.mycompany.roguelike.Map> getOldMaps() {
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
    
    public boolean increaseEnemySpawnCounter() {
        this.enemySpawnCounter++;
        if (this.enemySpawnCounter == this.enemySpawnInterval) {
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
