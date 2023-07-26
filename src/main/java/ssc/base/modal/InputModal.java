/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ssc.base.view.SSCForm;


/**
 * @author PC
 */
public class InputModal extends SSCBaseModal {


    private SSCForm objectForm;

    public SSCForm getObjectForm() {
        return objectForm;
    }

    public void setObjectForm(SSCForm objectForm) {
        this.objectForm = objectForm;
    }
    
    public InputModal(int widthRate, int height, String title,EventHandler<ActionEvent> event) {
        super(widthRate, height, title, "");
        objectForm = new SSCForm(content, title, true) {
            @Override
            public void setOnAction() {
               
            }
        };
        objectForm.getActionBtn().setOnAction(event);

    }

}
