/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.gamemanager.AttackResult;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.GameManager;
import domain.gamemanager.PlayerCommand;
import domain.gamemanager.PlayerCommandType;
import domain.mapobject.Enemy;
import domain.mapobject.player.Player;
import domain.support.Location;
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
public class EnemyTests {
    private Player p;
    private GameManager gm;
    
    public EnemyTests() {
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
     public void standingPlayerDies() {
         for (int i = 0; i < 1000; i++) {
             gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT));
         }
         assertTrue(p.getStats().isDead());
     }
     
     @Test
     public void enemyFindsPlayer() {
         for (int i = 0; i < 1000; i++) {
             gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT));
         }
         
         boolean b = false;
         
         for (Location l : p.getLocation().getAdjacent()) {
             if (gm.getMap().hasEnemy(l.getX(), l.getY())) {
                 b = true;
             }
         }
         
         assertTrue(b);
     }
     
     @Test
     public void enemyAttacksAdjacentplayer() {
         for (int i = 0; i < 1000; i++) {
             gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT));
         }
         
         Enemy e = null;
         
         for (Location l : p.getLocation().getAdjacent()) {
             if (gm.getMap().hasEnemy(l.getX(), l.getY())) {
                 e = gm.getMap().getEnemy(l.getX(), l.getY());
             }
         }
         
         assertTrue(e != null);
         
         AttackResult a = e.takeTurn();
         
         assertTrue(a.getType() == AttackResultType.HIT || a.getType() == AttackResultType.MISS || a.getType() == AttackResultType.KILL);
     }
}
