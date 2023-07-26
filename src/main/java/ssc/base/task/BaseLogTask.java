/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ssc.theta.app.model.TaskLog;


/**
 *
 * @author PC
 */
public abstract class BaseLogTask extends BaseTask{
    private ObservableList<TaskLog> arrLog;
    public BaseLogTask() {
        this.arrLog = FXCollections.observableArrayList();
    }

    public ObservableList<TaskLog> getArrLog() {
        return arrLog;
    }

    public void setArrLog(ObservableList<TaskLog> arrLog) {
        this.arrLog = arrLog;
    }
     public void insertErrorLog(String message, String reason) {
        arrLog.add(0,new TaskLog(System.currentTimeMillis(), message, reason, "", 2));
    }

    public void insertSuccessLog(String message) {
        arrLog.add(0,new TaskLog(System.currentTimeMillis(), message, "", "", 2));
    }

    public void insertErrorLog(Exception message) {
        arrLog.add(0,new TaskLog(System.currentTimeMillis(), message.getMessage(), "", "", 2));
    }

    
}
