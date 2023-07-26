/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author PC
 */
public class Graphics {

    static double initialX;
    static double initialY;

    public static void setIconLeft(Labeled control, String icon, Double size) {
        control.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.valueOf(icon), size + "em"));
        
    }

    public static void setIconCenter(Labeled control, String icon, Double size) {
        control.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.valueOf(icon), size + "em"));
    }

    public static void setIconRight(Labeled control, String icon, Double size) {
       control.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.valueOf(icon), size + "em"));
    }

    public static void addDragListeners(final Node n) {
        n.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    initialX = me.getSceneX();
                    initialY = me.getSceneY();
                } else {
                    n.getScene().getWindow().centerOnScreen();
                    initialX = n.getScene().getWindow().getX();
                    initialY = n.getScene().getWindow().getY();
                }
            }
        });
        n.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    n.getScene().getWindow().setX(me.getScreenX() - initialX);
                    n.getScene().getWindow().setY(me.getScreenY() - initialY);
                }
            }
        });
    }
}
