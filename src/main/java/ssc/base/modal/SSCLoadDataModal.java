/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import ssc.base.database.BaseModel;
import ssc.base.database.LoadModelTask;

/**
 *
 * @author PC
 */
public abstract class SSCLoadDataModal extends SSCBaseModal {

    public abstract void loadDataAction();
    public abstract void initTable(TableView tv);
    public abstract ObservableList loadList();
    private ObservableList data;
    public SSCLoadDataModal(int widthRate, int height, String title,ObservableList data) {
        super(widthRate, height, title, "");
        TableView tvData = new TableView();
        this.data=data;
        tvData.getStyleClass().addAll("table-view-material");
        tvData.setPrefSize(content.getPrefWidth(), content.getPrefHeight());
        content.getChildren().add(tvData);
        initTable(tvData);
        tvData.setItems(data);
        loadDataTask();
    }
    public void loadDataTask (){
        LoadModelTask lmt=new LoadModelTask("") {
            @Override
            public ObservableList<BaseModel> getData(String query) {
                return loadList();
            }
        };
        lmt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                data.setAll(lmt.getValue());
            }
        });
        lmt.start();
    }

}
