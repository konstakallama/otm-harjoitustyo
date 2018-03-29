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
public class AttackResult {
    private AttackResultType type;
    private int damageDealt;
    private Moves attacker;
    private Moves target;

    public AttackResult(AttackResultType type, int damage, Moves attacker, Moves target) {
        this.type = type;
        this.damageDealt = damage;
        this.attacker = attacker;
        this.target = target;
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
    
    
}
