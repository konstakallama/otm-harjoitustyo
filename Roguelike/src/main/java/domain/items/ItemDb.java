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
        return new InventoryItem(0, ItemType.CONSUMABLE, "potion", new Heal(5));
    }
    
    public InventoryItem createAtmaWeapon() {
        return new Weapon(100, 1.00, WeaponType.SWORD, "atma weapon");
    }

    private InventoryItem createUberArmor() {
        return new Armor(10, "über armor");
    }
    
}