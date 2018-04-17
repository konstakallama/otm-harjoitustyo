/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.support;

/**
 *
 * @author konstakallama
 */
public enum Direction {
    UP, DOWN, RIGHT, LEFT, NONE;
    
    public int xVal() {
        if (this == Direction.UP) {
            return 0;
        } else if (this == Direction.DOWN) {
            return 0;
        } else if (this == Direction.RIGHT) {
            return 1;
        } else if (this == Direction.LEFT) {
            return -1;
        } else {
            return 0;
        }
    }
    
    public int yVal() {
        if (this == Direction.UP) {
            return -1;
        } else if (this == Direction.DOWN) {
            return 1;
        } else if (this == Direction.RIGHT) {
            return 0;
        } else {
            return 0;
        }
    }
    
    public Direction getOpposite() {
        if (this == Direction.UP) {
            return Direction.DOWN;
        } else if (this == Direction.DOWN) {
            return Direction.UP;
        } else if (this == Direction.RIGHT) {
            return Direction.LEFT;
        } else if (this == Direction.LEFT){
            return Direction.RIGHT;
        } else {
            return Direction.NONE;
        }
    }
    
    public Direction getClockwiseTurn() {
        if (this == Direction.UP) {
            return Direction.RIGHT;
        } else if (this == Direction.DOWN) {
            return Direction.LEFT;
        } else if (this == Direction.RIGHT) {
            return Direction.DOWN;
        } else if (this == Direction.LEFT){
            return Direction.UP;
        } else {
            return Direction.NONE;
        }
    }
    
    public Direction getCounterclockwiseTurn() {
        if (this == Direction.UP) {
            return Direction.LEFT;
        } else if (this == Direction.DOWN) {
            return Direction.RIGHT;
        } else if (this == Direction.RIGHT) {
            return Direction.UP;
        } else if (this == Direction.LEFT){
            return Direction.DOWN;
        } else {
            return Direction.NONE;
        }
    }
    
}
