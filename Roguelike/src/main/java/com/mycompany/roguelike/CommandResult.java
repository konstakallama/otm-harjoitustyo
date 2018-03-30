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
public class CommandResult {
    boolean success;
    boolean hasLogMessage;
    String logMessage;
    AttackResult attackResult;

    public CommandResult(boolean success, boolean hasLogMessage, String logMessage, AttackResult attackResult) {
        this.success = success;
        this.hasLogMessage = hasLogMessage;
        this.logMessage = logMessage;
        this.attackResult = attackResult;
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
    
}
