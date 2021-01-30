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
public class Heal extends Effect {
    private int amount;
    private MessageDb mdb = new MessageDb();

    public Heal(int amount, String name) {
        super(name);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    

    @Override
    public CommandResult applyEffectToPlayer(Player p) {
        int am = (int) Math.round(Math.floor(this.amount * 0.01 * p.getStats().getMaxHP()));
        boolean success = p.getStats().heal(am);
        return new CommandResult(success, true, mdb.getItemHealMsg(am, this.sourceName));
    }
    
}
