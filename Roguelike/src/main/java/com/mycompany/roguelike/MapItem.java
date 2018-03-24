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
public class MapItem extends MapObject {
    private String name;
    private boolean visible;
    
    public MapItem(int x, int y, Map map, String name, boolean visible) {
        super(x, y, map);
        this.name = name;
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isOccupied() {
        return false;
    }

    @Override
    public boolean hasHP() {
        return false;
    }

    @Override
    public Stats getStats() {
        throw new UnsupportedOperationException("Item has no stats"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    
    
}
