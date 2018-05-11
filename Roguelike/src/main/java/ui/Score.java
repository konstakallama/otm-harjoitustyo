/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

/**
 *
 * @author konstakallama
 */
public class Score implements Comparable<Score> {
    String name;
    int floor;
    int turn;
    int level;
    String killedBy;

    public Score(String name, int floor, int turn, int level, String killedBy) {
        this.name = name;
        this.floor = floor;
        this.turn = turn;
        this.level = level;
        this.killedBy = killedBy;
    }

    public String getName() {
        return name;
    }

    public int getFloor() {
        return floor;
    }

    public int getTurn() {
        return turn;
    }

    public int getLevel() {
        return level;
    }

    public String getKilledBy() {
        return killedBy;
    }

    @Override
    public int compareTo(Score o) {
        return o.getFloor() - this.getFloor();
    }

    @Override
    public String toString() {
        return "Score{" + "name=" + name + ", floor=" + floor + ", turn=" + turn + ", level=" + level + ", killedBy=" + killedBy + '}';
    }
    
}
