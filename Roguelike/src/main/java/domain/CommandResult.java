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
public class CommandResult {
    boolean success;
    boolean hasLogMessage;
    String logMessage;
    AttackResult attackResult;
    boolean isOnStairs;

    public CommandResult(boolean success, boolean hasLogMessage, String logMessage, AttackResult attackResult, boolean isOnStairs) {
        this.success = success;
        this.hasLogMessage = hasLogMessage;
        this.logMessage = logMessage;
        this.attackResult = attackResult;
        this.isOnStairs = isOnStairs;
        
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
    
    
    
}
