/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

import java.util.*;

/**
 *
 * @author konstakallama
 */
public class Enemy extends Moves implements TakesTurns {

    boolean visible;
    EnemyStats stats;
    boolean hasMoved;

    public Enemy(int x, int y, EnemyType type, Map map, EnemyStats stats, boolean visible) {
        super(map, x, y, type.getName());
        this.stats = stats;
        this.visible = visible;
        this.hasMoved = false;
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
    public AttackResult takeTurn() {
        AttackResult result;

        if (!this.hasMoved) {
            Direction pd = map.getPlayerDirection(x, y);
            if (map.hasPlayer(x + pd.xVal(), y + pd.yVal())) {
                result = this.attack(pd);
            } else {
                result = new AttackResult(AttackResultType.FAIL, 0, this, null);
                if (map.isOccupied(x + pd.xVal(), y + pd.yVal())) {
                    this.randomMove();
                } else {
                    this.move(pd);
                }
            }
        } else {
            result = new AttackResult(AttackResultType.FAIL, 0, this, null);
        }
        
        this.hasMoved = true;
        return result;
    }

    private void randomMove() {
        Random r = new Random();
        double d = r.nextDouble();
        if (d < 0.25) {
            this.move(Direction.UP);
        } else if (d < 0.5) {
            this.move(Direction.DOWN);
        } else if (d < 0.75) {
            this.move(Direction.RIGHT);
        } else {
            this.move(Direction.LEFT);
        }
    }

    public void reset() {
        this.hasMoved = false;
    }

    public AttackResult attack(Direction d) {
        if (map.hasPlayer(x + d.xVal(), y + d.yVal())) {
            if (f.attackHits(this.stats, map.getPlayer().getStats())) {
                return f.enemyDamageCalculation(this, map.getPlayer());
            } else {
                return new AttackResult(AttackResultType.MISS, 0, this, map.getEnemy(x + d.xVal(), y + d.yVal()));
            }
        }
        return new AttackResult(AttackResultType.FAIL, 0, this, null);
    }
    
    public boolean move(Direction d) {
        if (this.map.moveEnemy(x, y, d)) {
            this.x += d.xVal();
            this.y += d.yVal();
            return true;
        }
        return false;   
    }

}
