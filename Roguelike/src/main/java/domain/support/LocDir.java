/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.support;

import domain.support.Direction;
import domain.support.Location;

/**
 *
 * @author konstakallama
 */
public class LocDir {
    private Location l;
    private Direction d;

    public LocDir(Location l, Direction d) {
        this.l = l;
        this.d = d;
    }

    public Location getL() {
        return l;
    }

    public Direction getD() {
        return d;
    }
    
}
