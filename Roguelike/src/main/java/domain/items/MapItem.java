/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.items;

import domain.map.Map;
import domain.mapobject.MapObject;

/**
 *
 * @author konstakallama
 */
public class MapItem extends MapObject {
    private boolean visible;
    
    public MapItem(int x, int y, Map map, String name, boolean visible) {
        super(x, y, map, name);
        this.visible = visible;
    }

    @Override
    public boolean isOccupied() {
        return false;
    }

    @Override
    public boolean hasHP() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    
    
}
