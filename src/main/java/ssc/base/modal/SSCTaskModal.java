/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import ssc.base.view.ProcessTaskView;

/**
 *
 * @author PC
 */
public abstract class SSCTaskModal extends SSCBaseModal {
    public abstract void initTable(TableView tv); 
    public abstract EventHandler<ActionEvent> event() ;
    private ObservableList dataRun;
    public ObservableList getDataRun() {
        return dataRun;
    }

    public void setDataRun(ObservableList dataRun) {
        this.dataRun = dataRun;
    }
    
    private ObservableList dataHistory;
    private ProcessTaskView uploadTaskView;

    public ProcessTaskView getUploadTaskView() {
        return uploadTaskView;
    }


    
    public abstract void modalTimerTaskBody();
    public abstract void modalRunEventBody();
    public SSCTaskModal(int widthRate, int height, String title) {
        super(widthRate, height, title, "");
        dataHistory=FXCollections.observableArrayList();
        dataRun=FXCollections.observableArrayList();
        uploadTaskView = new ProcessTaskView(content, "") {
            @Override
            public ObservableList initAndSetListRunning() {
                return dataRun;
            }

            @Override
            public ObservableList initAndSetListHistory() {
               return dataHistory;
            }

            @Override
            public void runEventBody() {
               modalRunEventBody();
            }

            @Override
            public void timerTaskBody() {
               modalTimerTaskBody();
            }

            @Override
            public boolean stopRunningTaskBody() {
                try {

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        };
        initTable(uploadTaskView.getTv());
        uploadTaskView.getTv().setItems(dataRun);
        setLayoutY(5);
    }


}
