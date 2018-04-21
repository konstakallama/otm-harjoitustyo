/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items.effects;

import domain.mapobject.Player;

/**
 *
 * @author konstakallama
 */
public class Heal extends Effect {
    private int amount;

    public Heal(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    

    @Override
    public boolean applyEffectToPlayer(Player p) {
        return p.getStats().heal(amount);
    }
    
}
