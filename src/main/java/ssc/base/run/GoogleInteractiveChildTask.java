/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import ssc.base.ultil.ThreadUtils;


/**
 *
 * @author simpl
 */
public abstract class GoogleInteractiveChildTask extends Task<Boolean> {
    private ActionWithAccountBase parentTask;
    
    public abstract void sendResultToParent();
    
    public void insertErrorLog(String message,String reason) {
        if(parentTask!=null){
            parentTask.insertErrorLog(message,reason);
        }
    }

    public void insertSuccessLog(String message) {
       if(parentTask!=null){
            parentTask.insertSuccessLog(message);
        }
    }

    public void insertWarningLog(String message) {
       if(parentTask!=null){
            parentTask.insertSuccessLog(message);
        }
    }

    public void insertErrorLog(Exception message) {
        if(parentTask!=null){
            parentTask.insertErrorLog(message);
        }
    }

    public boolean onFinishFail(String message) {
        getParentTask().updateMyMessage("Lỗi : " + message+" ");
        getParentTask().getAccount().updateData();
        getParentTask().insertErrorLog(message, "");
        return false;
    }
   
    public boolean onFinishSuccess() {
        
        
        
        
        getParentTask().updateMyTitle("Hoàn thành");
        getParentTask().updateMyMessage("Thành công");
        return true;
    }


    public void bindChildToParent() {
        this.setOnSucceeded(new EventHandler() {
            @Override
            public void handle(Event event) {
                parentTask.setChildTaskRunning(false);
                sendResultToParent();
            }
        });
        this.setOnFailed(new EventHandler() {
            @Override
            public void handle(Event event) {
                parentTask.setChildTaskRunning(false);
                sendResultToParent();
            }
        });
        this.setOnCancelled(new EventHandler() {
            @Override
            public void handle(Event event) {
                parentTask.setChildTaskRunning(false);
                sendResultToParent();
            }
        });
    }

    public ActionWithAccountBase getParentTask() {
        return parentTask;
    }

    public void updateMyMessage(String message) {
        if (parentTask != null) {
            parentTask.updateMyMessage(message);
        } else {
            updateMessage(message);
        }
    }
    

    public GoogleInteractiveChildTask(ActionWithAccountBase parentTask) {
        this.parentTask = parentTask;
    }
    public abstract String setActionName();
    public String actionName() {
        return setActionName();
    }
    
    public void wait(String message, int time) {
        for (int i = time; i >= 0; i--) {
            ThreadUtils.Sleep(1000);
            updateMyMessage("Chờ " + message + " " + i + " s");
        }
    }
   
}
