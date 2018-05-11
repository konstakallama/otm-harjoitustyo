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
public class CharCreationStats {

    int initialPoints;
    int pointsUsed;
    int limit;

    int strength;
    int connstitution;
    int intelligence;
    int dexterity;

    public CharCreationStats(int initialPoints, int limit) {
        this.initialPoints = initialPoints;
        this.pointsUsed = 0;

        this.strength = 1;
        this.connstitution = 1;
        this.intelligence = 1;
        this.dexterity = 1;
        this.limit = limit;
    }

    public int getInitialPoints() {
        return initialPoints;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public int getStrength() {
        return strength;
    }

    public int getConnstitution() {
        return connstitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getDexterity() {
        return dexterity;
    }
    
    public boolean increaseStr() {
        if (this.pointsUsed >= this.initialPoints || this.strength >= this.limit) {
            return false;
        }
        this.strength++;
        this.pointsUsed++;
        return true;
    }
    
    public boolean increaseCon() {
        if (this.pointsUsed >= this.initialPoints || this.connstitution >= this.limit) {
            return false;
        }
        this.connstitution++;
        this.pointsUsed++;
        return true;
    }
    
    public boolean increaseInt() {
        if (this.pointsUsed >= this.initialPoints || this.intelligence >= this.limit) {
            return false;
        }
        this.intelligence++;
        this.pointsUsed++;
        return true;
    }
    
    public boolean increaseDex() {
        if (this.pointsUsed >= this.initialPoints || this.dexterity >= this.limit) {
            return false;
        }
        this.dexterity++;
        this.pointsUsed++;
        return true;
    }
    
    public boolean decreaseStr() {
        if (this.strength <= 1) {
            return false;
        }
        this.strength--;
        this.pointsUsed--;
        return true;
    }
    
    public boolean decreaseCon() {
        if (this.connstitution <= 1) {
            return false;
        }
        this.connstitution--;
        this.pointsUsed--;
        return true;
    }
    
    public boolean decreaseInt() {
        if (this.intelligence <= 1) {
            return false;
        }
        this.intelligence--;
        this.pointsUsed--;
        return true;
    }
    
    public boolean decreaseDex() {
        if (this.dexterity <= 1) {
            return false;
        }
        this.dexterity--;
        this.pointsUsed--;
        return true;
    }

}
