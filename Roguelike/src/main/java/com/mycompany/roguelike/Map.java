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
    private MapObject[][] objects;
    private MapItem[][] items;
    private Terrain[][] terrain;
    private int floor;

    public Map(int mapWidth, int mapHeight, Terrain[][] terrain, int floor) {
        this.objects = new MapObject[mapWidth][mapHeight];
        this.terrain = terrain;
        this.floor = floor;
        this.items = new MapItem[mapWidth][mapHeight];
    }
    
    public boolean isOccupied(int x, int y) {
        if (this.terrain[x][y].isOccupied()) {
            return true;
        } else if (this.objects[x][y] == null) {
            return false;
        }
        return this.objects[x][y].isOccupied();
    }
    
    public void addObject(int x, int y, MapObject o) {
        this.objects[x][y] = o;
    }
    
    public boolean moveObject(int x, int y, Direction d) {       
        return moveHelper(x, y, d.xVal(), d.yVal());
    }
    
    private boolean moveHelper(int x, int y, int dx, int dy) {
        if (this.isOccupied(x + dx, y + dy)) {
            return false;
        }
        this.objects[x + dx][y + dy] = this.objects[x][y];
        this.objects[x][y] = null;
        return true;
    }
    
    public void removeObject(int x, int y) {
        this.objects[x][y] = null;
    }
    
    public MapObject getObject(int x, int y) {
        return this.objects[x][y];
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
        for (int i = 0; i < this.objects.length; i++) {
            for (int j = 0; j < this.objects[0].length; j++) {
               if (this.objects[i][j].isEnemy()) {
                   Enemy e = (Enemy) this.objects[i][j];
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
    
    
}
