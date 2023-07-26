/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ssc.base.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ssc.base.modal.SSCSettingModal;
import ssc.base.ui.components.WindowBarButton;
import ssc.base.global.TC;
import ssc.base.global.ViewGlobal;

/**
 *
 * @author PC
 */
public class WindowBarView {

    private HBox container;

    public WindowBarView() {
        container = (HBox) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().windowBarID);
        
        new WindowBarButton(container, "EXTERNAL_LINK", "Đăng xuất", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
               TC.getInts().setAccessToken("");
            }
        });
        new WindowBarButton(container, "COG", "Cài đặt tool", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
               new SSCSettingModal(3,600,"Cài đặt Tool","Vui lòng cài đặt thông tin tool").show();
            }
        });
        new WindowBarButton(container, "CARET_DOWN", "Ẩn Tool", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ((Stage) ViewGlobal.getInst().scene.getWindow()).setIconified(true);
            }
        });
        new WindowBarButton(container, "CLOSE", "Đóng Tool", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        
        
        
        
        
    }
}
