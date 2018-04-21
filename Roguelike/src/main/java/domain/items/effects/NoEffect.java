/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items.effects;

import domain.mapobject.Player;

/**
 *
 * @author konstakallama
 */
public class NoEffect extends Effect {

    @Override
    public boolean applyEffectToPlayer(Player p) {
        return false;
    }
    
}
