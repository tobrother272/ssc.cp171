/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.view;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ssc.theta.app.App;
import ssc.theta.app.LoginController;
import ssc.base.ui.components.SSCFormControl;
import ssc.base.ui.components.SSCPasswordField;
import ssc.base.ui.components.SSCTextField;
import ssc.base.global.ViewGlobal;
import ssc.base.task.LoginTask;
import ssc.base.ultil.Graphics;
import ssc.base.ui.components.SSCLoadingButton;
/**
 *
 * @author PC
 */
public class LoginView {

    private AnchorPane container;
    private SSCLoadingButton btnSubmit;
    private Button btnExit;
    private SSCForm loginForm;
    private AnchorPane loginFormRoot;
    private ImageView ivLogo;

    public LoginView(LoginController controller) {
        container = (AnchorPane) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().loginFormId);
        loginFormRoot = new AnchorPane();

        try {
            Image image = new Image(App.class.getResource("/assets/logo.png").toURI().toString());
            ivLogo = new ImageView(image);
            ivLogo.setFitHeight(50);
            ivLogo.setFitWidth(100);
            ivLogo.setLayoutY(50);
            ivLogo.setLayoutX((container.getPrefWidth() - 100) / 2);
            container.getChildren().add(ivLogo);
        } catch (Exception e) {
        }
        loginFormRoot.setLayoutY(150);
        loginFormRoot.setLayoutX(20);
        container.getChildren().add(loginFormRoot);
        loginFormRoot.setPrefWidth(container.getWidth() - 40);
        loginFormRoot.setPrefHeight(200);

        
        
        
        
        loginForm = new SSCForm(loginFormRoot, "Đăng nhập", false) {
            @Override
            public void setOnAction() {
                LoginTask login = new LoginTask(loginForm.getValues().get(0), loginForm.getValues().get(1));
                getActionBtn().bindTask(login);
                login.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        if (!login.getValue()) {
                            getActionBtn().setMessage("Sai tên đăng nhập hoặc mật khẩu");
                        } else {
                            try {
                                loginForm.saveStorageValue();
                                //controller.switchToSecondary();
                            } catch (Exception e) {
                            }
                        }
                    }
                });
                login.start();
            }
        };
        loginForm.setChangeContainer(true);
        loginForm.addChild(new SSCTextField(loginForm, "login_username", "Tên Đăng nhập", "", "Nhập Tên Đăng nhập ...", Arrays.asList(SSCFormControl.VALIDATES.REQUIRED)));
        loginForm.addChild(new SSCPasswordField(loginForm, "login_password", "Mật khẩu", "", "Nhập Mật khẩu ...", Arrays.asList(SSCFormControl.VALIDATES.REQUIRED)));
        loginForm.loadStorageValue();

        btnExit = new Button();
        btnExit.setPrefSize(30, 30);
        container.getChildren().add(btnExit);
        btnExit.setLayoutX(container.getPrefWidth() - 35);
        btnExit.setLayoutY(5);
        Graphics.setIconLeft(btnExit, "CLOSE", 1.0);
        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        btnExit.getStyleClass().setAll("btn-secondary");
    }
}
