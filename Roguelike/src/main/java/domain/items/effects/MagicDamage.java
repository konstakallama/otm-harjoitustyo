/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items.effects;

import domain.gamemanager.AttackResult;
import domain.gamemanager.CommandResult;
import domain.mapobject.Enemy;
import domain.mapobject.Player.Player;
import domain.mapobject.Player.PlayerStats;
import domain.mapobject.Stats;
import domain.support.Formulas;
import domain.support.MessageDb;

/**
 *
 * @author konstakallama
 */
public class MagicDamage extends Effect {
    int power;
    Formulas f = new Formulas();
    PlayerStats casterStats;
    MessageDb mdb = new MessageDb();

    public MagicDamage(int power, PlayerStats casterStats, String name) {
        super(name);
        this.power = power;
        this.casterStats = casterStats;
    }

    public MagicDamage(int power, String name) {
        super(name);
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Stats getCasterStats() {
        return casterStats;
    }

    public void setCasterStats(PlayerStats casterStats) {
        this.casterStats = casterStats;
    }

    @Override
    public CommandResult applyEffectToEnemy(Enemy e) {
        if (!f.spellHits(casterStats, e.getStats(), this.sourceName)) {
            return new CommandResult(true, true, mdb.getMissWithSpellMsg(this.sourceName, e, Math.round(100 * f.getSpellToHit(this.casterStats, e.getStats(), this.sourceName))));
        }
              
        AttackResult ar = f.magicDamageCalculation(casterStats, e, power, this.sourceName);
        if (ar.isKill()) {
            return new CommandResult(true, true, mdb.getKillWithSpellMsg(this.sourceName, ar), ar);
        }
        return new CommandResult(true, true, mdb.getAttackWithSpellMsg(this.sourceName, ar), ar);
    }

    @Override
    public CommandResult applyEffectToPlayer(Player p) {
        return super.applyEffectToPlayer(p); //To change body of generated methods, choose Tools | Templates.
    }
    
}
