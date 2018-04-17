/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.map;

import domain.support.Location;
import java.util.ArrayList;

/**
 *
 * @author konstakallama
 */
public class Room {
    
    private Location location;
    private int w;
    private int h;
    private ArrayList<Corridor> corridors;
    private ArrayList<Room> connected;

    public Room(Location location, int w, int h) {
        this.location = location;
        this.w = w;
        this.h = h;
        this.corridors = new ArrayList<>();
        this.connected = new ArrayList<>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public ArrayList<Corridor> getCorridors() {
        return corridors;
    }
    
    public void addCoridor(Corridor c) {
        this.corridors.add(c);
        if (c.getFrom().equals(this)) {
            this.connected.add(c.getTo());
            c.getTo().getConnected().add(this);
        } else {
            this.connected.add(c.getFrom());
            c.getFrom().getConnected().add(this);
        }
    }

    public ArrayList<Room> getConnected() {
        return connected;
    }
    
    public boolean isDirectlyConnected(Room r) {
        if (r.equals(this)) {
            return true;
        }
        return this.connected.contains(r);
    }
    
    public Location getMiddle() {
        return new Location(this.getLocation().getX() + (this.getW() / 2), this.getLocation().getY() + (this.getH() / 2));
    }
    
    public boolean isInside(Location l) {
        return (l.getX() >= this.location.getX() && l.getX() < this.location.getX() + w && l.getY() >= this.location.getY() && l.getY() < this.location.getY() + h);
    }
    
    public Location getNE() {
        return new Location(this.location.getX() + w - 1, this.location.getY());
    }
    
    public Location getSW() {
        return new Location(this.location.getX(), this.location.getY() + h - 1);
    }
    
    public Location getSE() {
        return new Location(this.location.getX() + w - 1, this.location.getY() + h - 1);
    }
    
    public Location getNW() {
        return this.location;
    }

}
