/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject;

/**
 *
 * @author konstakallama
 */
public class EnemyType {
    private int baseExp;
    private double expScale;
    private int baseStr;
    private double strScale;
    private int baseCon;
    private double conScale;
    private int baseInt;
    private double intScale;
    private int baseDex;
    private double dexScale;
    private String name;
    private EnemyDb e = new EnemyDb();

    public EnemyType(String name) {
//        this.baseExp = e.getBaseExp(name);
//        this.expScale = e.getExpScale(name);
//        this.baseStr = e.getBaseStr(name);
//        this.strScale = e.getStrScale(name);
//        this.baseCon = e.getBaseCon(name);
//        this.conScale = e.getConScale(name);
//        this.baseInt = e.getBaseInt(name);
//        this.intScale = e.getIntScale(name);
//        this.baseDex = e.getBaseDex(name);
//        this.dexScale = e.getDexScale(name);
        this.name = name;
    }
    
    public int getStr(int level) {
        return this.baseStr + (int) Math.round(strScale * Double.valueOf(level));
    }
    
    public int getCon(int level) {
        return this.baseCon + (int) Math.round(conScale * Double.valueOf(level));
    }
    
    public int getInt(int level) {
        return this.baseInt + (int) Math.round(intScale * Double.valueOf(level));
    }
    
    public int getDex(int level) {
        return this.baseDex + (int) Math.round(dexScale * Double.valueOf(level));
    }
    
    public int getExp(int level) {
        return this.baseExp + (int) Math.round(expScale * Double.valueOf(level));
    }

    public String getName() {
        return name;
    }
    
    
}
