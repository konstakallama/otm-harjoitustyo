/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.gamemanager;

import domain.support.Direction;

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
    
    public PlayerCommand(PlayerCommandType type) {
        this(type, Direction.NONE);
    }
    
    public PlayerCommand() {
        this(PlayerCommandType.COMMAND_NOT_FOUND);
    }
}
