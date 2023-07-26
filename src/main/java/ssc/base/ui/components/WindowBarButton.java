/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class WindowBarButton {

    private ButtonTooltip btn;

    public WindowBarButton(HBox container, String icon, String tooltip, EventHandler<ActionEvent> event) {

        btn = new ButtonTooltip(tooltip);
        btn.setPrefSize(30, 30);
        btn.setOnAction(event);
        container.getChildren().add(btn);
        Graphics.setIconLeft(btn, icon, 1.0);
      
    }

}
