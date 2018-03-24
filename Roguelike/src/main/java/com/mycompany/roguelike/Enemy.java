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
public class Enemy extends Moves implements TakesTurns {
    boolean visible;
    EnemyStats stats;
    
    public Enemy(int x, int y, EnemyType type, Map map, EnemyStats stats, boolean visible) {
        super(map, x, y);
        this.stats = stats;
        this.visible = visible;
    }

    public EnemyType getType() {
        EnemyStats s = (EnemyStats) this.stats;
        return s.getType();
    }

    public EnemyStats getStats() {
        return this.stats;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    @Override
    public boolean isEnemy() {
       return true;
    }

    void die() {
        map.removeEnemy(x, y);
    }

    @Override
    public void takeTurn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
