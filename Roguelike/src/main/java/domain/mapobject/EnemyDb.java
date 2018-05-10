/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject;

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
    String fileName = "data/Enemies.kek";
    

    public int getBaseExp(String name) {
        return Integer.parseInt(this.readFromIndex(name, 9));
    }

    double getExpScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 10));
    }

    int getBaseStr(String name) {
        return Integer.parseInt(this.readFromIndex(name, 1));
    }

    double getStrScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 2));
    }

    int getBaseCon(String name) {
        return Integer.parseInt(this.readFromIndex(name, 3));
    }

    double getConScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 4));
    }

    int getBaseInt(String name) {
        return Integer.parseInt(this.readFromIndex(name, 5));
    }

    double getIntScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 6));
    }

    int getBaseDex(String name) {
        return Integer.parseInt(this.readFromIndex(name, 7));
    }

    double getDexScale(String name) {
        return Double.parseDouble(this.readFromIndex(name, 8));
    }
    
    private String readFromIndex(String name, int index) {
        try {
            List<String> l = Files.readAllLines(Paths.get(this.fileName));
            
            for (String line : l) {
                String[] s = line.split("\t");
                if (s[0].equals(name)) {
                    return (s[index]);
                }               
            }      
        } catch (IOException ex) {
            
        }           
        return "";
    }

    int getBaseHP(String name) {
        return Integer.parseInt(this.readFromIndex(name, 11));
    }

    int getHPScale(String name) {
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

}
