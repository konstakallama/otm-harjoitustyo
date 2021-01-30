/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.google.common.io.CharStreams;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 * @author konstakallama
 */
public class FileReader {

    private String[] lines;
    private String sep = ",";

    public FileReader() {
    }

    public FileReader(String filename) {
        this.lines = this.readFile(filename).split("\n");
    }

    /**
     * Read a resource file found at src/main/resources/filename. Returns the
     * content of the file as a single String.
     *
     */
    public String readFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        String s = "";
        try {
            InputStream inputStream = classLoader.getResourceAsStream(filename);
            try (Reader reader = new InputStreamReader(inputStream)) {
                s = CharStreams.toString(reader);
            }
        } catch (Exception e) {
        }
        return s;
    }

    public String readFromIndex(String name, int index) {
        for (String line : this.lines) {
            String[] s = line.split(sep);
            if (s[0].equals(name)) {
                return (s[index]);
            }
        }
        throw new UnsupportedOperationException("Line with name " + name + " not found");
    }

    public String[] readLineByName(String name) {
        for (String line : this.lines) {
            String[] s = line.split(sep);
            if (s[0].equals(name)) {
                return s;
            }
        }
        return null;
    }

    public String[] readAllLines() {
        return this.lines;
    }

    public String readTest() {
        //ClassLoader classLoader = getClass().getClassLoader();
        //File file = new File(classLoader.getResource("data/test.txt").getFile());
        String content = "fail";
//        try {
//            content = new String(Files.readAllBytes(file.toPath()));
//        } catch (IOException ex) {
//        }
//        String fn = "data/Enemies.txt";
//        int row = 8;
//        int col = 13;
//        try {
//            //content = this.readFile(fn).split("\n")[row].split(sep)[col];
//            content = this.readFromIndex("monolith", 0);
//            //InputStream inputStream = classLoader.getResourceAsStream("data/Enemies.txt");
//            //try (Reader reader = new InputStreamReader(inputStream)) {
//            //    content = CharStreams.toString(reader).split("\n")[2].split(sep)[3];
//            //}
//            //String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//        } catch (Exception e) {
//        }

        try {
            File file = new File("Scores.txt");
            if (file.exists()) {
                content = "haha...";
            }
        } catch (Exception e) {
            
        }

        return content;
    }
}
