/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject;

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
        try {
            return addItem(this.itemDb.itemConverter(item));
        } catch (Exception e) {
            return false;
        }       
    }
    
    public boolean removeItem(InventoryItem i) {
        return this.items.remove(i);
    }


    
    
    
}
