/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.global;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class ViewGlobal {

    public static ViewGlobal inst;

    public static ViewGlobal getInst() {
        if (inst == null) {
            inst = new ViewGlobal();
        }
        return inst;
    }

    public void bindScreen(Scene scene) {
        this.scene = scene;
        Graphics.addDragListeners(scene.getRoot());
        try {
            scene.getRoot().getStylesheets().add(getClass().getResource("/assets/styles.css").toURI().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // ids 

    public String mainContainerId = "mainContainer";
    public String loginFormId = "loginForm";
    public String windowBarID = "windowBar";
    public String tooltipPaneID = "tooltipPane";
    public String navPaneID = "navPane";
    public String lbToolTipMessageID = "lbToolTipMessage";
    public String lbScreenTitleId = "lbScreenTitle";
    public String contentContainerId = "contentContainer";
    public String childMenuId = "apChildMenu";

    //views 
    public Scene scene;
    public Stage stage;

    private AnchorPane tooltipContainer;
    private Label lbToolTipMessage;
    private AnchorPane mainContainer;

    public AnchorPane getTooltipContainer() {
        return tooltipContainer;
    }

    public void setTooltipContainer(AnchorPane tooltipContainer) {
        this.tooltipContainer = tooltipContainer;
    }

    public Label getLbToolTipMessage() {
        return lbToolTipMessage;
    }

    public void setLbToolTipMessage(Label lbToolTipMessage) {
        this.lbToolTipMessage = lbToolTipMessage;
    }

    public void initToolTipView() {
        tooltipContainer = (AnchorPane) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().tooltipPaneID);
        lbToolTipMessage = (Label) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().lbToolTipMessageID);
        mainContainer = (AnchorPane) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().mainContainerId);
    }

    public AnchorPane getMainContainer() {
        return mainContainer;
    }

    public void setMainContainer(AnchorPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void showToolTip(String tooltip) {
        try {
            tooltipContainer.setVisible(true);
            lbToolTipMessage.setText(tooltip);
        } catch (Exception e) {
        }

    }

    public void hideToolTip() {
        try {
            tooltipContainer.setVisible(false);
            lbToolTipMessage.setText("");
        } catch (Exception e) {
        }

    }

}
