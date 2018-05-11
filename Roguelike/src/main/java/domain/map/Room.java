/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.map;

import domain.support.Direction;
import domain.support.Location;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A room on a map.
 * @author konstakallama
 */
public class Room {
    
    private Location location;
    private int w;
    private int h;
    private ArrayList<Corridor> corridors;
    private ArrayList<Corridor> arrivingCorridors;
    private ArrayList<Room> connected;

    public Room(Location location, int w, int h) {
        this.location = location;
        this.w = w;
        this.h = h;
        this.corridors = new ArrayList<>();
        this.arrivingCorridors = new ArrayList<>();
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
    /**
     * Adds a corridor to this room and an arriving corridor to the room said corridor connects to.
     * @param c 
     */
    public void addCoridor(Corridor c) {
        this.corridors.add(c);
        if (c.getFrom().equals(this)) {
            this.connected.add(c.getTo());
            c.getTo().getConnected().add(this);
            c.getTo().addArrivingCoridor(c);
        } else {
            this.connected.add(c.getFrom());
            c.getFrom().getConnected().add(this);
        }
    }

    public ArrayList<Room> getConnected() {
        return connected;
    }
    /**
     * Returns true if the given room is reachable from this room using this room's corridors. Used in map generation.
     * @param r
     * @return 
     */
    public boolean isDirectlyConnected(Room r) {
        if (r.equals(this)) {
            return true;
        }
        return this.connected.contains(r);
    }
    /**
     * Returns an approximate middle point for this room.
     * @return 
     */
    public Location getMiddle() {
        return new Location(this.getLocation().getX() + (this.getW() / 2), this.getLocation().getY() + (this.getH() / 2));
    }
    /**
     * Returns true if l is inside this room.
     * @param l
     * @return 
     */
    public boolean isInside(Location l) {
        if (this.h == 0 || this.w == 0) {
            return false;
        }
        return (l.getX() >= this.location.getX() && l.getX() < this.location.getX() + w && l.getY() >= this.location.getY() && l.getY() < this.location.getY() + h);
    }
    /**
     * Returns the northeast corner of this room.
     * @return 
     */
    public Location getNE() {
        return new Location(this.location.getX() + w - 1, this.location.getY());
    }
    /**
     * Returns the southwest corner of this room.
     * @return 
     */
    public Location getSW() {
        return new Location(this.location.getX(), this.location.getY() + h - 1);
    }
    /**
     * Returns the southeast corner of this room.
     * @return 
     */
    public Location getSE() {
        return new Location(this.location.getX() + w - 1, this.location.getY() + h - 1);
    }
    /**
     * Returns the northwest corner of this room.
     * @return 
     */
    public Location getNW() {
        return this.location;
    }
    /**
     * Adds c corridor to the list of corridors arriving in this room, if it is not already present.
     * @param c 
     */
    private void addArrivingCoridor(Corridor c) {
        if (!this.arrivingCorridors.contains(c)) {
            this.arrivingCorridors.add(c);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.location);
        hash = 11 * hash + this.w;
        hash = 11 * hash + this.h;
        hash = 11 * hash + Objects.hashCode(this.corridors);
        hash = 11 * hash + Objects.hashCode(this.connected);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.w != other.w) {
            return false;
        }
        if (this.h != other.h) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        return true;
    }
    /**
     * Returns the starting locations for all corridors leaving from and arriving to this room.
     * @return 
     */
    public ArrayList<Location> getCorridorStarts() {
        ArrayList<Location> starts = new ArrayList<>();
                
        for (Corridor c : this.corridors) {
            if (this.isNextTo(c.getStart())) {
                starts.add(c.getStart());
            } else if (this.isNextTo(c.getEnd())) {
                starts.add(c.getEnd());
            }
        }
        for (Corridor c : this.arrivingCorridors) {
            if (this.isNextTo(c.getStart())) {
                starts.add(c.getStart());
            } else if (this.isNextTo(c.getEnd())) {
                starts.add(c.getEnd());
            }
        }
        return starts;
    }

    @Override
    public String toString() {
        return "Room{" + "location=" + location + ", w=" + w + ", h=" + h + '}';
    }
    /**
     * Returns true if any location at manhattan distance 1 from l is inside this room.
     * @param l
     * @return 
     */
    public boolean isNextTo(Location l) {
        
        
        Direction d = Direction.DOWN;
        for (int i = 0; i < 4; i++) {
            if (this.isInside(l.locInDir(d))) {
                return true;
            }
            d = d.getClockwiseTurn();
        }
        return false;
    }
    /**
     * Removes the specified corridor from the list of corridors for this room and the list of arriving corridors for the target room.
     * @param c 
     */
    public void removeCorridor(Corridor c) {
        Room r = c.getTo();
        r.getArrivingCorridors().remove(c);
        this.corridors.remove(c);
    }



    public ArrayList<Corridor> getArrivingCorridors() {
        return arrivingCorridors;
    }
}
