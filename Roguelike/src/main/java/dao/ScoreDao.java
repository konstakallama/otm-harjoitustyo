/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.Score;
import java.sql.*;

/**
 *
 * @author konstakallama
 */
public class ScoreDao {

    private String fileName;
    private String sep = ",";
    private FileReader fr;
    private ArrayList<Score> scores;

    public ScoreDao(String fileName) {
        this.fileName = fileName;
        fr = new FileReader(fileName);
    }

    public ScoreDao() {
        this.fileName = "data/Scores.txt";
        this.fr = new FileReader(fileName);
        this.scores = this.getScores();
    }

    public ArrayList<Score> getScoresFromMemory() {
        return this.scores;
    }

    public ArrayList<Score> getScores() {
        ArrayList<Score> l = new ArrayList<>();
        l = readScores(fileName);

//        try {
//            l = readScores(fileName);
//        } catch (Exception e) {
//            try {
//                l = readScores("src/main/resources/" + fileName);
//            } catch (Exception ex) {
//            }
//        }
        if (l.isEmpty()) {
            FileReader fr2 = new FileReader("data/test.txt");
            String[] lines = fr2.readAllLines();
            for (int i = 1; i < lines.length; i++) {
                String[] s = lines[i].split(sep);
                l.add(new Score(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), s[4]));
            }
        }
        return l;
    }

    private ArrayList<Score> readScores(String fn) {
        ArrayList<Score> sl = new ArrayList<>();
        List<String> l = new ArrayList<>();
        try {
            l = Files.readAllLines(Paths.get("Scores.txt"));
            return readScores2(l);
        } catch (Exception e) {
            try {
                l = Files.readAllLines(Paths.get("data/Scores.txt"));
                return readScores2(l);
            } catch (IOException ex) {
            }
        }
        //List<String> l = Files.readAllLines(Paths.get(fn));
        String[] lines = fr.readAllLines();

        for (int i = 1; i < lines.length; i++) {
            String[] s = lines[i].split(sep);
            sl.add(new Score(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), s[4]));
        }
        return sl;
    }

    public void writeScore(Score s, boolean writeIfMissing) {
        this.scores.add(s);
        //System.out.println(s);
        PrintWriter pw = null;

        ArrayList<Score> l = this.scores;

        

//        String s = "";
//        try {
//            //InputStream inputStream = classLoader.getResourceAsStream(filename);
//        
//        
//        
//        ClassLoader cl = getClass().getClassLoader();
//        String fn = "data/Scores.txt";
//        
//            try {
//                //pw = new PrintWriter(new File(cl.getResource(fn).getPath()));
//                pw = new PrintWriter(cl.getResource(fn).getPath());
//                System.out.println("*");
//            } catch (Exception ex) {
//            }
//        } catch (Exception ex) {
//        }
//        try {
//            pw = new PrintWriter(fileName);
//        } catch (FileNotFoundException ex) {
//
//            try {
//                pw = new PrintWriter("src/main/resources/" + fileName);
//            } catch (FileNotFoundException e) {
//                try {
//                    pw = new PrintWriter("Scores.txt");
//                } catch (Exception e2) {
//
//                }
//            }
//        }

        File file1 = new File("Scores.txt");
        File file2 = new File("data/Scores.txt");
        
        if (!writeIfMissing) {
            if (!file1.exists()) {
                if (!file2.exists()) {
                    return;
                } else {
                    try {
                        pw = new PrintWriter("data/Scores.txt");
                    } catch (FileNotFoundException ex) {
                    }
                }
            } else {
                try {
                    pw = new PrintWriter("Scores.txt");
                } catch (FileNotFoundException ex) {
                }
            }
        } else {
            try {
                pw = new PrintWriter("Scores.txt");
            } catch (FileNotFoundException ex) {
            }
        }

        try {
            pw.println("Name" + sep + "Floor" + sep + "Turns" + sep + "Level" + sep + "Killed By");
            for (Score score : l) {
                pw.println(score.getName() + sep + score.getFloor() + sep + score.getTurn() + sep + score.getLevel() + sep + score.getKilledBy());
            }
            pw.close();
        } catch (Exception e) {
        }
    }

    private ArrayList<Score> readScores2(List<String> l) {
        ArrayList<Score> sl = new ArrayList<>();
        for (int i = 1; i < l.size(); i++) {
            String[] s = l.get(i).split(sep);
            sl.add(new Score(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), s[4]));
        }
        return sl;
    }

}
