/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ssc.theta.app.App;
import ssc.base.global.ViewGlobal;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class SSCBaseModal {

    private AnchorPane background;
    private AnchorPane container;
    private Label lbTitle;
    private Label lbSubTitle;
    private Button btnClose;
    public AnchorPane content;
    private AnchorPane loadingPane;

    public void bindTask(Task task) {
        loadingPane.visibleProperty().bind(task.runningProperty());
        lbSubTitle.textProperty().bind(task.messageProperty());
    }

    public void setLayoutY(double y) {
        container.setLayoutY(y);
    }

    public SSCBaseModal(int widthRate, int height, String title, String subTitle) {
        background = new AnchorPane();
        background.setPrefSize(ViewGlobal.getInst().getMainContainer().getPrefWidth(), ViewGlobal.getInst().getMainContainer().getPrefHeight());
        background.getStyleClass().setAll("modalBackground");
        ViewGlobal.getInst().getMainContainer().getChildren().add(background);
        background.setVisible(false);

        container = new AnchorPane();
        container.setPrefSize(ViewGlobal.getInst().getMainContainer().getPrefWidth() / widthRate, height);
        container.getStyleClass().setAll("modalPane");
        container.setLayoutX((ViewGlobal.getInst().getMainContainer().getPrefWidth() - container.getPrefWidth()) / 2);
        container.setLayoutY(50);
        ViewGlobal.getInst().getMainContainer().getChildren().add(container);
        container.setVisible(false);

        background.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                hide();
            }
        });

        lbTitle = new Label(title);
        lbTitle.setPrefSize(container.getPrefWidth(), 30);
        lbTitle.setLayoutY(20);
        lbTitle.getStyleClass().setAll("labelModalTitle");

        lbSubTitle = new Label(subTitle);
        lbSubTitle.setPrefSize(container.getPrefWidth(), 30);
        lbSubTitle.getStyleClass().setAll("labelModalSubTitle");
        lbSubTitle.setLayoutY(lbTitle.getLayoutY() + lbTitle.getPrefHeight());

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
        content = new AnchorPane();
        content.setPrefSize(container.getPrefWidth() - 40, height - (lbSubTitle.getLayoutY() + lbSubTitle.getPrefHeight() + 20));
        content.setLayoutY(lbSubTitle.getLayoutY() + lbSubTitle.getPrefHeight());
        content.setLayoutX(20);

        container.getChildren().add(lbTitle);
        container.getChildren().add(lbSubTitle);
        container.getChildren().add(btnClose);
        container.getChildren().add(content);

        loadingPane = new AnchorPane();
        loadingPane.setPrefSize(container.getPrefWidth() - 40, height - (lbSubTitle.getLayoutY() + lbSubTitle.getPrefHeight() + 20));
        loadingPane.setLayoutY(lbSubTitle.getLayoutY() + lbSubTitle.getPrefHeight()+20);
        loadingPane.setLayoutX(20);
        loadingPane.setVisible(false);
        content.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                loadingPane.setPrefWidth(t1.doubleValue());
            }
        });
        content.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                loadingPane.setPrefHeight(t1.doubleValue());
            }
        });

        loadingPane.getStyleClass().setAll("modalBackground");
        try {
            Image image = new Image(App.class.getResource("/assets/loading.gif").toURI().toString());
            ImageView ivLogo = new ImageView(image);
            ivLogo.setFitHeight(30);
            ivLogo.setFitWidth(30);
            ivLogo.setLayoutY((loadingPane.getPrefHeight() - 30) / 2);
            ivLogo.setLayoutX((loadingPane.getPrefWidth() - 30) / 2);
            loadingPane.getChildren().add(ivLogo);
        } catch (Exception e) {
        }

        container.getChildren().add(loadingPane);
    }

    public void show() {
        background.setVisible(true);
        container.setVisible(true);
    }

    public void hide() {
        ViewGlobal.getInst().getMainContainer().getChildren().remove(container);
        ViewGlobal.getInst().getMainContainer().getChildren().remove(background);
    }

}
