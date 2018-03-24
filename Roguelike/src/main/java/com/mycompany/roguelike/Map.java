/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

/**
 *
 * @author konstakallama
 */
public class Map {
    private Enemy[][] enemies;
    private MapItem[][] items;
    private Terrain[][] terrain;
    private int floor;
    private Player player;

    public Map(int mapWidth, int mapHeight, Terrain[][] terrain, int floor) {
        this.enemies = new Enemy[mapWidth][mapHeight];
        this.terrain = terrain;
        this.floor = floor;
        this.items = new MapItem[mapWidth][mapHeight];
    }
    
    public boolean isOccupied(int x, int y) {
        if (this.terrain[x][y].isOccupied()) {
            return true;
        } else if (this.enemies[x][y] == null) {
            return false;
        }
        return this.enemies[x][y].isOccupied();
    }
    
    public void addEnemy(int x, int y, Enemy o) {
        this.enemies[x][y] = o;
    }
    
    public boolean moveEnemy(int x, int y, Direction d) {       
        return moveHelper(x, y, d.xVal(), d.yVal());
    }
    
    private boolean moveHelper(int x, int y, int dx, int dy) {
        if (this.isOccupied(x + dx, y + dy)) {
            return false;
        }
        this.enemies[x + dx][y + dy] = this.enemies[x][y];
        this.enemies[x][y] = null;
        return true;
    }
    
    public void removeEnemy(int x, int y) {
        this.enemies[x][y] = null;
    }
    
    public Enemy getEnemy(int x, int y) {
        return this.enemies[x][y];
    }

    public int getFloor() {
        return floor;
    }
    
    public void addItem(int x, int y, MapItem item) {
        this.items[x][y] = item;
    }
    
    public MapItem getItem(int x, int y) {
        return this.items[x][y];
    }
    
    public void removeItem(int x, int y) {
        this.items[x][y] = null;
    }
    
    public void takeTurns() {
        for (int i = 0; i < this.enemies.length; i++) {
            for (int j = 0; j < this.enemies[0].length; j++) {
               if (this.enemies[i][j].isEnemy()) {
                   Enemy e = (Enemy) this.enemies[i][j];
                   e.takeTurn();
               }
            }
        }
    }
    
    public Terrain getTerrain(int x, int y) {
        return this.terrain[x][y];
    }
    
    public boolean movePlayer(Player p, Direction d) {
        boolean b = moveHelper(p.getX(), p.getY(), d.xVal(), d.yVal());
        int nx = p.getX() + d.xVal();
        int ny = p.getY() + d.yVal();
        if (!b) {
            return false;
        }
        if (this.items[nx][ny] != null) {
            p.pickUp(items[nx][ny]);
        }
        if (this.terrain[nx][ny] == Terrain.STAIRS) {
            // Player chooses to go to next floor
            // If yes, create new map, move player to it and return true
        }
        p.setX(nx);
        p.setY(ny);
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Enemy[][] getEnemies() {
        return enemies;
    }

    public MapItem[][] getItems() {
        return items;
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }
    
    public boolean hasEnemy(int x, int y) {
        return this.enemies[x][y] != null;
    }
    
}
