/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.items.Armor;
import domain.items.InventoryItem;
import domain.items.ItemType;
import domain.items.MapItem;
import domain.items.Weapon;
import domain.items.WeaponType;
import domain.items.effects.Heal;
import domain.items.effects.StaminaHeal;
import domain.items.effects.TeachSpell;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains information about all items in the game. Is intended to be replaced
 * with an actual database should there be sufficient time for its
 * implementation.
 *
 * @author konstakallama
 */
public class ItemDb {

    String fileName = "data/Items.txt";

    public InventoryItem itemConverter(MapItem item) throws Exception {
        return this.itemConverter(item.getName());
    }

    public InventoryItem itemConverter(String itemName) throws Exception {
        String[] line = this.readLineFromFile(itemName);

        if (line[1].equals("Consumable")) {
            return convertConsumable(line);
        } else if (line[1].equals("Weapon")) {
            return convertWeapon(line);
        } else if (line[1].equals("Armor")) {
            return convertArmor(line);
        } else if (line[1].equals("Other")) {
            return convertOther(line);
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

    public Weapon createEnemyTestWeapon(WeaponType wt) {
        return new Weapon(1, 0.6, wt, "enemy test weapon");
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

    public String getDescription(String name) {
        String[] line = this.readLineFromFile(name);

        if (line[1].equals("Consumable")) {
            return getConsumableDescription(line);
        } else if (line[1].equals("Weapon")) {
            return getWeaponDescription(line);
        } else if (line[1].equals("Armor")) {
            return getArmorDescription(line);
        } else if (line[1].equals("Other")) {
            return getOtherDescription(line);
        }

//        if (name.equals("test item")) {
//            return "Type: other";
//        } else if (name.equals("potion")) {
//            return "Type: consumable\n"
//                    + "Effect: heals 5 hp";
//        } else if (name.equals("atma weapon")) {
//            return "Type: sword\n"
//                    + "Damage: 100\n"
//                    + "Hit chance: 100%\n"
//                    + "Requires 0 strength";
//        } else if (name.equals("über armor")) {
//            return "Type: armor\n"
//                    + "Defence: 10";
//        } else if (name.equals("test weapon")) {
//            return "Type: sword\n"
//                    + "Damage: 3\n"
//                    + "Hit chance: 75%\n"
//                    + "Requires 0 strength";
//        } else if (name.equals("test armor")) {
//            return "Type: armor\n"
//                    + "Defence: 1";
//        } else if (name.equals("apple")) {
//            return "Type: consumable\n"
//                    + "Effect: heals 75 stamina";
//        } else if (name.equals("fire tome")) {
//            return "Type: consumable\n"
//                    + "Effect: teaches the spell Fire\n"
//                    + "\tPower: 4\n"
//                    + "\tAccuracy: 70%";
//        }
        return "";
    }

    private InventoryItem createApple() {
        return new InventoryItem(0, ItemType.CONSUMABLE, "apple", new StaminaHeal(75, "apple"));
    }

    private InventoryItem createFireTome() {
        return new InventoryItem(0, ItemType.CONSUMABLE, "fire tome", new TeachSpell("fire tome", "Fire"));
    }

    private String readIndexFromFile(String name, int index) {
        return this.readLineFromFile(name)[index];
    }

    private String[] readLineFromFileHelper(String name, String filename) throws IOException {

        List<String> l = Files.readAllLines(Paths.get(filename));

        for (String line : l) {
            String[] s = line.split("\t");
            if (s[0].equals(name)) {
                return (s);
            }
        }
        
        throw new IOException();

    }

    private InventoryItem convertConsumable(String[] line) throws Exception {
        if (line[8].equals("Heal")) {
            return new InventoryItem(Integer.parseInt(line[2]), ItemType.CONSUMABLE, line[0], new Heal(Integer.parseInt(line[9]), line[0]));
        } else if (line[8].equals("StaminaHeal")) {
            return new InventoryItem(Integer.parseInt(line[2]), ItemType.CONSUMABLE, line[0], new StaminaHeal(Integer.parseInt(line[9]), line[0]));
        } else if (line[8].equals("TeachSpell")) {
            return new InventoryItem(Integer.parseInt(line[2]), ItemType.CONSUMABLE, line[0], new TeachSpell(line[0], line[9]));
        }

        throw new Exception("Item not found");
    }

    private InventoryItem convertWeapon(String[] line) throws Exception {
        if (line[3].equals("Sword")) {
            return new Weapon(Integer.parseInt(line[4]), Double.parseDouble(line[5]), WeaponType.SWORD, Integer.parseInt(line[2]), Integer.parseInt(line[6]), line[0]);
        } else if (line[3].equals("Lance")) {
            return new Weapon(Integer.parseInt(line[4]), Double.parseDouble(line[5]), WeaponType.LANCE, Integer.parseInt(line[2]), Integer.parseInt(line[6]), line[0]);
        } else if (line[3].equals("Axe")) {
            return new Weapon(Integer.parseInt(line[4]), Double.parseDouble(line[5]), WeaponType.AXE, Integer.parseInt(line[2]), Integer.parseInt(line[6]), line[0]);
        }

        throw new Exception("Item not found");
    }

    private InventoryItem convertArmor(String[] line) {
        return new Armor(Integer.parseInt(line[2]), line[0], Integer.parseInt(line[7]), Integer.parseInt(line[10]));
    }

    private InventoryItem convertOther(String[] line) {
        return new InventoryItem(Integer.parseInt(line[2]), ItemType.OTHER, line[0]);
    }

    private String getConsumableDescription(String[] line) {
        if (line[8].equals("Heal")) {
            return "Type: consumable\n"
                    + "Effect: heals " + line[9] + " hp";
        } else if (line[8].equals("StaminaHeal")) {
            return "Type: consumable\n"
                    + "Effect: heals " + line[9] + " stamina";
        } else if (line[8].equals("TeachSpell")) {
            return "Type: consumable\n"
                    + "Effect: teaches the spell " + line[9] + ".";
        }

        return "";
    }

    private String getWeaponDescription(String[] line) {
        return "Type: " + line[3] + "\n"
                + "Damage: " + line[4] + "\n"
                + "Hit chance: " + Math.round(Double.parseDouble(line[5]) * 100) + "%\n"
                + "Requires " + line[6] + " strength";
    }

    private String getArmorDescription(String[] line) {
        return "Type: armor\n"
                + "Defence: " + line[7] + "\n"
                + "Dex penalty: " + line[10];
    }

    private String getOtherDescription(String[] line) {
        return "Type: other";
    }

    private String[] readLineFromFile(String name) {
        try {
            return this.readLineFromFileHelper(name, fileName);

        } catch (Exception ex) {
            System.out.println(fileName);
            System.out.println("ie1");
            String fn = "src/main/resources/" + fileName;
            try {
                
                return this.readLineFromFileHelper(name, fn);
            } catch (Exception e) {
                System.out.println(fn);
                System.out.println("ie2");
            }
        }

        return null;
    }

}
