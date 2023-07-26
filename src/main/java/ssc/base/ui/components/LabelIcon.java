/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class LabelIcon {

    private AnchorPane statusRoot;
    private Label lbTitle;
    private Label lbValue;
    private Button btIcon;

    public AnchorPane getStatusRoot() {
        return statusRoot;
    }
    
    public LabelIcon(String icon, String title, double w, double h) {

        statusRoot = new AnchorPane();
        statusRoot.getStyleClass().setAll("statusGroup");
        btIcon = new Button();
        statusRoot.setPrefSize(w, h);
        statusRoot.getChildren().add(btIcon);
        btIcon.setPrefSize(30, 30);
        btIcon.setLayoutX(10);
        btIcon.getStyleClass().setAll("ico-status");
        btIcon.setLayoutY(10);

        lbTitle = new Label(title);
        lbTitle.setPrefSize(150, 20);
        lbTitle.setLayoutX(50);
        lbTitle.setLayoutY(15);
        lbTitle.getStyleClass().setAll("lbStatusTitle");
        statusRoot.getChildren().add(lbTitle);

        lbValue = new Label(title);
        lbValue.setPrefSize(200, 20);
        lbValue.setLayoutX(100);
        lbValue.setLayoutY(15);
        lbValue.getStyleClass().setAll("lbStatusValue");
        statusRoot.getChildren().add(lbValue);
        Graphics.setIconLeft(btIcon, icon, 1.5);

    }

    public StringProperty textProperty() {
        return lbValue.textProperty();
    }

    public AnchorPane getView() {
        return statusRoot;
    }

    public void setText(String text) {
        lbValue.setText(text);
    }
}
