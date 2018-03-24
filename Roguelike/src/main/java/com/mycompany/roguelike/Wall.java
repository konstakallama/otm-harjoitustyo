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
public class Wall extends MapObject {

    public Wall(int x, int y, Map map) {
        super(x, y, map);
    }
    

    @Override
    public boolean isOccupied() {
        return true;
    }

    @Override
    public boolean hasHP() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
    
}
