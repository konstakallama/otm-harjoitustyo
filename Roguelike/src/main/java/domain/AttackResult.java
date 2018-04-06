/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author konstakallama
 */
public class AttackResult {
    private AttackResultType type;
    private int damageDealt;
    private Moves attacker;
    private Moves target;
    private boolean isLevelUp;
    private double toHit;
    private int expGained;

    public AttackResult(AttackResultType type, int damage, Moves attacker, Moves target) {
        this.type = type;
        this.damageDealt = damage;
        this.attacker = attacker;
        this.target = target;
        this.isLevelUp = false;
        this.toHit = 0;
    }

    public AttackResult(AttackResultType type, int damageDealt, Moves attacker, Moves target, boolean isLevelUp) {
        this(type, damageDealt, attacker, target);
        this.isLevelUp = isLevelUp;
    }

    public AttackResult(AttackResultType type, int damageDealt, Moves attacker, Moves target, boolean isLevelUp, double toHit) {
        this(type, damageDealt, attacker, target, isLevelUp);
        this.toHit = toHit;
    }

    public AttackResult(AttackResultType type, int damageDealt, Moves attacker, Moves target, double toHit) {
        this(type, damageDealt, attacker, target, false, toHit);
        this.toHit = toHit;
    }

    public AttackResult(AttackResultType type, int damageDealt, Moves attacker, Moves target, boolean isLevelUp, double toHit, int expGained) {
        this.type = type;
        this.damageDealt = damageDealt;
        this.attacker = attacker;
        this.target = target;
        this.isLevelUp = isLevelUp;
        this.toHit = toHit;
        this.expGained = expGained;
    }
    
    

    public AttackResultType getType() {
        return type;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public Moves getAttacker() {
        return attacker;
    }

    public Moves getTarget() {
        return target;
    }
    
    public boolean isLevelUp() {
        return this.isLevelUp;
    }

    public double getToHit() {
        return toHit;
    }

    public void setType(AttackResultType type) {
        this.type = type;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public void setAttacker(Moves attacker) {
        this.attacker = attacker;
    }

    public void setTarget(Moves target) {
        this.target = target;
    }

    public void setIsLevelUp(boolean isLevelUp) {
        this.isLevelUp = isLevelUp;
    }

    public void setToHit(double toHit) {
        this.toHit = toHit;
    }

    public int getExpGained() {
        return expGained;
    }

    public void setExpGained(int expGained) {
        this.expGained = expGained;
    }
    
    
}
