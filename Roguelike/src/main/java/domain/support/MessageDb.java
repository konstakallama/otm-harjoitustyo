/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.support;

import domain.gamemanager.AttackResult;
import domain.mapobject.Enemy;

/**
 *
 * @author konstakallama
 */
public class MessageDb {
    
    
    public String getItemHealMsg(int amount, String name) {
        return "You used the " + name + " and were healed for " + amount + " hp.";
    }

    public String getItemStaminaHealMsg(int amount, String name) {
        return "You ate the " + name + " and recovered " + amount + " stamina.";
    }


    public String getAttackWithSpellMsg(String spellName, AttackResult ar) {
        return "You cast " + spellName + " on the " + ar.getTarget() + " dealing " + ar.getDamageDealt() + " damage (" + (Math.round(ar.getToHit() * 100)) + " % to hit).";
    }

    public String getKillWithSpellMsg(String spellName, AttackResult ar) {
        return "You cast " + spellName + " and kill the " + ar.getTarget().getName() + " earning " + ar.getExpGained() + " exp (" + (Math.round(ar.getToHit() * 100)) + " % to hit).";
    }

    public String getSpellLearnedMsg(String spellName) {
        return "You learn the spell " + spellName + ".";
    }

    public String getCastingSpellMsg() {
        return "Select a target with the mouse (esc to cancel).";
    }

    public String getMissWithSpellMsg(String spellName, Enemy e, long toHit) {
        return "You cast " + spellName + " on the " + e.getName() + " and miss (" + toHit + " % to hit).";
    }
}
