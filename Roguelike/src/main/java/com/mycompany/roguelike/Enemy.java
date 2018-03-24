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
public abstract class Enemy extends Moves implements TakesTurns {
    boolean visible;
    
    public Enemy(int x, int y, EnemyType type, Map map, EnemyStats stats, boolean visible) {
        super(map, x, y);
        this.stats = stats;
        this.visible = visible;
    }

    public EnemyType getType() {
        EnemyStats s = (EnemyStats) this.stats;
        return s.getType();
    }

    @Override
    public Stats getStats() {
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
    
    
}
