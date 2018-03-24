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
public abstract class InventoryItem implements Comparable<InventoryItem> {
    int wt;
    ItemType itemType;
    String name;

    public InventoryItem(int wt, ItemType itemType, String name) {
        this.wt = wt;
        this.itemType = itemType;
        this.name = name;
    }

    @Override
    public int compareTo(InventoryItem o) {
        if (this.itemType.sortVal() - o.itemType.sortVal() != 0) {
            return this.itemType.sortVal() - o.itemType.sortVal();
        }
        return this.name.compareTo(o.getName());
    }

    public int getWt() {
        return wt;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public String getName() {
        return name;
    }
    
    
    
    
    
    
}
