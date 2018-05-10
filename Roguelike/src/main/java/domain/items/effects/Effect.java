/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items.effects;

import domain.gamemanager.CommandResult;
import domain.mapobject.Enemy;
import domain.mapobject.player.Player;
import domain.support.MessageDb;

/**
 *
 * @author konstakallama
 */
public abstract class Effect {
    String sourceName;
    MessageDb mdb;

    public Effect(String name) {
        this.sourceName = name;
    }

    public String getName() {
        return sourceName;
    }
    
    
    /**
     * Applies the effect specified by this class to the Player p. Returns a CommandResult detailing the results of the event.
     * @param p player to apply the effect to
     * @return a CommandResult detailing the results of the event.
     */
    public CommandResult applyEffectToPlayer(Player p) {
        return new CommandResult(false);
    }
    
    /**
     * Applies the effect specified by this class to the Enemy e. Returns a CommandResult detailing the results of the event.
     * @param e Enemy to apply the effect to
     * @return a CommandResult detailing the results of the event.
     */
    public CommandResult applyEffectToEnemy(Enemy e) {
        return new CommandResult(false);
    }
}
