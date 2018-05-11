/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items;

import dao.ItemDb;
import domain.items.effects.Effect;
import domain.items.effects.NoEffect;

/**
 *
 * @author konstakallama
 */
public class InventoryItem implements Comparable<InventoryItem> {
    int wt;
    ItemType itemType;
    String name;
    Effect effect;
    ItemDb db = new ItemDb();

    public InventoryItem(int wt, ItemType itemType, String name) {
        this(wt, itemType, name, new NoEffect(name));
    }

    public InventoryItem(int wt, ItemType itemType, String name, Effect effect) {
        this.wt = wt;
        this.itemType = itemType;
        this.name = name;
        this.effect = effect;
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

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
    
    public String getDescription() {
        return db.getDescription(this.name);
    }
    
    
    
    
    
    
}
