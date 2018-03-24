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
public abstract class MapObject {
    int x;
    int y;
    Map map;
    Formulas f = new Formulas();

    public MapObject(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.map = map;
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
    
    public boolean isEnemy() {
        return false;
    }
    
    public abstract boolean isOccupied();
    public abstract boolean hasHP();
    public abstract Stats getStats();
    public abstract boolean isVisible();
}
