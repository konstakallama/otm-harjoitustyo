/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.gamemanager.AttackResult;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.CommandResult;
import domain.mapobject.player.Player;
import domain.map.MapGenerator;
import domain.items.MapItem;
import domain.support.Direction;
import domain.gamemanager.GameManager;
import domain.gamemanager.PlayerCommand;
import domain.gamemanager.PlayerCommandType;
import domain.items.ItemDb;
import domain.items.effects.Heal;
import domain.items.effects.MagicDamage;
import domain.items.effects.StaminaHeal;
import domain.items.effects.TeachSpell;
import domain.map.Map;
import domain.mapobject.Enemy;
import domain.mapobject.player.Spell;
import domain.support.Formulas;
import domain.support.Location;
import java.util.Random;
import javafx.scene.input.KeyCode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ui.PlayerCommandParser;

/**
 *
 * @author konstakallama
 */
public class Tests {
    private Player p;
    private Random r = new Random();
    private Formulas f = new Formulas();
    private GameManager gm;
    
    public Tests() {
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
     
     @Test
     public void enemyFindsPlayer() {
         for (int i = 0; i < 500; i++) {
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
     public void playerCommandParserTest() {
         PlayerCommandParser pc = new PlayerCommandParser();
         pc.setMoveDown(KeyCode.K);
         PlayerCommand c = pc.parseCommand(KeyCode.K, gm);
         assertEquals(c.getDirection(), Direction.DOWN);
         assertEquals(c.getType(), PlayerCommandType.MOVE);
     }
     
     @Test
     public void enemyAttacksAdjacentplayer() {
         for (int i = 0; i < 500; i++) {
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
     
     @Test
     public void healHeals() {
         Heal h = new Heal(5, "heal");
         p.getStats().takeDamage(5);
         
         assertTrue(h.applyEffectToPlayer(p).isSuccess());
         assertEquals(p.getStats().getMaxHP(), p.getCurrentHP());
     }
     
     @Test
     public void healDoesntHealUnharmed() {
         Heal h = new Heal(5, "heal");
         
         assertFalse(h.applyEffectToPlayer(p).isSuccess());
         assertEquals(p.getStats().getMaxHP(), p.getCurrentHP());
     }
     
     @Test
     public void magicDamageTest() {
         MagicDamage m = new MagicDamage(4, p.getStats(), "Fire");
         p.getStats().setInt(100);
         Enemy e = gm.createTestEnemy(0, 0);
         CommandResult cr = m.applyEffectToEnemy(e);
         
         assertTrue(cr.isSuccess());
         assertTrue(cr.getAttackResult().isKill());
         assertTrue(e.getStats().isDead());
     }
     
     @Test
     public void staminaHeal() {
         StaminaHeal h = new StaminaHeal(50, "sheal");
         p.getStats().setStaminaDmg(51);
         
         assertTrue(h.applyEffectToPlayer(p).isSuccess());
         assertEquals(p.getStats().getMaxStamina() - 1, p.getStats().getCurrentStamina());
         
         assertTrue(h.applyEffectToPlayer(p).isSuccess());
         assertEquals(p.getStats().getMaxStamina(), p.getStats().getCurrentStamina());
         
         assertFalse(h.applyEffectToPlayer(p).isSuccess());
         assertEquals(p.getStats().getMaxStamina(), p.getStats().getCurrentStamina());
     }
     
     @Test
     public void teachSpell() {
         TeachSpell t = new TeachSpell("fire tome", "Fire");
         assertTrue(t.applyEffectToPlayer(p).isSuccess());
         
         Spell s = p.getStats().getSpells().get(0);
         
         assertEquals("Fire", s.getName());
         assertTrue(s.canBeUsed());
         
         s.setTurnsInCooldown(2);        
         assertFalse(s.canBeUsed());
         
         s.advanceCooldown();
         assertFalse(s.canBeUsed());
         
         s.advanceCooldown();
         assertTrue(s.canBeUsed());
         
         Enemy e = gm.createTestEnemy(0, 0);
         s.useOnEnemy(e);
         
         assertFalse(s.canBeUsed());
     }
     
     @Test
     public void playerStartsInRoom() {
         assertTrue(gm.getMap().isInsideRoom(p.getLocation()));
     }
     
     @Test
     public void getPlayerRoomTest() {
         assertEquals(gm.getMap().insideWhichRoom(p.getLocation()), gm.getMap().getPlayerRoom());
     }
     
     @Test
     public void fullInventoryDoesntPickUp() {
         for (int i = 0; i < p.getInventory().getSize(); i++) {
             p.pickUp(new MapItem(0, 0, gm.getMap(), "potion", true));
         }
         assertFalse(p.pickUp(new MapItem(0, 0, gm.getMap(), "potion", true)));
     }
     
     
}
