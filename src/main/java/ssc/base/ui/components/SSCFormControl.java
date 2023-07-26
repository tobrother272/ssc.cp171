/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ssc.base.view.SSCForm;
import ssc.base.global.TC;
import ssc.base.ultil.StringUtils;

/**
 *
 * @author PC
 */
public abstract class SSCFormControl {

    public static interface VALIDATES {

        public static String REQUIRED = "REQUIRED";
        public static String NUMBER = "NUMBER";
    }

    public String userData;
    private VBox formGroup;
    private Label lbFor;
    private Label lbMessage;
    private VBox container;
    private List<String> validates;
    public SSCForm form;
    public abstract Node getMainInput();

    public abstract String getValue();

    public abstract void setValue(String value);

    public abstract void initChangeValue();

    public String label;
    
    public SSCFormControl(SSCForm form, String userdata, String label, String defautValue, String placeHolder, List<String> validates) {
        this.userData = userdata;
        this.form=form;
        this.validates = new ArrayList<>();
        this.validates.addAll(validates);
        this.container = form.getListElement();
        this.formGroup = new VBox();
        this.formGroup.setPrefWidth(container.getPrefWidth());
        this.lbFor = new Label(label);
        lbMessage = new Label();
        this.label = label;
        lbFor.setPrefSize(formGroup.getPrefWidth(), 40);

        lbMessage.setPrefSize(formGroup.getPrefWidth(), 40);
        //index 0 
        formGroup.getChildren().add(lbFor);
        //index 1
        lbFor.getStyleClass().setAll("labelFor");
        lbMessage.getStyleClass().setAll("labelMessage");
        formGroup.getStyleClass().setAll("formGroup");
        container.getChildren().add(formGroup);
        formGroup.getChildren().add(1, getMainInput());
        setValue(defautValue);
        initChangeValue();
       
    }
    public void setMessage(String message) {
        if (lbMessage.getText().length() == 0) {
            lbMessage.setText(message);
            formGroup.getChildren().add(lbMessage);
            form.resizeForm(30);
        }
    }
    public void clearMessage() {
        if (formGroup.getChildren().indexOf(lbMessage) >= 0) {
            form.resizeForm(-30);
        }
        lbMessage.setText("");
        formGroup.getChildren().remove(lbMessage);
        //
    }

    public void saveStorageValue() {
        // System.out.println("userData "+getValue());
        TC.getInts().getPre().put(userData, getValue());
    }

    ;
    
    public void loadOldValue() {
        setValue(TC.getInts().getPre().get(userData, ""));
    }

    public boolean isValidate() {
        for (String validate : validates) {
            if (validate.contains(VALIDATES.REQUIRED)) {
                if (getValue().length() == 0 || getValue() == null) {
                    setMessage(label + " không để trống");
                    return false;
                }
            }
            if (validate.contains(VALIDATES.NUMBER)) {
                if(!StringUtils.isNumeric(getValue())){
                    setMessage(label + " phải là số");
                    return false;
                }
            }
        }
        return true;
    }
}
