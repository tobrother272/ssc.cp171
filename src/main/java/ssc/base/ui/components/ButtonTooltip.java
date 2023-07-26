/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import ssc.base.global.ViewGlobal;
import ssc.base.task.CheckHoverTask;

/**
 *
 * @author PC
 */
public class ButtonTooltip extends Button {

    private Thread t;
    private CheckHoverTask cht;
    private String toolTip;

    public void initToolTip() {
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cht = new CheckHoverTask();
                cht.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ViewGlobal.getInst().showToolTip(toolTip);
                    }
                });
                t = new Thread(cht);
                t.setDaemon(true);
                t.start();
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                t.stop();
                cht.cancel(true);
                ViewGlobal.getInst().hideToolTip();
            }
        });
    }

    public ButtonTooltip(String toolTip) {
        super();
        initToolTip();
        this.toolTip = toolTip;
    }

    public ButtonTooltip(String text, String toolTip) {
        super(text);
        initToolTip();
        this.toolTip = toolTip;
    }

    public ButtonTooltip(String text, Node node, String toolTip) {
        super(text, node);
        initToolTip();
        this.toolTip = toolTip;
    }

}
