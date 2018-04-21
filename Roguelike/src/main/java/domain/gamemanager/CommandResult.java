/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.gamemanager;

/**
 *
 * @author konstakallama
 */
public class CommandResult {
    boolean success;
    boolean hasLogMessage;
    String logMessage;
    AttackResult attackResult;
    boolean isOnStairs;
    boolean diedOfHunger;

    public CommandResult(boolean success, boolean hasLogMessage, String logMessage, AttackResult attackResult, boolean isOnStairs, boolean diedOfHunger) {
        this.success = success;
        this.hasLogMessage = hasLogMessage;
        this.logMessage = logMessage;
        this.attackResult = attackResult;
        this.isOnStairs = isOnStairs;
        this.diedOfHunger = diedOfHunger;
    }
    public CommandResult(boolean success, boolean hasLogMessage, String logMessage, AttackResult attackResult, boolean isOnStairs) {
        this(success, hasLogMessage, logMessage, attackResult, isOnStairs, false);
        
    } 

    public CommandResult(boolean success, boolean hasLogMessage, String logMessage, AttackResult attackResult) {
        this(success, hasLogMessage, logMessage, attackResult, false);
    }

    public CommandResult(boolean success) {
        this(success, false, "", new AttackResult(AttackResultType.FAIL, 0, null, null), false);
    }

    public CommandResult(boolean success, boolean hasLogMessage) {
        this(success, hasLogMessage, "", new AttackResult(AttackResultType.FAIL, 0, null, null), false);
    }
    
    
    
    

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
    
    public boolean hasLogMessage() {
        return this.hasLogMessage;
    }

    public AttackResult getAttackResult() {
        return attackResult;
    }

    public boolean isOnStairs() {
        return isOnStairs;
    }

    public boolean isHasLogMessage() {
        return hasLogMessage;
    }

    public void setHasLogMessage(boolean hasLogMessage) {
        this.hasLogMessage = hasLogMessage;
    }

    public boolean isIsOnStairs() {
        return isOnStairs;
    }

    public void setIsOnStairs(boolean isOnStairs) {
        this.isOnStairs = isOnStairs;
    }

    public boolean isDiedOfHunger() {
        return diedOfHunger;
    }

    public void setDiedOfHunger(boolean diedOfHunger) {
        this.diedOfHunger = diedOfHunger;
    }
    
    
    
    
    
}
