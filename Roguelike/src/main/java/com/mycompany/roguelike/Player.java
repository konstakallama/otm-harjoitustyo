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
class Player extends Moves {
    private Inventory inventory;

    public Player(Map map, int x, int y, PlayerStats stats, Inventory inventory) {
        super(map, x, y);
        this.stats = stats;
        this.inventory = inventory;
    }
    
    public void newFloor(Map newMap, int startX, int startY) {
        this.map = newMap;
        this.x = startX;
        this.y = startY;
        this.map.addObject(x, y, this);
    }

    @Override
    public boolean attack(Direction d) {
        if (map.getObject(x + d.xVal(), y + d.yVal()).hasHP()) {
            if (f.attackHits(this.stats, map.getObject(x + d.xVal(), y + d.yVal()).getStats())) {
                f.damageCalculation(this.stats, map.getObject(x + d.xVal(), y + d.yVal()).getStats());
                return true;
            }
        }
        return false;
    }

    @Override
    public Stats getStats() {
        return this.stats;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
    
    public boolean pickUp(MapItem item) {
        return this.inventory.pickUp(item);
    }
    
    @Override
    public boolean move(Direction d) {
        return map.movePlayer(this, d);
    }

    
}
