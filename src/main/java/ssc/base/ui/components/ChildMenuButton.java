/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ssc.base.ultil.Graphics;

/**
 *
 * @author PC
 */
public class ChildMenuButton extends ButtonTooltip {
    private String text;
    private String ico;
    public ChildMenuButton(String text,String toolTip,String ico,EventHandler<ActionEvent> event) {
        super(text,toolTip);
        Graphics.setIconLeft(this, ico, 1.0);
        this.setOnAction(event);
    }
     
}
