/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.database;

import javafx.concurrent.Task;

/**
 *
 * @author PC
 */
public abstract class ConfirmModelTask extends Task<Boolean> {
    public abstract boolean action();
    public ConfirmModelTask() {
       
    }

    @Override
    protected Boolean call() {
        updateMessage("Vui lòng chờ...");
        return action();
    }

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
