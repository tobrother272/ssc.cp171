/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import ssc.base.task.BaseTask;
import static ssc.base.ultil.ThreadUtils.Sleep;
import ssc.task.FinishEventTask;
import ssc.theta.app.model.TaskLog;

/**
 * @author simpl
 */
public abstract class ActionBase extends BaseTask {

    private ObservableList<TaskLog> arrLog;

    public ObservableList<TaskLog> getArrLog() {
        return arrLog;
    }

    public void setArrLog(ObservableList<TaskLog> arrLog) {
        this.arrLog = arrLog;
    }
    
    private String prefix = "";
    private Boolean actionSuccess = false;
    private boolean childTaskRunning;
    private String taskOwner;
    private int stt;
    private SimpleStringProperty ip;
    private String proxy = "";

    public ActionBase(int stt) {
        this.arrLog = FXCollections.observableArrayList();
        this.stt = stt;
        this.ip = new SimpleStringProperty("n/a");
    }

    public abstract void afterFail();

    public abstract void afterSuccess();

    public abstract String initFunction();

    public abstract String objectHistory();

    public abstract void checkAndStopTask();

    public abstract void updateAccountInfo();

    public String getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getActionSuccess() {
        return actionSuccess;
    }

    public void setActionSuccess(Boolean actionSuccess) {
        this.actionSuccess = actionSuccess;
    }

    public void insertErrorLog(String message, String reason) {
        arrLog.add(0,new TaskLog(System.currentTimeMillis(), message, reason, "", 2));
    }

    public void insertSuccessLog(String message) {
        arrLog.add(0,new TaskLog(System.currentTimeMillis(), message, "", "", 2));
    }

    public void insertErrorLog(Exception message) {
        arrLog.add(0,new TaskLog(System.currentTimeMillis(), subTitle, message.getMessage(), "", 2));
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public SimpleStringProperty ipProperty() {
        return ip;
    }

    public SimpleStringProperty getIp() {
        return ip;
    }

    public void setIp(String _ip) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ip.set(_ip);
            }
        });
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    private GoogleInteractiveChildTask currentRunningChildTask;

    public GoogleInteractiveChildTask getCurrentRunningChildTask() {
        return currentRunningChildTask;
    }

    public void setCurrentRunningChildTask(GoogleInteractiveChildTask currentRunningChildTask) {
        this.currentRunningChildTask = currentRunningChildTask;
    }

    public boolean isChildTaskRunning() {
        return childTaskRunning;
    }

    public void setChildTaskRunning(boolean childTaskRunning) {
        this.childTaskRunning = childTaskRunning;
    }
    private Thread childThread;

    public void changeChildTask() {
        currentRunningChildTask.bindChildToParent();
        if (childThread != null && childThread.isAlive()) {
            childThread.interrupt();
            childThread.stop();
        }
        childThread = new Thread(currentRunningChildTask);
        childThread.setDaemon(true);
        childThread.start();
    }

    private String subTitle = "";

    public void stopChild() {
        stop();
        if (childThread != null && childThread.isAlive()) {
            childThread.interrupt();
            childThread.stop();
        }
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void waitChildTask(int waitTime, GoogleInteractiveChildTask task) {
        currentRunningChildTask = task;
        childTaskRunning = true;
        changeChildTask();
        int currentTime = 0;
        while (childTaskRunning) {
            if (currentTime >= waitTime) {
                break;
            }
            updateTitle(currentTime + "/" + waitTime + " " + currentRunningChildTask.actionName() + " - " + subTitle);
            checkAndStopTask();
            currentTime++;
            Sleep(1000);
        }
        childTaskRunning = false;
        currentRunningChildTask.cancel();
    }

    public void wait(String message, int time) {
        updateTitle(" : " + message + " " + time + " giây");
        Sleep(1000 * time);
    }

    public void waitMessage(String message, int time) {

        for (int i = 1; i <= time; i++) {
            Sleep(1000);
            updateMessage(" : " + message + " " + i + "/" + time + " giây");
        }

    }
    public void eventListener(FinishEventTask myEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        myEvent.main();
                    }
                });
                setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        myEvent.main();
                    }
                });
                setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        myEvent.main();
                    }
                });
            }
        });
    }
    
    public abstract boolean automationAction();
    public abstract boolean initAutomationAction();
   
    @Override
    public boolean mainFunction() {
        try {
            updateTitle("Khởi tạo");
            Sleep(2000);
            if (proxy.length()!=0) {
                setIp(proxy.split(":")[0]);
            }
            String init = initFunction();
            if (init.length() != 0) {
                afterFail();
                updateTitle("Khởi tạo lỗi " + init);
                return false;
            }
            if(!initAutomationAction()){
                 return false;
            }
            if (!automationAction()) {
                return false;
            }
            afterSuccess();
            return true;
        } catch (Exception e) {
            insertErrorLog(this.taskOwner + " Khởi tạo tài khoản lỗi ", e.getMessage());
            e.printStackTrace();
        } finally {
            updateAccountInfo();
        }
        return true;
    }

}
