/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;
import java.util.Random;
/**
 *
 * @author konstakallama
 */
public class MapGenerator {
    Random random = new Random();
    Formulas f = new Formulas();

    public Map createTestMap(int w, int h, int floor) {
        Terrain[][] t = createTerrain(w, h);
        Map m = new Map(w, h, t, floor);
        this.addPotion(m);
        this.addStairs(m);
        
        return m;
    }
    
    public void addTestItem(Map m) {
        Location l = f.createRandomFreeLocation(m);
        
        MapItem item = new MapItem(l.getX(), l.getY(), m, "test item", true);
        
        m.addItem(l.getX(), l.getY(), item);
    }
    
    public void addPotion(Map m) {
        Location l = f.createRandomFreeLocation(m);
        
        MapItem item = new MapItem(l.getX(), l.getY(), m, "potion", true);
        
        m.addItem(l.getX(), l.getY(), item);
    }
    
    public void addStairs(Map m) {
        Location l = f.createRandomFreeLocation(m);
        m.setTerrain(l.getX(), l.getY(), Terrain.STAIRS);
    }

    public Terrain[][] createTerrain(int w, int h) {
        Terrain[][] t = new Terrain[w][h]; 
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (random.nextDouble() < 0.2) {
                    t[i][j] = Terrain.WALL;
                } else {
                    t[i][j] = Terrain.FLOOR;
                }
                
            }
        }
//        for (int i = 2; i < 50; i++) {
//            for (int j = 2; j < 30; j++) {
//                if (!(i ==20 && j == 20)) {
//                    t[i][j] = Terrain.FLOOR;
//                }
//                
//            }
//        }
        return t;
   }
    
    
    
}
