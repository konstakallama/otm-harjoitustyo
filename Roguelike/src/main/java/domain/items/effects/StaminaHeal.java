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
public class StaminaHeal extends Effect {
    int amount;

    public StaminaHeal(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean applyEffectToPlayer(Player p) {
        if (p.getStats().getStaminaDmg() <= 0) {
            return false;
        }
        p.getStats().increaseStamina(amount);
        return true;
    }
    
}
