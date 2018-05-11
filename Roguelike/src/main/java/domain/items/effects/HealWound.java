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
public class HealWound extends Effect {
    MessageDb mdb = new MessageDb();
    
    public HealWound(String name) {
        super(name);
    }

    @Override
    public CommandResult applyEffectToPlayer(Player p) {
        if (p.getStats().getWound() <= 0) {
            return new CommandResult(false);
        }
        int w = p.getStats().getWound();
        p.getStats().heal(w);
        return new CommandResult(true, true, mdb.getHealWoundMsg(w));
    }
    
}
