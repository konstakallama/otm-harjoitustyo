/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items;

/**
 *
 * @author konstakallama
 */
public class Armor extends InventoryItem {
    int def;
    int dexPenalty;
    
    public Armor(int wt, String name, int def) {
        super(wt, ItemType.ARMOR, name);
        this.def = def;
    }
    
    public Armor(int def, String name) {
        this(0, name, def);
    }

    public Armor(int wt, String name, int def, int dexPenalty) {
        super(wt, ItemType.ARMOR, name);
        this.def = def;
        this.dexPenalty = dexPenalty;
    }

    public int getDef() {
        return def;
    }

    public int getDexPenalty() {
        return dexPenalty;
    }
    
    
}
