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
public class Weapon extends InventoryItem {
    private int atk;
    private double hit;
    private WeaponType type;
    private int strRec;

    public Weapon(int atk, double hit, WeaponType type, int wt, int strRec, String name) {
        super(wt, ItemType.WEAPON, name);
        this.atk = atk;
        this.hit = hit;
        this.type = type;
        this.strRec = strRec;
    }

    public Weapon(int atk, double hit, WeaponType type, String name) {
        this(atk, hit, type, 0, 0, name);
    }
    

    public int getAtk() {
        return atk;
    }

    public double getHit() {
        return hit;
    }

    public WeaponType getType() {
        return type;
    }

    public int getStrRec() {
        return strRec;
    }
    
    
    
    
}
