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
import java.util.*;

class Inventory {
    private ArrayList<InventoryItem> items;
    private int size;

    public Inventory(int size) {
        this.items = new ArrayList<>();
        this.size = size;
    }
    
    public boolean isFull() {
        return this.items.size() >= this.size;
    }
    
    public boolean addItem(InventoryItem item) {
        if (this.isFull()) {
            return false;
        } else {
            this.items.add(item);
            return true;
        }
    }
    
    public void sort() {
        Collections.sort(items);
    }

    public ArrayList<InventoryItem> getItems() {
        return items;
    }

    public int getSize() {
        return size;
    }

    boolean pickUp(MapItem item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}