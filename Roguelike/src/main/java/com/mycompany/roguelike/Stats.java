/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

/**
 *
 * @author konstakallama
 */
public abstract class Stats {
    int level;
    int exp;
    int str;
    int con;
    int intel;
    int dex;
    int damage;
    Formulas f = new Formulas();
    Weapon weapon;
    Armor armor;

    public Stats(int level, int str, int con, int intel, int dex, Weapon weapon, Armor armor) {
        this.level = level;
        this.exp = 0;
        this.str = str;
        this.con = con;
        this.intel = intel;
        this.dex = dex;
        this.damage = 0;
        this.weapon = weapon;
        this.armor = armor;
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        this.level++;
    }

    public int getExp() {
        return exp;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getCon() {
        return con;
    }

    public void setCon(int con) {
        this.con = con;
    }

    public int getIntel() {
        return intel;
    }

    public void setIntel(int intel) {
        this.intel = intel;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getDamage() {
        return damage;
    }

    public boolean takeDamage(int dmg) {
        this.damage += dmg;
        return this.isDead();
    }
    
    public void heal(int amount) {
        this.damage -= amount;
        if (this.damage < 0) {
            this.damage = 0;
        }
    }
    
    public abstract int getMaxHP();
    
    public boolean isDead() {
        return this.getMaxHP() - this.damage <= 0;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    
    
    
}
