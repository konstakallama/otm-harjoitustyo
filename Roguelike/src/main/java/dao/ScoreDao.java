/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    public ScoreDao(String fileName) {
        this.fileName = fileName;
    }


    public ArrayList<Score> getScores() {
        ArrayList<Score> l = new ArrayList<>();

        try {
            l = readScores(fileName);
        } catch (Exception e) {
            System.out.println("e1");
            System.out.println(fileName);
            try {
                l = readScores("src/main/resources/" + fileName);
            } catch (Exception ex) {
                System.out.println("e2");
                System.out.println("../" + fileName);
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

        try {
            pw = new PrintWriter(fileName);
        } catch (FileNotFoundException ex) {

            try {
                pw = new PrintWriter("src/main/resources/" + fileName);
            } catch (FileNotFoundException e) {
            }
        }

        pw.println("Name\tFloor\tTurns\tLevel\tKilled By");

        l.add(s);

        for (Score score : l) {
            pw.println(score.getName() + "\t" + score.getFloor() + "\t" + score.getTurn() + "\t" + score.getLevel() + "\t" + score.getKilledBy());
        }

        pw.close();
    }

}
