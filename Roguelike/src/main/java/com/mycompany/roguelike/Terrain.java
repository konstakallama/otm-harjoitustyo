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
public enum Terrain {
    FLOOR, WALL, STAIRS;
    
    public boolean isOccupied() {
        if (this == WALL) {
            return true;
        }
        return false;
    }
}
