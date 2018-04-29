/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items.effects;

import domain.gamemanager.CommandResult;
import domain.mapobject.Player.Player;
import domain.mapobject.Player.Spell;
import domain.mapobject.Player.SpellDb;
import domain.support.MessageDb;

/**
 *
 * @author konstakallama
 */
public class TeachSpell extends Effect {
    String spellName;
    SpellDb sdb = new SpellDb();
    MessageDb mdb = new MessageDb();
    
    public TeachSpell(String name, String spellName) {
        super(name);
        this.spellName = spellName;
    }

    @Override
    public CommandResult applyEffectToPlayer(Player p) {
        if (p.getStats().getFreeSpellBookSlots() <= 0) {
            return new CommandResult(false);
        }
        Spell s = sdb.spellConverter(spellName, p.getStats());
        p.getStats().learnSpell(s);
        return new CommandResult(true, true, mdb.getSpellLearnedMsg(spellName));
    }
    
}
