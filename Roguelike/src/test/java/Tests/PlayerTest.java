/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.*;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author konstakallama
 */
public class PlayerTest {
    private Player p;
    private Random r = new Random();
    
    public PlayerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        GameManager gm = new GameManager();
        p = gm.getPlayer();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void InitialHpIsMaxHP() {
         assertEquals(p.getCurrentHP(), p.getMaxHP());
     }
     
     @Test
     public void takeDamageWorks() {
         int oldMaxHp = p.getMaxHP();
         p.getStats().takeDamage(1);
         assertEquals(p.getCurrentHP(), p.getMaxHP() - 1);
         assertEquals(p.getMaxHP(), oldMaxHp);
     }
     
     @Test
     public void playerDies() {
         p.getStats().takeDamage(p.getMaxHP());
         assertTrue(p.getStats().isDead());
     }
     
     @Test
     public void itemPickUp() {
         MapGenerator m = new MapGenerator();
         p.pickUp(new MapItem(0, 0, m.createTestMap(50, 50, 0), "potion", true));
         assertEquals(p.getInventory().getItems().get(0).getName(), "potion");
     }
     
     @Test
     public void expGainWorks() {
         int oldLevel = p.getStats().getLevel();
         p.getStats().gainExp(p.getStats().expToNextLevel() - 1);
         assertEquals(oldLevel, p.getStats().getLevel());
         p.getStats().gainExp(p.getStats().expToNextLevel());
         assertEquals(oldLevel + 1, p.getStats().getLevel());
     }
     
     @Test
     public void moveTest() {
         for (int i = 0; i < 1000; i++) {
             int x = r.nextInt(4);
             Direction d;
             
             switch (x) {
                 case 0:
                     d = Direction.DOWN;
                     break;
                 case 1:
                     d = Direction.UP;
                     break;
                 case 2:
                     d = Direction.LEFT;
                     break;
                 default:
                     d = Direction.RIGHT;
                     break;
             }
             
             int oldX = p.getX();
             int oldY = p.getY();
             boolean isOccupied = p.getMap().isOccupied(oldX + d.xVal(), oldY + d.yVal());
             p.move(d);
             
             if (isOccupied) {
                 assertEquals(p.getX(), oldX);
                 assertEquals(p.getY(), oldY);
             } else {
                 assertEquals(p.getX(), oldX + d.xVal());
                 assertEquals(p.getY(), oldY + d.yVal());
             }
         }
     }
     
     
}
