/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author konstakallama
 */
public abstract class MapObject {
    int x;
    int y;
    Map map;
    Formulas f = new Formulas();
    String name;

    public MapObject(int x, int y, Map map, String name) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.name = name;
    }
    
    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }
    
    public boolean isEnemy() {
        return false;
    }
    
    public boolean isOccupied() {
        return true;
    }
    public boolean hasHP() {
        return false;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
//    public abstract Stats getStats();
    public abstract boolean isVisible();
}
