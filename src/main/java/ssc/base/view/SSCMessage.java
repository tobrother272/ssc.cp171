/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.view;
import java.util.Comparator;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import ssc.base.global.ViewGlobal;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class SSCMessage {

    public static SSCMessage instance;

    public SSCMessage() {
        position = FXCollections.observableArrayList();
        position.add("1");
        position.add("2");
        position.add("3");
        position.add("4");
        position.add("5");
        position.add("6");
        position.add("7");
        position.add("8");
        position.add("9");
        position.add("10");
    }

    public static SSCMessage getInstance() {
        if (instance == null) {
            instance = new SSCMessage();
        }
        return instance;
    }
    private ObservableList<String> position;

    public static void showError(String text) {
        show(text, "error", "CLOSE");
    }

    public static void showSuccess(String text) {
        show(text, "success", "CHECK_SQUARE_ALT");
    }

    public static void showWarning(String text) {
        show(text, "warning", "WARNING");
    }

    public static void showSuccessInThread(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showSuccess(text);
            }
        });

    }

    public static void showWarningInThread(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showWarning( text);
            }
        });

    }
        public static void showErrorInThread(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showError( text);
            }
        });

    }
    private static void show(String text, String prefix, String icon) {
        System.err.println("------------"+text);
        try {

            AnchorPane apRoot = ((AnchorPane) ViewGlobal.getInst().scene.lookup("#mainContainer"));
           
            AnchorPane apMessageRoot = new AnchorPane();
            apMessageRoot.setPrefSize(apRoot.getPrefWidth() / 2, 30);

         

            Button btnIcon = new Button();
            btnIcon.setLayoutX(10);
            btnIcon.setPrefSize(30, 30);
            apMessageRoot.getChildren().add(btnIcon);
            btnIcon.getStyleClass().setAll("icon-" + prefix);
            
            
            Graphics.setIconLeft(btnIcon, icon, 1.5);
            


            Label lbMe = new Label(text);
            lbMe.setLayoutX(btnIcon.getLayoutX() + btnIcon.getPrefWidth());
            lbMe.setPrefHeight(30);
            lbMe.getStyleClass().setAll("lb-message-" + prefix);
            apMessageRoot.getChildren().add(lbMe);

            FXCollections.sort(SSCMessage.getInstance().position, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.parseInt(o1) - Integer.parseInt(o2);
                }
            });

            String _position = SSCMessage.getInstance().position.get(0);
            SSCMessage.getInstance().position.remove(_position);
            apMessageRoot.setUserData("message#" + _position);

            apMessageRoot.setLayoutX((apRoot.getPrefWidth() - apMessageRoot.getPrefWidth()) / 2);
            apRoot.getChildren().add(apMessageRoot);
            apMessageRoot.setLayoutY(Integer.parseInt(_position) * 35);
            apMessageRoot.getStyleClass().setAll("message-" + prefix);
            FadeTransition fadeInTransition = new FadeTransition(Duration.millis(500), apMessageRoot);
            fadeInTransition.setFromValue(0.0);
            fadeInTransition.setToValue(1.0);
            fadeInTransition.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(500), apMessageRoot);
            tt.setByX(-200);
            tt.setToX(5);
            tt.play();
            tt.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FadeTransition fadeInTransition = new FadeTransition(Duration.millis(3500), apMessageRoot);
                    fadeInTransition.setFromValue(1.0);
                    fadeInTransition.setToValue(0.9);
                    fadeInTransition.play();
                    fadeInTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            SSCMessage.getInstance().position.add(apMessageRoot.getUserData().toString().replaceAll("message#", ""));
                            apRoot.getChildren().remove(apMessageRoot);
                            if (apRoot.getChildren().size() == 0) {
                                apRoot.setVisible(false);
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
