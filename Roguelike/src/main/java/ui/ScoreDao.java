/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author konstakallama
 */
public class ScoreDao {

    String fileName;

    public ScoreDao(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<Score> getScores() {
        ArrayList<Score> l = new ArrayList<>();

        try {
            l = readScores(fileName);
        } catch (Exception e) {
            try {
                l = readScores("../" + fileName);
            } catch (Exception ex) {

            }
        }
        return l;
    }

    private ArrayList<Score> readScores(String filename) throws IOException {
        List<String> l = Files.readAllLines(Paths.get(filename));
        ArrayList<Score> sl = new ArrayList<>();

        for (int i = 1; i < l.size(); i++) {
            String line = l.get(i);
            String[] s = line.split("\t");
            sl.add(new Score(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), s[4]));
        }
        return sl;
    }

    public void writeScore(Score s) {
        PrintWriter pw = null;
        
        ArrayList<Score> l = this.getScores();
        
//        System.out.println("i");
        
        try {
            pw = new PrintWriter(fileName);
        } catch (FileNotFoundException ex) {
            
//            System.out.println("c1");
            
            try {
                pw = new PrintWriter("../" + fileName);
            } catch (FileNotFoundException e) {
//                System.out.println("c2");
            }
        }
        
        pw.println("Name\tFloor\tTurns\tLevel\tKilled By");
        
        l.add(s);
        
        for (Score score : l) {
//            System.out.println(score);
            pw.println(score.getName() + "\t" + score.getFloor() + "\t" + score.getTurn() + "\t" + score.getLevel() + "\t" + score.killedBy);
        }
        
        
        
        
        pw.close();
    }
}
