/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.gamemanager.GameManager;
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
public class MapTests {
    private Player p;
    private GameManager gm;
    
    public MapTests() {
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
     public void playerStartsInRoom() {
         assertTrue(gm.getMap().isInsideRoom(p.getLocation()));
     }
     
     @Test
     public void getPlayerRoomTest() {
         assertEquals(gm.getMap().insideWhichRoom(p.getLocation()), gm.getMap().getPlayerRoom());
     }
}
