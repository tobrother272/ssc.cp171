/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import ssc.base.ui.components.SSCTextField;
import ssc.base.view.SSCForm;

/**
 *
 * @author PC
 */
public abstract class TaskModalWithInput extends SSCBaseModal {

    public abstract void initTable(TableView tv);

    public abstract void taskAction(String value);
    private SSCTextField tfInputFileData;
    private ObservableList<ResultModelItem> dataResult;

    public ObservableList<ResultModelItem> getDataResult() {
        return dataResult;
    }

    public void setDataResult(ObservableList<ResultModelItem> dataResult) {
        this.dataResult = dataResult;
    }

    public TaskModalWithInput(int widthRate, int height, String title) {
        super(widthRate, height, title, "");
        dataResult = FXCollections.observableArrayList();
        AnchorPane apRoot = new AnchorPane();
        apRoot.setPrefSize(content.getPrefWidth(), 100);
        SSCForm form = new SSCForm(apRoot, title, true) {
            @Override
            public void setOnAction() {

            }
        };

        tfInputFileData = new SSCTextField(form, "tfInputFileData", "File danh sách tài khoản", "", "Click đúp chọn file txt", Arrays.asList("required"));
        tfInputFileData.enableSelectFile("Chọn file txt chứa danh sách tài khoản", new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("Txt", "*.txt")});
        content.getChildren().add(apRoot);
        tfInputFileData.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        taskAction(t1);
                        return null;
                    }
                };
                
                bindTask(task);
                
                Thread thr = new Thread(task);
                thr.setDaemon(true);
                thr.start();
            }
        });

        TableView tvData = new TableView();
        tvData.setLayoutY(apRoot.getLayoutY() + apRoot.getPrefHeight() + 10);
        tvData.getStyleClass().addAll("table-view-material");
        tvData.setPrefSize(content.getPrefWidth(), content.getPrefHeight() - 50);

        content.getChildren().add(tvData);
        initTable(tvData);
        tvData.setItems(dataResult);

    }

}
