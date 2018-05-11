/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.map;

import domain.support.Location;
import java.util.Objects;

/**
 * A corridor on the map connecting 2 rooms. Used internally for map generation purposes.
 * @author konstakallama
 */
public class Corridor {
    private Room from;
    private Room to;
    private Location start;
    private Location turn;
    private Location end;

    public Corridor(Room from, Room to) {
        this.from = from;
        this.to = to;
    }

    public Room getFrom() {
        return from;
    }

    public void setFrom(Room from) {
        this.from = from;
    }

    public Room getTo() {
        return to;
    }

    public void setTo(Room to) {
        this.to = to;
    }
    
    public boolean connects(Room r1, Room r2) {
        return (this.from.equals(r1) && this.to.equals(r2)) || (this.from.equals(r2) && this.to.equals(r1));
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getTurn() {
        return turn;
    }

    public void setTurn(Location turn) {
        this.turn = turn;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Corridor other = (Corridor) obj;
        if (this.from.equals(other.getFrom()) && this.to.equals(other.getTo())) {
            return true;
        }
        return (this.from.equals(other.getTo()) && this.to.equals(other.getFrom()));
    }

    @Override
    public String toString() {
        return "Corridor{" + "from=" + from + ", to=" + to + ", start=" + start + ", end=" + end + '}';
    }
    
}
