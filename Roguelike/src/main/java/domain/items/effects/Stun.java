/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items.effects;

import domain.gamemanager.CommandResult;
import domain.mapobject.Enemy;
import domain.mapobject.player.PlayerStats;
import domain.support.Formulas;
import domain.support.MessageDb;

/**
 *
 * @author konstakallama
 */
public class Stun extends Effect {
    int duration;
    Formulas f = new Formulas();
    PlayerStats casterStats;
    MessageDb mdb = new MessageDb();
    
    public Stun(int duration, PlayerStats casterStats, String name) {
        super(name);
        this.duration = duration;
        this.casterStats = casterStats;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public PlayerStats getCasterStats() {
        return casterStats;
    }

    public void setCasterStats(PlayerStats casterStats) {
        this.casterStats = casterStats;
    }

    @Override
    public CommandResult applyEffectToEnemy(Enemy e) {
        double hitProb = f.getSpellToHit(this.casterStats, e.getStats(), this.sourceName);
        
        if (!f.spellHits(casterStats, e.getStats(), this.sourceName)) {
            return new CommandResult(true, true, mdb.getMissWithSpellMsg(this.sourceName, e, Math.round(100 * f.getSpellToHit(this.casterStats, e.getStats(), this.sourceName))));
        }
              
        e.stun(duration);
        return new CommandResult(true, true, mdb.getHitWithStunMsg(this.sourceName, Math.round(100 * hitProb), e, duration));
    }
}
