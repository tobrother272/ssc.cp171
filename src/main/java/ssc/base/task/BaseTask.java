/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import javafx.concurrent.Task;

/**
 *
 * @author PC
 */
public abstract class BaseTask extends Task<Boolean> {

    public abstract boolean mainFunction();
    private long startTime;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void updateMyMessage(String message) {
        updateMessage(message);
    }

    public void updateMyTitle(String message) {
        updateTitle(message);
    }

    @Override
    protected Boolean call() {
        startTime = System.currentTimeMillis();
        return mainFunction();
    }
    private Thread t;

    public void start() {
        t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public void stop() {
        try {
            if (t.isAlive()) {
                t.stop();
                cancel(true);
            }
        } catch (Exception e) {
        }
    }
}
