/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.gamemanager.GameManager;
import domain.gamemanager.PlayerCommand;
import domain.gamemanager.PlayerCommandType;
import domain.support.Direction;
import javafx.scene.input.KeyCode;

/**
 *
 * @author konstakallama
 */
public class PlayerCommandParser {

    KeyCode moveUp = KeyCode.UP;
    KeyCode moveDown = KeyCode.DOWN;
    KeyCode moveRight = KeyCode.RIGHT;
    KeyCode moveLeft = KeyCode.LEFT;
    
    KeyCode moveUp2 = KeyCode.W;
    KeyCode moveDown2 = KeyCode.S;
    KeyCode moveRight2 = KeyCode.D;
    KeyCode moveLeft2 = KeyCode.A;
      
    KeyCode wait = KeyCode.SHIFT;
    KeyCode wait2 = KeyCode.Z;
    
    KeyCode yes = KeyCode.ENTER;
    KeyCode yes2 = KeyCode.E;
    
    KeyCode inventory = KeyCode.TAB;
    KeyCode inventory2 = KeyCode.I;
    
    KeyCode close = KeyCode.ESCAPE;
    KeyCode menu = KeyCode.M;
    KeyCode pickUp = KeyCode.P;
    KeyCode discard = KeyCode.Q;

    public PlayerCommand parseCommand(KeyCode k, GameManager gm) {
        if (k == this.moveDown || k == this.moveUp || k == this.moveLeft || k == this.moveRight || k == this.moveDown2 || k == this.moveUp2 || k == this.moveLeft2 || k == this.moveRight2) {
            return this.checkMove(k);
        } else if (k == this.wait || k == this.wait2) {
            return new PlayerCommand(PlayerCommandType.WAIT);
        } else if (gm.getMap().playerIsOnStairs()) {
            return this.checkOnStairs(k);
        } else if (k == this.inventory || k == this.inventory2) {
            return new PlayerCommand(PlayerCommandType.INVENTORY);
        } else if (k == this.close) {
            return new PlayerCommand(PlayerCommandType.CLOSE);
        } else if (k == this.menu) {
            return new PlayerCommand(PlayerCommandType.MENU);
        } else if (k == this.pickUp) {
            return new PlayerCommand(PlayerCommandType.PICK_UP);
        } else if (k == this.discard) {
            return new PlayerCommand(PlayerCommandType.DISCARD);
        }
        return new PlayerCommand(PlayerCommandType.COMMAND_NOT_FOUND);
    }

    private PlayerCommand checkMove(KeyCode k) {
        if (k == this.moveDown || k == this.moveDown2) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.DOWN);
        } else if (k == this.moveUp || k == this.moveUp2) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.UP);
        } else if (k == this.moveLeft || k == this.moveLeft2) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.LEFT);
        } else if (k == this.moveRight || k == this.moveRight2) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.RIGHT);
        }
        return new PlayerCommand();
    }

    public KeyCode getMoveUp() {
        return moveUp;
    }

    public void setMoveUp(KeyCode moveUp) {
        this.moveUp = moveUp;
    }

    public KeyCode getMoveDown() {
        return moveDown;
    }

    public void setMoveDown(KeyCode moveDown) {
        this.moveDown = moveDown;
    }

    public KeyCode getMoveRight() {
        return moveRight;
    }

    public void setMoveRight(KeyCode moveRight) {
        this.moveRight = moveRight;
    }

    public KeyCode getMoveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(KeyCode moveLeft) {
        this.moveLeft = moveLeft;
    }

    public KeyCode getWait() {
        return wait;
    }

    public void setWait(KeyCode wait) {
        this.wait = wait;
    }

    public KeyCode getYes() {
        return yes;
    }

    public void setYes(KeyCode yes) {
        this.yes = yes;
    }

    public KeyCode getInventory() {
        return inventory;
    }

    public void setInventory(KeyCode inventory) {
        this.inventory = inventory;
    }

    public KeyCode getMoveUp2() {
        return moveUp2;
    }

    public void setMoveUp2(KeyCode moveUp2) {
        this.moveUp2 = moveUp2;
    }

    public KeyCode getMoveDown2() {
        return moveDown2;
    }

    public void setMoveDown2(KeyCode moveDown2) {
        this.moveDown2 = moveDown2;
    }

    public KeyCode getMoveRight2() {
        return moveRight2;
    }

    public void setMoveRight2(KeyCode moveRight2) {
        this.moveRight2 = moveRight2;
    }

    public KeyCode getMoveLeft2() {
        return moveLeft2;
    }

    public void setMoveLeft2(KeyCode moveLeft2) {
        this.moveLeft2 = moveLeft2;
    }

    public KeyCode getClose() {
        return close;
    }

    public void setClose(KeyCode close) {
        this.close = close;
    }

    public KeyCode getMenu() {
        return menu;
    }

    public void setMenu(KeyCode menu) {
        this.menu = menu;
    }

    public KeyCode getPickUp() {
        return pickUp;
    }

    public void setPickUp(KeyCode pickUp) {
        this.pickUp = pickUp;
    }

    public KeyCode getWait2() {
        return wait2;
    }

    public void setWait2(KeyCode wait2) {
        this.wait2 = wait2;
    }

    public KeyCode getYes2() {
        return yes2;
    }

    public void setYes2(KeyCode yes2) {
        this.yes2 = yes2;
    }

    public KeyCode getInventory2() {
        return inventory2;
    }

    public void setInventory2(KeyCode inventory2) {
        this.inventory2 = inventory2;
    }

    public KeyCode getDiscard() {
        return discard;
    }

    public void setDiscard(KeyCode discard) {
        this.discard = discard;
    }

    private PlayerCommand checkOnStairs(KeyCode k) {
        if (k == this.yes || k == this.yes2) {
            return new PlayerCommand(PlayerCommandType.NEXT_FLOOR);
        } else {
            return new PlayerCommand(PlayerCommandType.COMMAND_NOT_FOUND);
        }
    }
}
