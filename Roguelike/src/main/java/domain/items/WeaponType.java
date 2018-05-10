/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items;

/**
 *
 * @author konstakallama
 */
public enum WeaponType {
    SWORD, AXE, LANCE;

    @Override
    public String toString() {
        if (this == SWORD) {
            return "Sword";
        } else if (this == LANCE) {
            return "Lance";
        } else if (this == AXE) {
            return "Axe";
        }
        return "";
    }
    
    
}
