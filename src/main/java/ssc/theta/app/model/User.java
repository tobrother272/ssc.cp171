/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;
import ssc.base.annotation.SSCDatabaseField;
import ssc.base.annotation.SSCDatabaseTable;
import ssc.theta.app.api.MetaApi;
import ssc.base.database.BaseModel;
import ssc.base.ui.components.SSCTableColum;

/**
 *
 * @author PC
 */
public class User extends BaseModel {

    public User(int stt, String line) {
        super(stt, line);
    }

    @SSCDatabaseTable(tableName = "sub_user")
    public User(int stt) {
        super(stt);
    }

    @SSCDatabaseTable(tableName = "sub_user")
    public User(int stt, ResultSet rs) {
        super(stt);
        initValueFromResultSet(rs);
    }

    @SSCDatabaseTable(tableName = "sub_user")
    public User(JSONObject object, int stt) {
        super(object, stt);
    }
    @SSCDatabaseField(sql_col_name = "id", sql_col_type = ColType.INTEGER, sql_col_key = true, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private int id;
    @SSCDatabaseField(not_null = true, sql_col_name = "username", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 255, view_name = "Tên profile", view_type = ViewType.TextField)
    private String username;
    @SSCDatabaseField(not_null = true, sql_col_name = "shared", txt_format = true, sql_col_type = ColType.INTEGER, sql_col_tyle_length = 255, view_name = "Tên profile", view_type = ViewType.TextField)
    private int shared;
    @SSCDatabaseField(not_null = true, sql_col_name = "state", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 255, view_name = "Loại profile", view_type = ViewType.TextField)
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getShared() {
        return shared;
    }

    public void setShared(int shared) {
        this.shared = shared;
    }

    public static List<TableColumn> getArrCol(String profileID, ObservableList<User> data) {
        List<TableColumn> arrCol = new ArrayList<>();
        try {

            TableColumn<User, User> actionCol = new TableColumn("Share");
            actionCol.setResizable(false);
            actionCol.setPrefWidth(50);
            actionCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            actionCol.setCellFactory(param -> new TableCell<User, User>() {

                private AnchorPane rootPane;

                @Override
                public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
                    return super.buildEventDispatchChain(tail); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected void updateItem(User gmail, boolean empty) {
                    super.updateItem(gmail, empty);
                    if (gmail == null) {
                        setGraphic(null);
                        return;
                    }
                    try {
                        CheckBox cbShared = new CheckBox("");
                        cbShared.setSelected(gmail.getShared() == 1);
                        cbShared.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                if (gmail.getState().contains("child")) {
                                    if (cbShared.isSelected()) {
                                        if (MetaApi.insertUserProfile(profileID, gmail.getId())) {
                                            cbShared.setSelected(true);
                                        } else {
                                            cbShared.setSelected(false);
                                        }
                                    } else {
                                        if (MetaApi.deleteUserProfile(profileID, gmail.getId())) {
                                            cbShared.setSelected(false);
                                        } else {
                                            cbShared.setSelected(true);
                                        }
                                    }
                                } else {
                                    if (!cbShared.isSelected()) {
                                        if (MetaApi.deleteProfileShare(profileID, gmail.getId())) {
                                            data.remove(gmail);
                                        } else {
                                            cbShared.setSelected(true);
                                        }
                                    }
                                }
                            }
                        });
                        cbShared.setLayoutX(10);
                        cbShared.setLayoutY(20);
                        rootPane = new AnchorPane();
                        rootPane.getStyleClass().addAll("actionCol");
                        rootPane.setPrefSize(50, 50);
                        rootPane.getChildren().addAll(cbShared);

                        setGraphic(rootPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            arrCol.add(actionCol);
            arrCol.add(SSCTableColum.getTableColInteger("STT", "stt", 50, false));
            arrCol.add(SSCTableColum.getTableColString("Username", "username", 250, false));
            arrCol.add(SSCTableColum.getTableColString("Loại", "state", 70, false));
        } catch (Exception e) {
        }
        return arrCol;
    }

}
