/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject.player;

import domain.map.Map;
import domain.items.ItemType;
import domain.items.Weapon;
import domain.items.InventoryItem;
import domain.items.Armor;
import domain.items.MapItem;
import domain.support.Direction;
import domain.gamemanager.CommandResult;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.AttackResult;
import domain.mapobject.Moves;
import domain.support.Location;

/**
 *
 * @author konstakallama
 */
public class Player extends Moves {
    private Inventory inventory;
    private PlayerStats stats;
    private int visionRange;

    public Player(Map map, int x, int y, PlayerStats stats, Inventory inventory) {
        super(map, x, y, "player");       
        this.stats = stats;
        this.inventory = inventory;
        this.map.setPlayer(this);
        this.visionRange = 3;
    }
    
    public Player(Map map, int x, int y, PlayerStats stats, Inventory inventory, String name) {
        super(map, x, y, name);       
        this.stats = stats;
        this.inventory = inventory;
        this.map.setPlayer(this);
        this.visionRange = 3;
    }
//    public void newFloor(Map newMap, int startX, int startY) {
//        this.map = newMap;
//        this.x = startX;
//        this.y = startY;
//        this.map.setPlayer(this);
//    }
    /**
     * Attacks the Enemy in d, if there is one. Returns an AttackResult determining the outcome of the attack.
     * @param d
     * @return an AttackResult determining the outcome of the attack.
     */
    public AttackResult attack(Direction d) {
        if (map.hasEnemy(x + d.xVal(), y + d.yVal())) {            
            if (f.attackHits(this.stats, map.getEnemy(x + d.xVal(), y + d.yVal()).getStats())) {
                return f.playerDamageCalculation(this, map.getEnemy(x + d.xVal(), y + d.yVal()));              
            } else {
                return new AttackResult(AttackResultType.MISS, 0, this, map.getEnemy(x + d.xVal(), y + d.yVal()), (f.hitProb(stats, map.getEnemy(x + d.xVal(), y + d.yVal()).getStats())));
            }
        }
        return new AttackResult(AttackResultType.FAIL, 0, this, null);
    }

    public PlayerStats getStats() {
        return this.stats;
    }
    
    public int getCurrentHP() {
        return this.stats.getCurrentHP();
    }

    @Override
    public boolean isVisible() {
        return true;
    }
    /**
     * Picks up the item using inventory.pickUp(item). Returns true if the action is successful.
     * @param item
     * @return true if the action is successful.
     */
    public boolean pickUp(MapItem item) {
        return this.inventory.pickUp(item);
    }
    
    /**
     * Moves the player in d using map.movePlayer(d). Returns a CommandResult determining the outcome of the move.
     * @param d
     * @return a CommandResult determining the outcome of the move.
     */
    public CommandResult move(Direction d) {
        return map.movePlayer(d);
    }
    
    public int getMaxHP() {
        return this.stats.getMaxHP();
    }

    public Inventory getInventory() {
        return inventory;
    }
    /**
     * Equips the given item using stats.equipWeapon or stats.equipArmor. Returns true if the equip is successful.
     * @param i
     * @return true if the equip is successful.
     */
    public boolean equip(InventoryItem i) {
        if (i.getItemType() == ItemType.WEAPON) {
            return this.stats.equipWeapon((Weapon) i, this.inventory);
        } else if (i.getItemType() == ItemType.ARMOR) {
            return this.stats.equipArmor((Armor) i, this.inventory);
        }
        return false;
    }
    
    public Location getLocation() {
        return new Location(x, y);
    }

    public int getVisionRange() {
        return visionRange;
    }

    public void setVisionRange(int visionRange) {
        this.visionRange = visionRange;
    }
    

    
}
