/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.view;

import ssc.base.view.ScreenBase;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ssc.base.global.ViewGlobal;


/**
 *
 * @author PC
 */
public class Navigator {
    private AnchorPane navigatorContainer;
    public VBox menuList;
    private HBox apChildMenu;  
    private Label lbScreenTitle;
    private TabPane contentContainer;
    private List<ScreenBase> arrScreen;
    public Navigator(){
        arrScreen=new ArrayList<>();
        navigatorContainer=(AnchorPane) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().navPaneID);
        lbScreenTitle=(Label) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().lbScreenTitleId);
        contentContainer=(TabPane)ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().contentContainerId);
        apChildMenu=(HBox) ViewGlobal.getInst().scene.lookup("#" + ViewGlobal.getInst().childMenuId);
        menuList=new VBox();
        menuList.setPrefWidth(navigatorContainer.getPrefWidth());
        navigatorContainer.getChildren().add(menuList);
        menuList.setLayoutY(100);
    
    }
    
    public void setTitle(String title){
        lbScreenTitle.setText(title);
    }
    public void setTabIndex(int index){
        for (ScreenBase screenBase : arrScreen) {
            screenBase.clearActive();
        }
        contentContainer.getSelectionModel().select(index);
        apChildMenu.getChildren().clear();
        apChildMenu.getChildren().addAll(arrScreen.get(index).getArrChildMenuBtn());
    }

    public void addScreen(ScreenBase screen){
        arrScreen.add(screen);
    }
}
