/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items.effects;

import domain.gamemanager.CommandResult;
import domain.mapobject.player.Player;
import domain.support.MessageDb;

/**
 *
 * @author konstakallama
 */
public class StaminaHeal extends Effect {
    int amount;
    MessageDb mdb = new MessageDb();

    public StaminaHeal(int amount, String name) {
        super(name);
        this.amount = amount;
    }

    @Override
    public CommandResult applyEffectToPlayer(Player p) {
        if (p.getStats().getStaminaDmg() <= 0) {
            return new CommandResult(false);
        }
        int am = (int) Math.round(Math.floor(this.amount * 0.01 * p.getStats().getMaxStamina()));
        p.getStats().increaseStamina(am);
        return new CommandResult(true, true, mdb.getItemStaminaHealMsg(am, this.sourceName));
    }
    
}
