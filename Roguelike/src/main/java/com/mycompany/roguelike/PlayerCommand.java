/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

/**
 *
 * @author konstakallama
 */
public class PlayerCommand {
    private PlayerCommandType type;
    private Direction d;

    public PlayerCommandType getType() {
        return type;
    }

    public Direction getDirection() {
        return d;
    }

    public PlayerCommand(PlayerCommandType type, Direction d) {
        this.type = type;
        this.d = d;
    }
}
