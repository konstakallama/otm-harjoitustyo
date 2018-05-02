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
    
    
    
    public CommandResult applyEffectToPlayer(Player p) {
        return new CommandResult(false);
    }
    
    public CommandResult applyEffectToEnemy(Enemy e) {
        return new CommandResult(false);
    }
}
