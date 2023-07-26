/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import ssc.base.ui.components.SSCLoadingButton;
import ssc.base.database.ConfirmModelTask;

/**
 *
 * @author PC
 */
public abstract class SSCConfirmTaskModal extends SSCBaseModal {

    public abstract boolean afterConfirmAction();

    public SSCConfirmTaskModal(int widthRate, int height, String title) {
        super(widthRate, height, title, "");
        Button btn = new Button("Thoát");
        btn.setPrefSize(200, 30);
        content.getChildren().add(btn);
        btn.getStyleClass().setAll("btn-secondary");
        SSCLoadingButton btnConfirm = new SSCLoadingButton("Đồng Ý", 200, 30, 0);
        content.getChildren().add(btnConfirm.getView());
        btnConfirm.setLayoutY(btn.getLayoutY());
        btnConfirm.setLayoutX(btn.getLayoutX() + btn.getPrefWidth() + 10);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                hide();
            }
        });
        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ConfirmModelTask cmt = new ConfirmModelTask() {
                    @Override
                    public boolean action() {
                        return afterConfirmAction();
                    }
                };
                cmt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        hide();
                    }
                });
                btnConfirm.bindTask(cmt);
                cmt.start();

            }
        });
    }

}
