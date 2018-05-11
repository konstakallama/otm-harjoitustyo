/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject;

import domain.map.Map;

/**
 *
 * @author konstakallama
 */
public abstract class Moves extends MapObject {

    public Moves(Map map, int x, int y, String name) {
        super(x, y, map, name);
    }

    @Override
    public boolean hasHP() {
        return true;
    }

    @Override
    public boolean isOccupied() {
        return true;
    }

}
