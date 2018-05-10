/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.gamemanager.CommandResult;
import domain.gamemanager.GameManager;
import domain.items.effects.Heal;
import domain.items.effects.MagicDamage;
import domain.items.effects.StaminaHeal;
import domain.items.effects.TeachSpell;
import domain.mapobject.Enemy;
import domain.mapobject.player.Player;
import domain.mapobject.player.Spell;
import domain.support.Direction;
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
public class EffectTests {
    private Player p;
    private GameManager gm;
    
    public EffectTests() {
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
         
         assertTrue(s.inRange(p.getLocation(), p.getLocation().locInDir(Direction.UP)));
         
         Location outOfRange = new Location(p.getLocation().getX() + s.getRange() + 1, p.getLocation().getY());
         
         assertFalse(s.inRange(p.getLocation(), outOfRange));
     }
}
