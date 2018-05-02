/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject.player;

/**
 *
 * @author konstakallama
 */
import domain.items.ItemDb;
import domain.items.InventoryItem;
import domain.items.MapItem;
import java.util.*;

public class Inventory {
    private ArrayList<InventoryItem> items;
    private int size;
    ItemDb itemDb = new ItemDb();

    public Inventory(int size) {
        this.items = new ArrayList<>();
        this.size = size;
    }
    /**
     * Returns true if the amount of items in the inventory is equal or greater
     * @return 
     */
    public boolean isFull() {
        return this.items.size() >= this.size;
    }
    /**
     * Adds the given item to the inventory. Returns true if the addition is successful.
     * @param item item to add
     * @return true if the addition is successful.
     */
    public boolean addItem(InventoryItem item) {
        if (this.isFull()) {
            return false;
        } else {
            this.items.add(item);
            return true;
        }
    }
    /**
     * Sorts the inventory. Items will be sorted primarily by type and secondarily by alphabetical order.
     */
    public void sort() {
        Collections.sort(items);
    }

    public ArrayList<InventoryItem> getItems() {
        return items;
    }

    public int getSize() {
        return size;
    }
    /**
     * Converts the given MapItem into the corresponding InventoryItem and adds it to he inventory. Returns true if the addition is successful.
     * @param item
     * @return true if the addition is successful.
     */
    boolean pickUp(MapItem item) {
        try {
            return addItem(this.itemDb.itemConverter(item));
        } catch (Exception e) {
            return false;
        }       
    }
    /**
     * Removes the specified item from the inventory. Returns true if the item was successfully removed.
     * @param i
     * @return true if the item was successfully removed.
     */
    public boolean removeItem(InventoryItem i) {
        return this.items.remove(i);
    }


    
    
    
}
