/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject.player;

import domain.gamemanager.CommandResult;
import domain.items.effects.Effect;
import domain.mapobject.Enemy;
import domain.support.Location;

/**
 *
 * @author konstakallama
 */
public class Spell {
    String name;
    int cooldown;
    Effect effect;
    boolean targetsSelf;
    int range;
    boolean isAoE;
    RangeType rangeType;
    double accuracy;
    int turnsInCooldown;

    public Spell(String name, int cooldown, Effect effect, int range, double accuracy, boolean targetsSelf, boolean isAoE, RangeType rangeType) {
        this.name = name;
        this.cooldown = cooldown;
        this.effect = effect;
        this.targetsSelf = targetsSelf;
        this.range = range;
        this.isAoE = isAoE;
        this.rangeType = rangeType;
        this.accuracy = accuracy;
        this.turnsInCooldown = 0;
    }

    public Spell(String name, int cooldown, Effect effect, int range, double accuracy) {
        this(name, cooldown, effect, range, accuracy, false, false, RangeType.CIRCLE);
    }

    public String getName() {
        return name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Effect getEffect() {
        return effect;
    }

    public boolean targetsSelf() {
        return targetsSelf;
    }

    public int getRange() {
        return range;
    }

    public boolean isAoE() {
        return isAoE;
    }

    public RangeType getRangeType() {
        return rangeType;
    }

    public double getAccuracy() {
        return accuracy;
    }
    /**
     * Returns true if the spell cast from s has t in its valid casting range.
     * @param s location the spell is cast from
     * @param t location of the spell's target
     * @return true if the spell cast from s has t in its valid casting range.
     */
    public boolean inRange(Location s, Location t) {
        return s.manhattanDistance(t) <= this.getRange();
    }
    /**
     * Applies the spell's effect to a given Enemy and sets the spell's cooldown counter to its cooldown value. 
     * @param e
     * @return 
     */
    public CommandResult useOnEnemy(Enemy e) {
        this.turnsInCooldown = this.cooldown;
        return this.effect.applyEffectToEnemy(e);
    }

    public int getTurnsInCooldown() {
        return turnsInCooldown;
    }

    public void setTurnsInCooldown(int turnsInCooldown) {
        this.turnsInCooldown = turnsInCooldown;
    }
    /**
     * Reduces the spell's cooldown counter by one. It will not be lowered 
     */
    public void advanceCooldown() {
        this.turnsInCooldown--;
        if (this.turnsInCooldown < 0) {
            this.turnsInCooldown = 0;
        }
    }
    /**
     * Returns true if the spells cooldown counter is at 0 and the spell can be used.
     * @return true if the spells cooldown counter is at 0 and the spell can be used.
     */
    public boolean canBeUsed() {
        return this.turnsInCooldown <= 0;
    }
    

    
    
    
}
