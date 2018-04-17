/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items;

import domain.mapobject.Player;

/**
 *
 * @author konstakallama
 */
public abstract class Effect {
    public abstract boolean applyEffectToPlayer(Player p);
}
