/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.database;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import ssc.base.ui.components.ButtonTooltip;
import ssc.base.ui.components.ListSelectItem;
import ssc.base.ui.components.SSCSelectModal;
import ssc.base.global.ViewGlobal;
import ssc.base.task.BaseTask;
import ssc.base.ultil.Graphics;
import ssc.base.view.SSCMessage;

/**
 *
 * @author PC
 */
public abstract class BaseTable {

    private AnchorPane container;
    private TableView tvData;
    public abstract ObservableList initGetData(String query);
    public abstract void removeEvt(ObservableList listRemove);
    public abstract List<TableColumn> initArrCol();
    private ObservableList arrData;
    private HBox listTableMenu;
    private ButtonTooltip btnAddItem;
    private ButtonTooltip btnRemove;
    public void setMenuVisible(Boolean visible) {
        listTableMenu.setVisible(visible);
    }

    public TableView getTvData() {
        return tvData;
    }

    public void setTvData(TableView tvData) {
        this.tvData = tvData;
    }

    public BaseTable(String containerID, ObservableList arrData, String addTitle,List<ListSelectItem> selectItems) {
        container = (AnchorPane) ViewGlobal.getInst().scene.lookup("#" + containerID);

        listTableMenu = new HBox();
        listTableMenu.setPrefSize(700, 40);
        tvData = new TableView();
        tvData.setPrefSize(container.getPrefWidth() - 20, container.getPrefHeight() - 70);
        tvData.getStyleClass().addAll("table-view-material");
        tvData.setLayoutY(60);
        tvData.setLayoutX(10);
        this.arrData = arrData;
        container.getChildren().add(tvData);

        btnAddItem = new ButtonTooltip("Tạo thêm ");
        btnRemove= new ButtonTooltip("Xóa ");
        Graphics.setIconLeft(btnAddItem, "PLUS_SQUARE", 0.7);
        if(addTitle.length()==0){
            btnAddItem.setVisible(false);
        }
        btnAddItem.setText(addTitle);
        btnRemove.setText("Xóa ");
        Graphics.setIconLeft(btnRemove, "REMOVE", 0.7);
        listTableMenu.getChildren().add(btnAddItem);
        listTableMenu.setLayoutX(container.getPrefWidth() - listTableMenu.getPrefWidth() - 10);
        listTableMenu.setLayoutY(10);
        listTableMenu.getStyleClass().addAll("tableMenuBar");
        listTableMenu.setAlignment(Pos.CENTER_RIGHT);
        btnAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SSCSelectModal selectInsertModal = new SSCSelectModal(3, 600, "Chọn hình thức thêm tài khoản ", selectItems) {
                };
                selectInsertModal.show();
            }
        });
        
        btnRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ObservableList dataSelect = tvData.getSelectionModel().getSelectedItems();
                if(dataSelect.size()==0){
                    SSCMessage.showError("Vui lòng chọn danh sách cần xóa");
                    return;
                }
                BaseTask task=new BaseTask() {
                    @Override
                    public boolean mainFunction() {
                         removeEvt(dataSelect);
                         return true;
                    }
                };
                task.start();
            }
        });
        listTableMenu.getChildren().add(btnRemove);
        container.getChildren().add(listTableMenu);
        initTable();
        reloadData("");
    }

    public void reloadData(String query) {
        LoadModelTask lmt = new LoadModelTask(query) {
            @Override
            public ObservableList<BaseModel> getData(String query) {
                return initGetData(query);
            }
        };

        lmt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                arrData.setAll(lmt.getValue());
            }
        });

        lmt.start();

    }

    public void initTable() {
        tvData.getColumns().addAll(initArrCol());
        tvData.setItems(arrData);
        tvData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
