/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject.player;

import domain.items.effects.MagicDamage;
import domain.mapobject.Stats;

/**
 * Contains information about all spells in the game. Is intended to be replaced with an actual database should there be sufficient time for its implementation.
 * @author konstakallama
 */
public class SpellDb {
    
    public Spell spellConverter(String name, PlayerStats s) {
        if (name.equals("Fire")) {
            return this.createFire(s);
        }
        
        return null;
    }

    private Spell createFire(PlayerStats s) {
        return new Spell("Fire", 6, new MagicDamage(4, s, "Fire"), 4, 0.7);
    }
    
    public String getHelp(String spellName) {
        if (spellName.equals("Fire")) {
            return "Type: Magic attack\n"
                    + "Range type: Circle around caster\n"
                    + "Single-target\n"
                    + "Power: 4\n"
                    + "Accuracy: 70%\n"
                    + "Range: 4 tiles\n"
                    + "Cooldown: 5 turns";
        }
        return "";
    }
}
