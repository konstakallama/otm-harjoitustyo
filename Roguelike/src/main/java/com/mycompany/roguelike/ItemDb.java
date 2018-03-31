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
class ItemDb {

    InventoryItem itemConverter(MapItem item) {
        return this.testItemConverter();
    }

    private InventoryItem testItemConverter() {
        return new InventoryItem(0, ItemType.OTHER, "test item");
    }

    Weapon createTestWeapon() {
        return new Weapon(3, 0.75, WeaponType.SWORD, "test weapon");
    }

    Armor createTestArmor() {
        return new Armor(1, "test armor");
    }
    
    Weapon createEnemyTestWeapon() {
        return new Weapon(1, 0.6, WeaponType.SWORD, "enemy test weapon");
    }
    
    Armor createEnemyTestArmor() {
        return new Armor(0, "enemy test armor");
    }
    
}
