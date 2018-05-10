/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import domain.gamemanager.AttackResult;
import domain.gamemanager.AttackResultType;
import domain.mapobject.player.Player;
import domain.support.Direction;
import domain.gamemanager.GameManager;
import domain.gamemanager.PlayerCommand;
import domain.gamemanager.PlayerCommandType;
import domain.items.ItemDb;
import domain.mapobject.Enemy;
import domain.mapobject.EnemyStats;
import domain.mapobject.EnemyType;
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
public class PlayerTests {

    private Player p;
    private Random r = new Random();
    private Formulas f = new Formulas();
    private GameManager gm;

    public PlayerTests() {
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
    public void hpGainAtStatUp() {
        p.getStats().increaseCon();
        assertEquals(f.getPlayerMaxHP(p.getStats().getCon()), p.getCurrentHP());
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
    public void palyerAttckTest() {
        ItemDb itemDb = new ItemDb();
        Direction ed = Direction.NONE;
        Direction d = Direction.UP;
        Enemy e = null;

        for (int i = 0; i < 4; i++) {
            Location el = p.getLocation().locInDir(d);
            if (!gm.getMap().isOccupied(el.getX(), el.getY())) {
                e = new Enemy(el.getX(), el.getY(), gm.getMap(), new EnemyStats(1, new EnemyType("test enemy"), itemDb.createEnemyTestWeapon(), itemDb.createEnemyTestArmor()), true);
                gm.getMap().addEnemy(el.getX(), el.getY(), e);
                ed = d;
                break;
            }
            d = d.getClockwiseTurn();
        }

        assertFalse(ed == Direction.NONE);
        p.getStats().setDex(100);
        AttackResult ar = p.attack(ed);

        assertEquals(ar.getAttacker(), p);
        assertEquals(ar.getTarget(), e);
        assertTrue(ar.getType() == AttackResultType.HIT);
        assertFalse(ar.isKill());
        assertFalse(ar.isLevelUp());
        assertTrue(ar.getDamageDealt() > 0);
        assertEquals(ar.getExpGained(), 0);
        assertEquals(1.0, ar.getToHit(), 0.0001);

        p.getStats().setStr(100);
        ar = p.attack(ed);

        assertTrue(ar.getType() == AttackResultType.KILL);
        assertTrue(ar.isKill());
        assertTrue(ar.getDamageDealt() > 0);
        assertTrue(ar.getExpGained() > 0);

        ar = p.attack(ed);

        assertTrue(ar.getType() == AttackResultType.FAIL);
    }

}
