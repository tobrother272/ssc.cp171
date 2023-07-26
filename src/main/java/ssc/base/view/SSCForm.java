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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ssc.base.ui.components.SSCFormControl;
import ssc.base.ui.components.SSCLoadingButton;

/**
 *
 * @author PC
 */
public abstract class SSCForm {

    private VBox listElement;
    private List<SSCFormControl> arrFromControl;
    private SSCLoadingButton actionButton;
    private AnchorPane container;

    public abstract void setOnAction();

    public VBox getListElement() {
        return listElement;
    }

    public void setListElement(VBox listElement) {
        this.listElement = listElement;
    }

    public boolean changeContainer = false;

    public void setChangeContainer(boolean changeContainer) {
        this.changeContainer = changeContainer;
    }

    private ScrollPane scrollContainer;

    public List<String> getValues() {
        List<String> arrValues = new ArrayList<>();
        for (SSCFormControl sSCFormControl : arrFromControl) {
            arrValues.add(sSCFormControl.getValue());
        }
        return arrValues;
    }

    public SSCLoadingButton getActionBtn() {
        return actionButton;
    }

    public boolean pinButton;

    public SSCForm(AnchorPane container, String btnTitle, boolean pinButton) {

        this.arrFromControl = new ArrayList<>();
        scrollContainer = new ScrollPane();
        this.container = container;
        this.pinButton = pinButton;
        this.listElement = new VBox();
        this.listElement.setPrefWidth(pinButton?container.getPrefWidth()-20:container.getPrefWidth());
        scrollContainer.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollContainer.setPrefWidth(container.getPrefWidth());

        scrollContainer.setContent(listElement);
        container.getChildren().add(scrollContainer);
        
        actionButton = new SSCLoadingButton(btnTitle, container.getPrefWidth(), 50);
        actionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (!checkValidates()) {
                    return;
                }
                setOnAction();
            }
        });

        scrollContainer.setPrefHeight(container.getPrefHeight() - 40);
        scrollContainer.getStyleClass().setAll("loginContainer");
        container.getChildren().add(actionButton.getView());
        if (pinButton) {
            scrollContainer.setPrefHeight(container.getPrefHeight());
            actionButton.setLayoutY(scrollContainer.getLayoutY() + scrollContainer.getPrefHeight() + 20);
        } else {
            actionButton.setLayoutY(listElement.getLayoutY() + listElement.getPrefHeight() + 20);
        }

    }

    public void addChild(SSCFormControl child) {
        arrFromControl.add(child);
        listElement.setPrefHeight(arrFromControl.size() * 80);
        if (!pinButton) {
            actionButton.setLayoutY(listElement.getLayoutY() + listElement.getPrefHeight() + 20);
        }

    }

    public void saveStorageValue() {
        for (SSCFormControl formControl : arrFromControl) {
            //System.out.println(formControl.userData+" aaaaaaaaaaa");
            formControl.saveStorageValue();
        }
    }

    public void loadStorageValue() {
        for (SSCFormControl formControl : arrFromControl) {
            formControl.loadOldValue();
        }
    }

    public void resizeForm(int size) {
        if (changeContainer) {
            container.setPrefHeight(container.getPrefHeight() + size);
            scrollContainer.setPrefHeight(container.getPrefHeight() - 40);
        }
        listElement.setPrefHeight(listElement.getPrefHeight() + size);
        if (!pinButton) {
            actionButton.setLayoutY(listElement.getLayoutY() + listElement.getPrefHeight() + 20);
        }

    }

    private boolean checkValidates() {
        Boolean validate = true;
        for (SSCFormControl formControl : arrFromControl) {
            if (!formControl.isValidate()) {
                validate = false;
            }
        }
        return validate;
    }

}
