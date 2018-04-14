/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author konstakallama
 */
public class Corridor {
    private Room from;
    private Room to;

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
    
}
