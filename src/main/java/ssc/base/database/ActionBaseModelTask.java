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
public abstract class ActionBaseModelTask extends Task<BaseModel> {

    private BaseModel object;

    public abstract BaseModel getData(BaseModel object);

    public ActionBaseModelTask(BaseModel object) {
        this.object = object;
    }

    @Override
    protected BaseModel call() {
        updateMessage("Vui lòng chờ...");
        return getData(object);
    }

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
