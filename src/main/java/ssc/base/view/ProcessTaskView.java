/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import ssc.base.run.ActionWithAccountBase;

/**
 *
 * @author PC
 */
public abstract class ProcessTaskView extends ProcessViewBase {

    private TableView tv;
    private AnchorPane container;
    private ProgressBar prLoading;

    public ProgressBar getLoading() {
        return prLoading;
    }

    public TableView getTv() {
        return tv;
    }

    public void setTv(TableView tv) {
        this.tv = tv;
    }
    private ObservableList listRunning;
    private ObservableList listHistory;
    private ObservableList listSuccess;
    private ObservableList listError;

    public ObservableList getListSuccess() {
        return listSuccess;
    }

    public void setListSuccess(ObservableList listSuccess) {
        this.listSuccess = listSuccess;
    }

    public ObservableList getListError() {
        return listError;
    }

    public void setListError(ObservableList listError) {
        this.listError = listError;
    }

    public ObservableList getListRunning() {
        return listRunning;
    }

    public void setListRunning(ObservableList listRunning) {
        this.listRunning = listRunning;
    }

    public ObservableList getListHistory() {
        return listHistory;
    }

    public void setListHistory(ObservableList listHistory) {
        this.listHistory = listHistory;
    }

    private HBox hboxFilter;

    public HBox getFilterContainer() {
        return hboxFilter;
    }

    public abstract ObservableList initAndSetListRunning();

    public abstract ObservableList initAndSetListHistory();

    @Override
    public int getTotal() {
        return listHistory.size();
    }

    @Override
    public int getCountRunning() {
        return listRunning.size();
    }

    @Override
    public int getSuccess() {
        return listSuccess.size();
    }

    @Override
    public int getError() {
        return listError.size();
    }
    Label lbLableTitle;
    Label lbLableSubTitle;

    public ProcessTaskView(AnchorPane _container, String title) {
        this.container = _container;

        tv = new TableView();
        tv.getStyleClass().add("table-view-material");
        listSuccess = FXCollections.observableArrayList();
        listError = FXCollections.observableArrayList();

        initView(container, 0, 0, container.getPrefWidth(), 150);
        lbLableTitle = new Label("Danh sách tiến trình đang chạy");
        lbLableSubTitle = new Label("Nhấp vào 1 mục (đang chạy,thành công,lỗi) để đổi danh sách tiến trình");
        tv.setPrefSize(container.getPrefWidth() - 20, container.getPrefHeight() - getApStatus().getPrefHeight() - 10);
        tv.setLayoutX(10);
        tv.setLayoutY(155);
        lbLableTitle.setLayoutX(10);
        lbLableTitle.setLayoutY(120);
        lbLableSubTitle.setLayoutX(10);
        lbLableSubTitle.setLayoutY(140);
        lbLableTitle.getStyleClass().add("labelTableHeader");
        lbLableSubTitle.getStyleClass().add("labelTableHeader2");
        container.getChildren().add(tv);
        container.getChildren().add(lbLableTitle);
        container.getChildren().add(lbLableSubTitle);

        prLoading = new ProgressBar();
        prLoading.setVisible(false);
        prLoading.setPrefSize(tv.getPrefWidth(), 3);
        prLoading.setLayoutX(tv.getLayoutX());
        prLoading.setLayoutY(tv.getLayoutY() - 3);
        container.getChildren().add(prLoading);
        listRunning = initAndSetListRunning();
        listHistory = initAndSetListHistory();
        tv.setItems(listRunning);

        tv.setRowFactory(tv -> {
            TableRow<ActionWithAccountBase> row = new TableRow<>();
            row.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    ActionWithAccountBase task = (ActionWithAccountBase) row.getItem();
                    SSCLogView SSLogView = new SSCLogView(task.getArrLog(), task.getArrRemoteLog());
                    SSLogView.getLbMessage().textProperty().bind(task.messageProperty());
                    SSLogView.show();
                }
            });
            return row;
        });

        statusError.getStatusRoot().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                tv.setItems(listError);
                lbLableTitle.setText("Danh sách tiến trình lỗi");
            }
        });
        statusSuccess.getStatusRoot().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                tv.setItems(listSuccess);
                lbLableTitle.setText("Danh sách tiến trình thành công");
            }
        });
        statusThread.getStatusRoot().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                tv.setItems(listRunning);

                lbLableTitle.setText("Danh sách tiến trình đang chạy");
            }
        });
    }

}
