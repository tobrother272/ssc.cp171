/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import ssc.base.view.SSCForm;
import ssc.base.global.TC;

/**
 *
 * @author PC
 */
public class SSCCombobox extends SSCFormControl {

    public SSCCombobox(SSCForm form, String userdata, String label, String defautValue, String placeHolder, List<String> validates, ObservableList dataSelect) {
        super(form, userdata, label, defautValue, placeHolder, validates);
        this.dataSelect = dataSelect;
        comboBox.setItems(dataSelect);
        comboBox.getSelectionModel().selectFirst();
    }

    private ComboBox comboBox;
    private AnchorPane apInputContainer;
    private ObservableList dataSelect;

    @Override
    public Node getMainInput() {
        apInputContainer = new AnchorPane();
        apInputContainer.setPrefWidth(form.getListElement().getPrefWidth());
        comboBox = new ComboBox();
        comboBox.setPrefWidth(apInputContainer.getPrefWidth());
        apInputContainer.getChildren().add(comboBox);
        return apInputContainer;
    }

    public ObjectProperty valueProperty() {
        return comboBox.valueProperty();
    }

    @Override
    public String getValue() {
        return comboBox.getValue().toString();
    }

    @Override
    public void setValue(String value) {
        comboBox.getSelectionModel().select(value);
    }

    @Override
    public void loadOldValue() {
        setValue(TC.getInts().getPre().get(userData, ""));
    }

    @Override
    public void initChangeValue() {
        comboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                clearMessage();
            }
        });
    }

}
