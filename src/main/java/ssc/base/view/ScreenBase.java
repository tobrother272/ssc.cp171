/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.view;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import ssc.base.ui.components.ChildMenuButton;
import ssc.base.ultil.Graphics;
/**
 * @author PC
 */
public abstract class ScreenBase {

    private String title;
    private int tabIndex;
    private String menuIcon;
    private Navigator navigator;
    private Button btnMenu;
    private List<ChildMenuButton> arrChildMenuBtn;

    public List<ChildMenuButton> getArrChildMenuBtn() {
        return arrChildMenuBtn;
    }


    
    public void clearActive(){
        btnMenu.getStyleClass().setAll("btn-nav-menu");
    }
    
    public abstract void initView();

    public abstract void reloadView();
    
    public abstract void initArrBtn(List<ChildMenuButton> arrChildMenuBtn);
    
    public ScreenBase(String title, int tabIndex, String menuIcon,Navigator navigator) {
        arrChildMenuBtn=new ArrayList<>();
        this.title = title;
        this.tabIndex = tabIndex;
        this.menuIcon = menuIcon;
        this.navigator=navigator;
        btnMenu = new Button();
        btnMenu.setPrefSize(60, 60);
        navigator.menuList.getChildren().add(btnMenu);
        btnMenu.getStyleClass().setAll("btn-nav-menu");
        Graphics.setIconLeft(btnMenu, menuIcon, 1.5);
        btnMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                navigator.setTitle(title);
                navigator.setTabIndex(tabIndex);
                btnMenu.getStyleClass().setAll("btn-nav-menu-active");
                reloadView();
            }
        });
        initArrBtn(arrChildMenuBtn);
        initView();
    }

}
