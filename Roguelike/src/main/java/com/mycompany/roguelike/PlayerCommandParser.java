/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

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
    KeyCode wait = KeyCode.SPACE;
    KeyCode yes = KeyCode.ENTER;
    
    
    public PlayerCommand parseCommand(KeyCode k, GameManager gm) {
        if (k == this.moveDown) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.DOWN);
        } else if (k == this.moveUp) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.UP);
        } else if (k == this.moveLeft) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.LEFT);
        } else if (k == this.moveRight) {
            return new PlayerCommand(PlayerCommandType.MOVE, Direction.RIGHT);
        } else if (k == this.wait) {
            return new PlayerCommand(PlayerCommandType.WAIT, Direction.NONE);
        } else if (gm.getMap().playerIsOnStairs()) {
            if (k == this.yes) {
                return new PlayerCommand(PlayerCommandType.NEXT_FLOOR, Direction.NONE);
            } else {
                return new PlayerCommand(PlayerCommandType.WAIT, Direction.NONE);
            }
        } else {
            return new PlayerCommand(PlayerCommandType.COMMAND_NOT_FOUND, Direction.NONE);
        }
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
}
