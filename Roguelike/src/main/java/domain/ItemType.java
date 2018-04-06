/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author konstakallama
 */
public enum ItemType {
    WEAPON, ARMOR, CONSUMABLE, OTHER;
    
    public int sortVal() {
        if (this == WEAPON) {
            return 0;
        } else if (this == ARMOR) {
            return 1;
        } else if (this == CONSUMABLE) {
            return 2;
        } else {
            return 3;
        }
    }
}
