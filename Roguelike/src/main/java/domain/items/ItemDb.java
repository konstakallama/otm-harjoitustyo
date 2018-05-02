/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items;

import domain.items.effects.Heal;
import domain.items.effects.StaminaHeal;
import domain.items.effects.TeachSpell;

/**
 *
 * @author konstakallama
 */
public class ItemDb {

    public InventoryItem itemConverter(MapItem item) throws Exception {
        if (item.getName().equals("test item")) {
            return this.testItemConverter();
        } else if (item.getName().equals("potion")) {
            return this.createPotion();
        } else if (item.getName().equals("atma weapon")) {
            return this.createAtmaWeapon();
        } else if (item.getName().equals("über armor")) {
            return this.createUberArmor();
        } else if (item.getName().equals("apple")) {
            return this.createApple();
        } else if (item.getName().equals("test weapon")) {
            return this.createTestWeapon();
        } else if (item.getName().equals("test armor")) {
            return this.createTestArmor();
        } else if (item.getName().equals("fire tome")) {
            return this.createFireTome();
        }
        
        throw new Exception("Item not found");
    }

    private InventoryItem testItemConverter() {
        return new InventoryItem(0, ItemType.OTHER, "test item");
    }

    public Weapon createTestWeapon() {
        return new Weapon(3, 0.75, WeaponType.SWORD, "test weapon");
    }

    public Armor createTestArmor() {
        return new Armor(1, "test armor");
    }
    
    public Weapon createEnemyTestWeapon() {
        return new Weapon(1, 0.6, WeaponType.SWORD, "enemy test weapon");
    }
    
    public Armor createEnemyTestArmor() {
        return new Armor(0, "enemy test armor");
    }
    
    public InventoryItem createPotion() {
        return new InventoryItem(0, ItemType.CONSUMABLE, "potion", new Heal(5, "potion"));
    }
    
    public InventoryItem createAtmaWeapon() {
        return new Weapon(100, 1.00, WeaponType.SWORD, "atma weapon");
    }

    private InventoryItem createUberArmor() {
        return new Armor(10, "über armor");
    }

    String getDescription(String name) {
        if (name.equals("test item")) {
            return "Type: other";
        } else if (name.equals("potion")) {
            return "Type: comsumable\n"
                    + "Effect: heals 5 hp";
        } else if (name.equals("atma weapon")) {
            return "Type: sword\n"
                    + "Damage: 100\n"
                    + "Hit chance: 100%\n"
                    + "Requires 0 strength";
        } else if (name.equals("über armor")) {
            return "Type: armor\n"
                    + "Defence: 10";
        } else if (name.equals("test weapon")) {
            return "Type: sword\n"
                    + "Damage: 3\n"
                    + "Hit chance: 75%\n"
                    + "Requires 0 strength";
        } else if (name.equals("test armor")) {
            return "Type: armor\n"
                    + "Defence: 1";
        } else if (name.equals("apple")) {
            return "Type: consumable\n"
                    + "Effect: heals 75 stamina";
        } else if (name.equals("fire tome")) {
            return "Type: consumable\n"
                    + "Effect: teaches the spell Fire\n"
                    + "\tPower: 4\n"
                    + "\tAccuracy: 70%";
        }
        
        return "";
    }

    private InventoryItem createApple() {
        return new InventoryItem(0, ItemType.CONSUMABLE, "apple", new StaminaHeal(75, "apple"));
    }

    private InventoryItem createFireTome() {
        return new InventoryItem(0, ItemType.CONSUMABLE, "fire tome", new TeachSpell("fire tome", "Fire"));
    }
    
}
