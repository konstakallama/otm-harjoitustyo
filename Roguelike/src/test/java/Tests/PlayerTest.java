/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.*;
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
         p.pickUp(new MapItem(0, 0, m.createTestMap(50, 50, 0), "test item", true));
         assertEquals(p.getInventory().getItems().get(0).getName(), "test item");
     }
}
