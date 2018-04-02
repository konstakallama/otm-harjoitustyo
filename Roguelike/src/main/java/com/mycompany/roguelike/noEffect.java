/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

/**
 *
 * @author konstakallama
 */
public class noEffect extends Effect {

    @Override
    public boolean applyEffectToPlayer(Player p) {
        return false;
    }
    
}
