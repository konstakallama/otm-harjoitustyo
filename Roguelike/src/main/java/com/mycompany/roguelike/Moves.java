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
public abstract class Moves extends MapObject {

    public Moves(Map map, int x, int y) {
        super(x, y, map);
    }

    @Override
    public boolean hasHP() {
        return true;
    }

    @Override
    public boolean isOccupied() {
        return true;
    }

    public boolean move(Direction d) {
        if (this.map.moveEnemy(x, y, d)) {
            this.x += d.xVal();
            this.y += d.yVal();
            return true;
        }
        return false;   
    }

//    public boolean attack(Direction d) {
//        if (map.getEnemy(x + d.xVal(), y + d.yVal()).hasHP()) {
//            if (f.attackHits(this.stats, map.getEnemy(x + d.xVal(), y + d.yVal()).getStats())) {
//                f.damageCalculation(this.stats, map.getEnemy(x + d.xVal(), y + d.yVal()).getStats());
//                return true;
//            }
//        }
//        return false;
//    }
}
