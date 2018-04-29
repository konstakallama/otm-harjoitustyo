/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.mapobject.Player.Player;
import domain.map.MapGenerator;
import domain.items.MapItem;
import domain.support.Direction;
import domain.gamemanager.GameManager;
import domain.gamemanager.PlayerCommand;
import domain.gamemanager.PlayerCommandType;
import domain.items.ItemDb;
import domain.support.Formulas;
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
    private Formulas f = new Formulas();
    private GameManager gm;
    
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
        gm = new GameManager();
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
     public void hpGainAtStatUp() {
         p.getStats().increaseCon();
         assertEquals(f.getPlayerMaxHP(p.getStats().getCon()), p.getCurrentHP());
     }
     
     @Test
     public void standingPlayerDies() {
         for (int i = 0; i < 1000; i++) {
             gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT));
         }
         assertTrue(p.getStats().isDead());
     }
     
     
}
