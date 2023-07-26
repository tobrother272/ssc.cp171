/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ssc.base.database.BaseModel;
import ssc.base.database.LoadModelTask;

/**
 *
 * @author PC
 */
public abstract class SSCLoadDataModalWithInputText extends SSCBaseModal {

    public abstract void loadDataAction();

    public abstract void initTable(TableView tv);

    public abstract ObservableList loadList();
    public abstract EventHandler<ActionEvent> event() ;
    private ObservableList data;
    private TextField tfInviteUser;

    public TextField getTfInviteUser() {
        return tfInviteUser;
    }

    public void setTfInviteUser(TextField tfInviteUser) {
        this.tfInviteUser = tfInviteUser;
    }
    
    public SSCLoadDataModalWithInputText(int widthRate, int height, String title, ObservableList data) {
        super(widthRate, height, title, "");

        tfInviteUser = new TextField();
        tfInviteUser.setPrefSize(content.getPrefWidth() - 70, 30);
        tfInviteUser.getStyleClass().setAll("text-field");
        content.getChildren().add(tfInviteUser);
        Button btn=new Button("Mời");
        btn.setPrefSize(60, 30);
        btn.setLayoutX(tfInviteUser.getPrefWidth()+10);
        btn.getStyleClass().setAll("btn-primary");
        content.getChildren().add(btn);
        
       
        TableView tvData = new TableView();
        this.data = data;
        tvData.setLayoutY(tfInviteUser.getLayoutY()+tfInviteUser.getPrefHeight()+10);
        tvData.getStyleClass().addAll("table-view-material");
        tvData.setPrefSize(content.getPrefWidth(), content.getPrefHeight()-50);

        content.getChildren().add(tvData);
        initTable(tvData);
        tvData.setItems(data);
        loadDataTask();
        btn.setOnAction(event());
    }

    public void loadDataTask() {
        LoadModelTask lmt = new LoadModelTask("") {
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
