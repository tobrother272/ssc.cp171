/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.database;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 * @author PC
 */
public abstract class LoadModelTask extends Task<ObservableList<BaseModel>> {

    private String query;

    public abstract ObservableList<BaseModel> getData(String query);

    public LoadModelTask(String query) {
        this.query = query;
    }

    @Override
    protected ObservableList<BaseModel> call() {
        return getData(query);
    }

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
