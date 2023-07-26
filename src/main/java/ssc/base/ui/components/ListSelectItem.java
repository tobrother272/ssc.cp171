/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author PC
 */
public class ListSelectItem {
    private String title; 
    private EventHandler<ActionEvent> event;

    public ListSelectItem(String title, EventHandler<ActionEvent> event) {
        this.title = title;
        this.event = event;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EventHandler<ActionEvent> getEvent() {
        return event;
    }

    public void setEvent(EventHandler<ActionEvent> event) {
        this.event = event;
    }
    
    
}
