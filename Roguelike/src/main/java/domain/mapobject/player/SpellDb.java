/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject.player;

import domain.items.effects.MagicDamage;
import domain.mapobject.Stats;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Contains information about all spells in the game. Is intended to be replaced with an actual database should there be sufficient time for its implementation.
 * @author konstakallama
 */
public class SpellDb {
    String fileName = "data/Spells.kek";
    
    public Spell spellConverter(String name, PlayerStats s) {
        String[] line = this.readLineFromFile(name);
        
        if (line[1].equals("MagicDamage")) {
            return cerateOffensiveSpell(line, s);
        }
        
//        if (name.equals("Fire")) {
//            return this.createFire(s);
//        }
        
        return null;
    }

    private Spell createFire(PlayerStats s) {
        return new Spell("Fire", 6, new MagicDamage(4, s, "Fire"), 4, 0.7);
    }
    
    public String getHelp(String spellName) {
        String[] line = this.readLineFromFile(spellName);
        
        return "Type: " + getTypeDesc(line) + "\n"
                    + "Range type: " + getRangeDesc(line) + "\n"
                    + getAoEDesc(line) + "\n"
                    + "Power: " + line[2] + "\n"
                    + "Accuracy: " + Math.round(Double.parseDouble(line[3]) * 100) + "%\n"
                    + "Range: " + line[5] + " tiles\n"
                    + "Cooldown: " + (Integer.parseInt(line[4]) - 1) + " turns";
    }
    
    private String readIndexFromFile(String name, int index) {
        return this.readLineFromFile(name)[index];
    }

    private String[] readLineFromFile(String name) {
        try {
            List<String> l = Files.readAllLines(Paths.get(this.fileName));
            
            for (String line : l) {
                String[] s = line.split("\t");
                if (s[0].equals(name)) {
                    return (s);
                }
            }
                     
        } catch (IOException ex) {
        }
        
        return null;
    }

    private Spell cerateOffensiveSpell(String[] line, PlayerStats s) {
        
        
//        String name, int cooldown, Effect effect, int range, double accuracy, boolean targetsSelf, boolean isAoE, RangeType rangeType
        
        
        if (line[6].equals("Circle")) {
            return new Spell(line[0], Integer.parseInt(line[4]), new MagicDamage(Integer.parseInt(line[2]), s, line[0]), Integer.parseInt(line[5]), Double.parseDouble(line[3]), Boolean.valueOf(line[7]), Boolean.valueOf(line[8]), RangeType.CIRCLE);
        }
        
        
        return null;
    }

    private String getTypeDesc(String[] line) {
        if (line[1].equals("MagicDamage")) {
            return "Magic attack";
        }
        return "";
    }

    private String getRangeDesc(String[] line) {
        if (line[6].equals("Circle")) {
            return "Circle around caster";
        }
        return "";
    }

    private String getAoEDesc(String[] line) {
        if (!Boolean.valueOf(line[8])) {
            return "Single-target";
        }
        return "Targets all enemies in range";
    }
}
