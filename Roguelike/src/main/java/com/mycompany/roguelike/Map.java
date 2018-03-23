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

    public Map(int x, int y) {
        this.objects = new MapObject[x][y];
    }
    
    public boolean isOccupied(int x, int y) {
        if (this.objects[x][y] == null) {
            return false;
        }
        return this.objects[x][y].isOccupied();
    }
    
    public void addObject(int x, int y, MapObject o) {
        this.objects[x][y] = o;
    }
    
    
}
