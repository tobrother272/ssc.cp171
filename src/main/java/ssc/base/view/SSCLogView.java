/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import ssc.base.global.ViewGlobal;
import ssc.theta.app.model.TaskLog;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class SSCLogView {

    private AnchorPane container;
    private Button btnClose;
    private Button btnChangeType;
    private ObservableList<TaskLog> arrLog1;
    private ObservableList<TaskLog> arrLog2;
    private TableView tvLog;
    private Label lbMessage;

    public Label getLbMessage() {
        return lbMessage;
    }

    public void setLbMessage(Label lbMessage) {
        this.lbMessage = lbMessage;
    }
    private boolean isOne = true;

    public SSCLogView(ObservableList<TaskLog> arrLog1, ObservableList<TaskLog> arrLog2) {
        this.arrLog1 = arrLog1;
        this.arrLog2 = arrLog2;
        container = new AnchorPane();
        container.setPrefSize(ViewGlobal.getInst().getMainContainer().getPrefWidth(), 500);
        container.getStyleClass().setAll("logBackground");
        ViewGlobal.getInst().getMainContainer().getChildren().add(container);
        container.setVisible(false);
        container.setLayoutY(ViewGlobal.getInst().getMainContainer().getPrefHeight() - container.getPrefHeight());
        container.setLayoutX(0);

        btnClose = new Button();
        btnClose.setPrefSize(30, 30);

        btnClose.setLayoutX(container.getPrefWidth() - 35);
        btnClose.setLayoutY(5);
        Graphics.setIconLeft(btnClose, "CLOSE", 1.0);
        btnClose.getStyleClass().setAll("btn-secondary");
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                hide();
            }
        });

        btnChangeType = new Button();
        btnChangeType.setPrefSize(30, 30);

        btnChangeType.setLayoutX(container.getPrefWidth() - 65);
        btnChangeType.setLayoutY(5);

        Graphics.setIconLeft(btnChangeType, "REFRESH", 1.0);
        btnChangeType.getStyleClass().setAll("btn-secondary");
        btnChangeType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                isOne = !isOne;
                if(isOne){
                    tvLog.setItems(arrLog1);
                }else{
                    tvLog.setItems(arrLog2);
                }
            }
        });

        lbMessage = new Label();
        lbMessage.setPrefSize(container.getPrefWidth() - 70, 30);
        lbMessage.setLayoutY(5);
        lbMessage.setLayoutX(20);
        lbMessage.getStyleClass().setAll("labelRowMain");
        container.getChildren().add(lbMessage);

        tvLog = new TableView(arrLog1);
        tvLog.setPrefSize(container.getPrefWidth() - 40, container.getPrefHeight() - 50);

        tvLog.setLayoutY(40);
        tvLog.setLayoutX(20);
        tvLog.getStyleClass().setAll("table-view-log");

        container.getChildren().add(btnClose);
        container.getChildren().add(btnChangeType);
        container.getChildren().add(tvLog);

        tvLog.getColumns().setAll(TaskLog.getArrCol());

    }

    public void show() {
        container.setVisible(true);
    }

    public void hide() {
        ViewGlobal.getInst().getMainContainer().getChildren().remove(container);
    }

}
