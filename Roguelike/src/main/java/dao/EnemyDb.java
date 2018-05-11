/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.items.WeaponType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains information about all enemies in the game. Is intended to be
 * replaced with an actual database should there be sufficient time for its
 * implementation.
 *
 * @author konstakallama
 */
public class EnemyDb {

    String fileName = "data/Enemies.txt";

    public int getBaseExp(String name) {
        return Integer.parseInt(this.readFromIndex(name, 9));
    }

    public double getExpScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 10));
    }

    public int getBaseStr(String name) {
        return Integer.parseInt(this.readFromIndex(name, 1));
    }

    public double getStrScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 2));
    }

    public int getBaseCon(String name) {
        return Integer.parseInt(this.readFromIndex(name, 3));
    }

    public double getConScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 4));
    }

    public int getBaseInt(String name) {
        return Integer.parseInt(this.readFromIndex(name, 5));
    }

    public double getIntScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 6));
    }

    public int getBaseDex(String name) {
        return Integer.parseInt(this.readFromIndex(name, 7));
    }

    public double getDexScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 8));
    }

    private String readFromIndexHelper(String name, int index, String filename) throws IOException {

        List<String> l = Files.readAllLines(Paths.get(filename));

        for (String line : l) {
            String[] s = line.split("\t");
            if (s[0].equals(name)) {
                return (s[index]);
            }
        }

        throw new IOException();
    }

    public int getBaseHP(String name) {
        return Integer.parseInt(this.readFromIndex(name, 11));
    }

    public int getHPScale(String name) {
        return Integer.parseInt(this.readFromIndex(name, 12));
    }

    public WeaponType getWeaponType(String name) {
        if (this.readFromIndex(name, 13).equals("Sword")) {
            return WeaponType.SWORD;
        } else if (this.readFromIndex(name, 13).equals("Lance")) {
            return WeaponType.LANCE;
        } else if (this.readFromIndex(name, 13).equals("Axe")) {
            return WeaponType.AXE;
        }

        return null;
    }

    private String readFromIndex(String name, int index) {
        try {
            return this.readFromIndexHelper(name, index, fileName);

        } catch (Exception ex) {
            System.out.println("ee1");
            try {
                return this.readFromIndexHelper(name, index, "src/main/resources/" + fileName);
            } catch (Exception e) {
            }
        }

        return null;
    }

}
