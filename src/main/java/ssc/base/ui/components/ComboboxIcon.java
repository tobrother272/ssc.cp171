/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.AnchorPane;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class ComboboxIcon {

    private AnchorPane statusRoot;
    private Label lbTitle;
    private ComboBox lbValue;

    public ComboBox getLbValue() {
        return lbValue;
    }

    public void setLbValue(ComboBox lbValue) {
        this.lbValue = lbValue;
    }

    private Button btIcon;

    public ComboboxIcon(String icon, String title, ObservableList<String> arrOption, double w, double h) {

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
        lbTitle.setPrefSize(w - 50, 20);
        lbTitle.setLayoutX(50);
        lbTitle.setLayoutY(5);
        lbTitle.getStyleClass().setAll("lbStatusTitle");
        statusRoot.getChildren().add(lbTitle);

        lbValue = new ComboBox(arrOption);
        lbValue.setPrefSize(w - 50, 20);
        lbValue.setLayoutX(50);
        lbValue.getSelectionModel().selectFirst();
        lbValue.setLayoutY(25);
        //lbValue.getStyleClass().setAll("combo-box");
        statusRoot.getChildren().add(lbValue);

        Graphics.setIconLeft(btIcon, icon, 1.5);

    }

    public void setValue(int index) {
        lbValue.getSelectionModel().select(index);
    }

    public SingleSelectionModel valueProperties() {
        return lbValue.getSelectionModel();
    }

    public AnchorPane getView() {
        return statusRoot;
    }

}
