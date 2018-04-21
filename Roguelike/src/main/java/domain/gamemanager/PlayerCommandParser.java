/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.gamemanager;

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
    KeyCode wait = KeyCode.Z;
    KeyCode yes = KeyCode.ENTER;
    KeyCode inventory = KeyCode.I;
    KeyCode close = KeyCode.ESCAPE;
    KeyCode menu = KeyCode.M;
    KeyCode pickUp = KeyCode.P;

    public PlayerCommand parseCommand(KeyCode k, GameManager gm) {
        if (k == this.moveDown || k == this.moveUp || k == this.moveLeft || k == this.moveRight) {
            return this.checkMove(k);
        } else if (k == this.wait) {
            return new PlayerCommand(PlayerCommandType.WAIT);
        } else if (gm.getMap().playerIsOnStairs()) {
            return this.checkOnStairs(k);
        } else if (k == this.inventory) {
            return new PlayerCommand(PlayerCommandType.INVENTORY);
        } else if (k == this.close) {
            return new PlayerCommand(PlayerCommandType.CLOSE);
        } else if (k == this.menu) {
            return new PlayerCommand(PlayerCommandType.MENU);
        } else if (k == this.pickUp) {
            return new PlayerCommand(PlayerCommandType.PICK_UP);
        }
        return new PlayerCommand(PlayerCommandType.COMMAND_NOT_FOUND);      
    }
    
    private PlayerCommand checkMove(KeyCode k) {
        if (k == this.moveDown) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.DOWN);
        } else if (k == this.moveUp) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.UP);
        } else if (k == this.moveLeft) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.LEFT);
        } else if (k == this.moveRight) {
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

    private PlayerCommand checkOnStairs(KeyCode k) {
        if (k == this.yes) {
                return new PlayerCommand(PlayerCommandType.NEXT_FLOOR);
            } else {
                return new PlayerCommand(PlayerCommandType.WAIT);
            }
    }
}
