/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import ssc.base.view.SSCForm;

/**
 *
 * @author PC
 */
public class SSCPasswordField extends SSCFormControl {

    public SSCPasswordField(SSCForm form, String userdata, String label, String defautValue, String placeHolder,List<String> validates) {
        super(form, userdata, label, defautValue, placeHolder,validates);
    }

    private PasswordField txt;

    @Override
    public Node getMainInput() {
        txt = new PasswordField(); 
        return txt;
    }

    @Override
    public String getValue() {
        return txt.getText();
    }

    @Override
    public void setValue(String value) {
        txt.setText(value);
    }

    @Override
    public void initChangeValue() {
        txt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                clearMessage();
            }
        });
    }

}
