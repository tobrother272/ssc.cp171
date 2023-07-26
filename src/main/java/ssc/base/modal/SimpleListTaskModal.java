/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 *
 * @author PC
 */
public abstract class SimpleListTaskModal extends SSCBaseModal {

    public abstract void loadDataAction();
    
    public abstract void initTable(TableView tv);
    private Timer timer;
    public int running=0;
    public abstract TimerTask timerTask();
    private int position = 0;
    public void increatePosition(){
        position++;
        running++;
    }
    public void decreaseRunning(){
        running--;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }
    
    
    public int getPosition() {
        return position;
    }

    public ObservableList getData() {
        return data;
    }

    public void setData(ObservableList data) {
        this.data = data;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    private ObservableList data;
    private Button rButton;

    public SimpleListTaskModal(int widthRate, int height, String title, ObservableList data, String btnTitle) {
        super(widthRate, height, title, "");

        TableView tvData = new TableView();
        this.data = data;
        tvData.setLayoutY(10);
        tvData.getStyleClass().addAll("table-view-material");
        tvData.setPrefSize(content.getPrefWidth(), content.getPrefHeight() - 90);
        rButton = new Button();
        rButton.setPrefSize(content.getPrefWidth(), 30);
        content.getChildren().add(tvData);
        content.getChildren().add(rButton);
        rButton.setLayoutY((int) (tvData.getLayoutY() + tvData.getPrefHeight() + 20));

        rButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                position = 0;
                running=0;
                timer = new Timer();
                timer.schedule(timerTask(), 1000, 1000);
            }
        });

        initTable(tvData);
        tvData.setItems(data);
        loadDataTask();
    }

    public void loadDataTask() {

    }

}
