/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;
import java.util.Random;
/**
 *
 * @author konstakallama
 */
class MapGenerator {
    Random random = new Random();

    Map createMap(int w, int h) {
        Terrain[][] t = createTerrain(w, h);
        return new Map(w, h, t, 0);
    }

    private Terrain[][] createTerrain(int w, int h) {
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
