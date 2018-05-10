/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.gamemanager.GameManager;
import domain.items.ItemDb;
import domain.items.MapItem;
import domain.map.MapGenerator;
import domain.mapobject.player.Player;
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
public class ItemTests {
    private Player p;
    private GameManager gm;
    
    public ItemTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        gm = new GameManager();
        p = gm.getPlayer();
    }
    
    @After
    public void tearDown() {
    }

    @Test
     public void itemPickUp() {
         MapGenerator m = new MapGenerator();
         p.pickUp(new MapItem(0, 0, m.createTestMap(50, 50, 0), "potion", true));
         assertEquals(p.getInventory().getItems().get(0).getName(), "potion");
     }
     
     @Test
     public void equipTest() {
         ItemDb d = new ItemDb();
         MapItem i = new MapItem(0, 0, p.getMap(), "atma weapon", true);
         String oldW = p.getStats().getWeapon().getName();
         try {
             p.equip(d.itemConverter(i));
         } catch (Exception e) {
             assertEquals(true, false);
         }
         assertEquals("atma weapon", p.getStats().getWeapon().getName());
         assertEquals(oldW, p.getInventory().getItems().get(0).getName());
     }
     
     @Test
     public void armorEquipTestFullInventory() {
         for (int i = 0; i < p.getInventory().getSize() - 1; i++) {
             p.pickUp(new MapItem(0, 0, gm.getMap(), "potion", true));
         }
         
         MapItem i = new MapItem(0, 0, p.getMap(), "über armor", true);
         p.pickUp(i);
         String oldA = p.getStats().getArmor().getName();
         try {
             p.equip(p.getInventory().getItems().get(p.getInventory().getSize() - 1));
         } catch (Exception e) {
             assertEquals(true, false);
         }
         assertEquals("über armor", p.getStats().getArmor().getName());
         
         p.getInventory().sort();
         
         assertEquals(oldA, p.getInventory().getItems().get(0).getName());
     }
     
     @Test
     public void fullInventoryDoesntPickUp() {
         for (int i = 0; i < p.getInventory().getSize(); i++) {
             p.pickUp(new MapItem(0, 0, gm.getMap(), "potion", true));
         }
         assertFalse(p.pickUp(new MapItem(0, 0, gm.getMap(), "potion", true)));
     }
}
