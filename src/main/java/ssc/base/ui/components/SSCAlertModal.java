/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import ssc.base.modal.SSCBaseModal;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 *
 * @author PC
 */
public class SSCAlertModal extends SSCBaseModal {

    public SSCAlertModal(int widthRate, int height, String title) {
        super(widthRate, height, title, "");
        Button btn = new Button("Thoát");
        btn.setPrefSize(150, 30);
        content.getChildren().add(btn);
        btn.getStyleClass().setAll("btn-secondary");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                hide();
            }
        });
    }

}
