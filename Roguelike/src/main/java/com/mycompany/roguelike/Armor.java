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
public class Armor extends InventoryItem {
    int def;
    
    public Armor(int wt, ItemType itemType, String name, int def) {
        super(wt, itemType, name);
        this.def = def;
    }

    public int getDef() {
        return def;
    }
    
    
}
