/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.items.effects.Freeze;
import domain.items.effects.HealWound;
import domain.items.effects.MagicDamage;
import domain.items.effects.Stun;
import domain.mapobject.Stats;
import domain.mapobject.player.PlayerStats;
import domain.mapobject.player.RangeType;
import domain.mapobject.player.Spell;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Contains information about all spells in the game. Is intended to be replaced
 * with an actual database should there be sufficient time for its
 * implementation.
 *
 * @author konstakallama
 */
public class SpellDb {

    String fileName = "data/Spells.txt";
    FileReader fr = new FileReader(fileName);

    public Spell spellConverter(String name, PlayerStats s) {
        String[] line = this.readLineFromFile(name);
//        System.out.println("n: " + name);
//        System.out.println("l0: " + line[0]);

        if (line[1].equals("MagicDamage")) {
            return cerateOffensiveSpell(line, s);
        } else if (line[1].equals("Freeze")) {
            return createFreeze(line, s);
        } else if (line[1].equals("Stun")) {
            return createStun(line, s);
        } else if (line[1].equals("HealWound")) {
            return createHealWound(line, s);
        }

        return null;
    }

    public String getHelp(String spellName) {
        String[] line = this.readLineFromFile(spellName);

        if (line[1].equals("HealWound")) {
            return getTypeDesc(line) + "\n"
                    + "Range type: " + getRangeDesc(line) + "\n"
                    + "Cooldown: " + (Integer.parseInt(line[4]) - 1) + " turns";
        }

        return "Type: " + getTypeDesc(line) + "\n"
                + "Range type: " + getRangeDesc(line) + "\n"
                + getAoEDesc(line) + "\n"
                + "Power: " + line[2] + "\n"
                + "Accuracy: " + Math.round(Double.parseDouble(line[3]) * 100) + "%\n"
                + "Range: " + line[5] + " tiles\n"
                + "Cooldown: " + (Integer.parseInt(line[4]) - 1) + " turns";
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

    private Spell cerateOffensiveSpell(String[] line, PlayerStats s) {
        if (line[6].equals("Circle")) {
            return new Spell(line[0], Integer.parseInt(line[4]), new MagicDamage(Integer.parseInt(line[2]), s, line[0]), Integer.parseInt(line[5]), Double.parseDouble(line[3]), Boolean.valueOf(line[7]), Boolean.valueOf(line[8]), RangeType.CIRCLE);
        }

        return null;
    }

    private String getTypeDesc(String[] line) {
        if (line[1].equals("MagicDamage")) {
            return "Magic attack";
        } else if (line[1].equals("Freeze")) {
            return "Freeze (power = duration, doesn't deal damage).\n"
                    + "Frozen enemies may not move or evede attacks or spells.";
        } else if (line[1].equals("Stun")) {
            return "Stun (power = duration, doesn't deal damage).\n"
                    + "Stunned enemies may not move, attack or evede.";
        } else if (line[1].equals("HealWound")) {
            return "Heals the most recent wound suffered by the target. The wound is removed afterwards.";
        }
        return "Type decription not found";
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

    private Spell createFreeze(String[] line, PlayerStats s) {
        if (line[6].equals("Circle")) {
            return new Spell(line[0], Integer.parseInt(line[4]), new Freeze(Integer.parseInt(line[2]), s, line[0]), Integer.parseInt(line[5]), Double.parseDouble(line[3]), Boolean.valueOf(line[7]), Boolean.valueOf(line[8]), RangeType.CIRCLE);
        }

        return null;
    }

    private Spell createStun(String[] line, PlayerStats s) {
        if (line[6].equals("Circle")) {
            return new Spell(line[0], Integer.parseInt(line[4]), new Stun(Integer.parseInt(line[2]), s, line[0]), Integer.parseInt(line[5]), Double.parseDouble(line[3]), Boolean.valueOf(line[7]), Boolean.valueOf(line[8]), RangeType.CIRCLE);
        }

        return null;
    }

    private Spell createHealWound(String[] line, PlayerStats s) {
        return new Spell(line[0], Integer.parseInt(line[4]), new HealWound(line[0]), Integer.parseInt(line[5]), Double.parseDouble(line[3]), Boolean.valueOf(line[7]), Boolean.valueOf(line[8]), RangeType.SELF);
    }

    private String[] readLineFromFile(String name) {
        return fr.readLineByName(name);
        
//        try {
//            return this.readLineFromFileHelper(name, fileName);
//        } catch (Exception ex) {
//            try {
//                return this.readLineFromFileHelper(name, "src/main/resources/" + fileName);
//            } catch (Exception e) {
//            }
//        }
//
//        return null;
    }
}
