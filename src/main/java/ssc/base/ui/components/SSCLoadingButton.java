/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import java.io.File;
import java.nio.file.Paths;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ssc.theta.app.App;

/**
 *
 * @author PC
 */
public class SSCLoadingButton {

    private AnchorPane formGroup;
    private Button btn;
    private ImageView imgLoading;
    private EventHandler<ActionEvent> event;
    private Label lbMessage;

    public void bindTask(Task task) {
        if (lbMessage != null) {
            lbMessage.setText("");
        }
        btn.textProperty().bind(task.messageProperty());
        imgLoading.visibleProperty().bind(task.runningProperty());
        btn.disableProperty().bind(task.runningProperty());
    }

    public void setLayoutY(double layoutY) {
        this.formGroup.setLayoutY(layoutY);
    }

    public AnchorPane getView() {
        return formGroup;
    }

    public void setOnAction(EventHandler<ActionEvent> event) {
        this.btn.setOnAction(event);
    }
    public void setText(String text){
        this.btn.setText(text);
    }
    
    public void pinButton(){
        btn.setLayoutY(0);
        formGroup.getChildren().remove(lbMessage);
    }
    
    
    public SSCLoadingButton(String btnTitle, double width, double height) {
        this.formGroup = new AnchorPane();
        this.formGroup.setPrefSize(width, height);
        this.btn = new Button(btnTitle);
        lbMessage = new Label();
        btn.setPrefSize(formGroup.getPrefWidth(), 40);
        lbMessage.setPrefSize(formGroup.getPrefWidth(), 40);
        btn.setOnAction(event);

        Image image = null;
        try {
            image = new Image(App.class.getResource("/assets/loading.gif").toURI().toString());
          
        } catch (Exception e) {
        }

        imgLoading = new ImageView(image);
        imgLoading.setVisible(false);
        imgLoading.setFitWidth(20);
        imgLoading.setFitHeight(20);
        imgLoading.setLayoutY(30);
        imgLoading.setLayoutX(5);

        formGroup.getChildren().add(btn);
        formGroup.getChildren().add(imgLoading);
        formGroup.getChildren().add(lbMessage);
        btn.setLayoutY(20);

        btn.getStyleClass().setAll("btn-primary");

        lbMessage.getStyleClass().setAll("labelMessage");
        lbMessage.setLayoutY(btn.getLayoutY() + btn.getPrefHeight());
        lbMessage.setLayoutX(5);

    }

    public SSCLoadingButton(String btnTitle, double width, double height, int marginTop) {
        this.formGroup = new AnchorPane();
        this.formGroup.setPrefSize(width, height);
        this.btn = new Button(btnTitle);
        btn.setPrefSize(formGroup.getPrefWidth(), height);
        btn.setOnAction(event);

        Image image = null;
        try {
            image = new Image(getClass().getResource("/assets/loading.gif").toURI().toString());
        } catch (Exception e) {
        }

        imgLoading = new ImageView(image);
        imgLoading.setVisible(false);
        imgLoading.setFitWidth(20);
        imgLoading.setFitHeight(20);
        imgLoading.setLayoutY(30);
        imgLoading.setLayoutX(5);
        formGroup.getChildren().add(btn);
        formGroup.getChildren().add(imgLoading);
        btn.getStyleClass().setAll("btn-primary");

    }

    public void setLayoutX(double layoutX) {
        formGroup.setLayoutX(layoutX);
    }

    public void setMessage(String message) {
        if (lbMessage != null) {
            lbMessage.setText(message);
        }

    }

    public AnchorPane getFormGroup() {
        return formGroup;
    }

    public void setFormGroup(AnchorPane formGroup) {
        this.formGroup = formGroup;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public ImageView getImgLoading() {
        return imgLoading;
    }

    public void setImgLoading(ImageView imgLoading) {
        this.imgLoading = imgLoading;
    }

}
