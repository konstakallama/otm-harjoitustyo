/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject.player;

import domain.items.effects.MagicDamage;
import domain.mapobject.Stats;

/**
 *
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
}
