/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.database;

import ssc.base.global.SQLiteConnection;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.json.simple.JSONObject;
import ssc.base.annotation.SSCDatabaseField;
import ssc.base.annotation.SSCDatabaseTable;
import ssc.base.ui.components.SSCCombobox;
import ssc.base.ui.components.SSCFormControl.VALIDATES;
import ssc.base.modal.SSCObjectModal;
import ssc.base.ui.components.SSCTextField;
import ssc.base.modal.ResultModelItem;
import ssc.base.modal.SSCEditObjectModal;
import ssc.base.modal.TaskModalWithInput;
import ssc.base.view.SSCForm;
import static ssc.base.database.SQLIteHelper.executeQuery;
import static ssc.base.database.SQLIteHelper.getIntegerFromRS;
import static ssc.base.database.SQLIteHelper.getLongFromRS;
import static ssc.base.database.SQLIteHelper.getStringFromRS;
import ssc.theta.app.model.GoogleAccount;
import ssc.base.ultil.MyFileUtils;
import ssc.theta.app.model.sqlQuery.GoogleAccountQuery;

/**
 *
 * @author PC
 */
public abstract class BaseModel {

    public static interface ColType {

        public static String INTEGER = "INTEGER";
        public static String Text = "Text";
        public static String VARCHAR = "VARCHAR";
        public static String BIGINT = "BIGINT";
    }

    public static interface ViewType {

        public static int NOVIEW = -1;
        public static int TextField = 0;
        public static int TextArea = 1;
        public static int ComboBox = 2;
        public static int CheckBox = 3;
        public static int CheckComboBox = 4;
        public static int ComboBoxWithButton = 5;
        public static int TextFieldFolder = 6;
    }

    private int stt;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public BaseModel(int stt, String line) {
        this.stt = stt;
        initValueFromLineString(line);
    }

    public BaseModel(JSONObject object, int stt) {
        initValueFromJSONObject(object, stt);
    }

    public String getLineString() {
        BaseModel _this = this;
        String format = "";
        try {
            List<String> arrF = new ArrayList<>();
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getAnnotation(SSCDatabaseField.class) != null && field.getAnnotation(SSCDatabaseField.class).txt_format()) {
                    arrF.add(field.get(_this).toString());
                }
            }
            format = String.join("|", arrF);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return format;
    }

    /**
     * show format import txt
     *
     * @return
     */
    public String getHeaderTxt() {
        String format = "";
        try {
            List<String> arrF = new ArrayList<>();
            for (Field field : getClass().getDeclaredFields()) {
                if (field.getAnnotation(SSCDatabaseField.class) != null && field.getAnnotation(SSCDatabaseField.class).txt_format()) {
                    arrF.add(field.getAnnotation(SSCDatabaseField.class).sql_col_name());
                }
            }
            format = String.join("|", arrF);
        } catch (Exception e) {
        }

        return format;
    }

    public BaseModel(String line, int stt) {
        this.stt = stt;
        initValueFromLineString(line);
    }

    public BaseModel(int stt) {
        this.stt = stt;
        String queryCreateTable = "";
        boolean firstCol = true;
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                try {
                    field.setAccessible(true);
                    if (firstCol) {
                        firstCol = false;
                        queryCreateTable = field.getAnnotation(SSCDatabaseField.class).sql_col_name()
                                + " " + field.getAnnotation(SSCDatabaseField.class).sql_col_type() + (field.getAnnotation(SSCDatabaseField.class).sql_col_tyle_length() == 0 ? "" : "(" + field.getAnnotation(SSCDatabaseField.class).sql_col_tyle_length() + ")") + " " + (field.getAnnotation(SSCDatabaseField.class).sql_col_key() ? " PRIMARY KEY " : "");
                    } else {
                        queryCreateTable = queryCreateTable + "," + field.getAnnotation(SSCDatabaseField.class).sql_col_name()
                                + " " + field.getAnnotation(SSCDatabaseField.class).sql_col_type() + (field.getAnnotation(SSCDatabaseField.class).sql_col_tyle_length() == 0 ? "" : "(" + field.getAnnotation(SSCDatabaseField.class).sql_col_tyle_length() + ")") + " " + (field.getAnnotation(SSCDatabaseField.class).sql_col_key() ? " PRIMARY KEY " : "");
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
        String query = "CREATE TABLE IF NOT EXISTS " + getClass().getConstructors()[0].getAnnotation(SSCDatabaseTable.class).tableName() + " (" + queryCreateTable + ");";

        //TC.log(query, "BaseModel", "CREATE TABLE");
        executeQuery(query);
    }

    public Map<String, String> getPostData() {
        Map<String, String> data = new HashMap<String, String>();

        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                field.setAccessible(true);
                //System.out.println(field.getAnnotation(SSCDatabaseField.class).sql_col_name() + " " + getValue(field));
                data.put(field.getAnnotation(SSCDatabaseField.class).sql_col_name(), getValue(field));
            }
        }
        return data;
    }

    public static void AddDataFromFileTxt(ObservableList arrData) {
        TaskModalWithInput sCTaskModalWtihInput = new TaskModalWithInput(2, 600, "Thêm tài khoản từ file") {
            @Override
            public void initTable(TableView tv) {
                tv.getColumns().addAll(ResultModelItem.getArrCol());
            }

            @Override
            public void taskAction(String value) {
                List<String> arrLine = MyFileUtils.getListStringFromFile(value);
                for (String string : arrLine) {
                    GoogleAccount ga = new GoogleAccount(arrData.size() + 1, string);
                    if(GoogleAccountQuery.checkEmailHave(ga.getUsername())){
                        getDataResult().add(new ResultModelItem(arrLine.indexOf(string),ga.getUsername()+ " đã có trong DB"));
                        continue;
                    }
                    String insertRes = "";
                    if ((insertRes = ga.insertData()).length() == 0) {
                        arrData.add(ga);
                        getDataResult().add(new ResultModelItem(arrLine.indexOf(string), "Đã thêm " + string));
                    } else {
                        getDataResult().add(new ResultModelItem(arrLine.indexOf(string), "Thêm " + string + " lỗi : " + insertRes));
                    }
                }
            }
        };
        sCTaskModalWtihInput.show();
    }

    public void showAddViewFromDB(ObservableList arrData, String title) {
        SSCObjectModal sscom = new SSCObjectModal(3, 600, title, this, arrData) {
            @Override
            public BaseModel initAction(BaseModel object) {
                //GoogleAccount ga = (GoogleAccount) object;
                String insertMessage = object.insertData();
                if (insertMessage.length() == 0) {
                    return object;
                }
                return null;
            }
        };
        sscom.show();
    }

    public void showEditViewFromDB(ObservableList arrData, String title) {
        SSCEditObjectModal sscom = new SSCEditObjectModal(3, 600, title, this, arrData) {
            @Override
            public BaseModel initAction(BaseModel object) {
                //GoogleAccount ga = (GoogleAccount) object;
                String insertMessage = object.updateData();
                if (insertMessage.length() == 0) {
                    return object;
                }
                return null;
            }
        };
        sscom.show();
    }

    public void initValueFromResultSet(ResultSet rs) {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                try {
                    field.setAccessible(true);
                    if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.INTEGER)) {
                        field.set(this, getIntegerFromRS(field.getAnnotation(SSCDatabaseField.class).sql_col_name(), rs));
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.Text)) {
                        field.set(this, getStringFromRS(field.getAnnotation(SSCDatabaseField.class).sql_col_name(), rs));
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.VARCHAR)) {
                        field.set(this, getStringFromRS(field.getAnnotation(SSCDatabaseField.class).sql_col_name(), rs));
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.BIGINT)) {
                        field.set(this, getLongFromRS(field.getAnnotation(SSCDatabaseField.class).sql_col_name(), rs));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initValueFromLineString(String line) {

        List<String> arrValue = new ArrayList<>();
        arrValue.addAll(Arrays.asList(line.split("\\|")));

        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null && field.getAnnotation(SSCDatabaseField.class).txt_index() != -1) {
                try {
                    field.setAccessible(true);
                    if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.INTEGER)) {
                        field.set(this, Integer.parseInt(arrValue.get(field.getAnnotation(SSCDatabaseField.class).txt_index())));
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.Text)) {
                        field.set(this, arrValue.get(field.getAnnotation(SSCDatabaseField.class).txt_index()));
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.VARCHAR)) {
                        field.set(this, arrValue.get(field.getAnnotation(SSCDatabaseField.class).txt_index()));
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.BIGINT)) {
                        field.set(this, Long.parseLong(arrValue.get(field.getAnnotation(SSCDatabaseField.class).txt_index())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initValueFromJSONObject(JSONObject object, int stt) {
        this.stt = stt;
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                try {
                    field.setAccessible(true);
                    if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.INTEGER)) {
                        field.set(this, Integer.parseInt(object.get(field.getAnnotation(SSCDatabaseField.class).sql_col_name()).toString()));
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.Text)) {
                        field.set(this, object.get(field.getAnnotation(SSCDatabaseField.class).sql_col_name()).toString());
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.VARCHAR)) {
                        field.set(this, object.get(field.getAnnotation(SSCDatabaseField.class).sql_col_name()).toString());
                    } else if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().equals(ColType.BIGINT)) {
                        field.set(this, Long.parseLong(object.get(field.getAnnotation(SSCDatabaseField.class).sql_col_name()).toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initObjectView(SSCForm form) {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                try {
                    field.setAccessible(true);

                    SSCDatabaseField ano = field.getAnnotation(SSCDatabaseField.class);

                    if (ano.view_type() == ViewType.NOVIEW) {
                        continue;
                    }
                    List<String> validates = new ArrayList<>();

                    if (ano.not_null()) {
                        validates.add(VALIDATES.REQUIRED);
                    }
                    if (ano.sql_col_type().equals(ColType.INTEGER) || ano.sql_col_type().equals(ColType.BIGINT)) {
                        validates.add(VALIDATES.NUMBER);
                    }
                    BaseModel thisOBJ = this;
                    if (ano.view_type() == ViewType.ComboBox) {
                        ObservableList<String> dataSelect = FXCollections.observableArrayList();
                        dataSelect.addAll(Arrays.asList(ano.defaultValue()));
                        SSCCombobox combobox = new SSCCombobox(form, ano.sql_col_name(), ano.view_name(), "", "Chọn " + ano.view_name(), validates, dataSelect);
                        form.addChild(combobox);
                        combobox.valueProperty().addListener(new ChangeListener() {
                            @Override
                            public void changed(ObservableValue ov, Object t, Object t1) {
                                try {
                                    field.set(thisOBJ, t1.toString());
                                } catch (Exception e) {
                                }
                            }
                        });
                        combobox.setValue(ano.start_value());
                    } else {
                        SSCTextField textField = new SSCTextField(form, ano.sql_col_name(), ano.view_name(), "", "Nhập " + ano.view_name(), validates);
                        form.addChild(textField);
                        textField.textProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                                try {
                                    if (ano.sql_col_type().equals(ColType.INTEGER)) {
                                        field.set(thisOBJ, Integer.parseInt(t1));
                                    } else if (ano.sql_col_type().equals(ColType.BIGINT)) {
                                        field.set(thisOBJ, Long.parseLong(t1));
                                    } else {
                                        field.set(thisOBJ, t1);
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                        textField.setValue(ano.start_value());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initEditObjectView(SSCForm form) {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                try {
                    field.setAccessible(true);

                    SSCDatabaseField ano = field.getAnnotation(SSCDatabaseField.class);

                    if (ano.view_type() == ViewType.NOVIEW) {
                        continue;
                    }
                    List<String> validates = new ArrayList<>();

                    if (ano.not_null()) {
                        validates.add(VALIDATES.REQUIRED);
                    }
                    if (ano.sql_col_type().equals(ColType.INTEGER) || ano.sql_col_type().equals(ColType.BIGINT)) {
                        validates.add(VALIDATES.NUMBER);
                    }
                    BaseModel thisOBJ = this;
                    if (ano.view_type() == ViewType.ComboBox) {
                        ObservableList<String> dataSelect = FXCollections.observableArrayList();
                        dataSelect.addAll(Arrays.asList(ano.defaultValue()));
                        SSCCombobox combobox = new SSCCombobox(form, ano.sql_col_name(), ano.view_name(), "", "Chọn " + ano.view_name(), validates, dataSelect);
                        form.addChild(combobox);
                        combobox.valueProperty().addListener(new ChangeListener() {
                            @Override
                            public void changed(ObservableValue ov, Object t, Object t1) {
                                try {
                                    field.set(thisOBJ, t1.toString());
                                } catch (Exception e) {
                                }
                            }
                        });
                        combobox.setValue(getValue(field));
                    } else {
                        SSCTextField textField = new SSCTextField(form, ano.sql_col_name(), ano.view_name(), "", "Nhập " + ano.view_name(), validates);
                        form.addChild(textField);
                        textField.textProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                                try {
                                    if (ano.sql_col_type().equals(ColType.INTEGER)) {
                                        field.set(thisOBJ, Integer.parseInt(t1));
                                    } else if (ano.sql_col_type().equals(ColType.BIGINT)) {
                                        field.set(thisOBJ, Long.parseLong(t1));
                                    } else {
                                        field.set(thisOBJ, t1);
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                        textField.setValue(getValue(field));
                    }

                } catch (Exception e) {
                    
                    
                    e.printStackTrace();
                }
            }
        }
    }

    public String deleteData() {
        String keyBuild = "";
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                if (field.getAnnotation(SSCDatabaseField.class).sql_col_key()) {
                    keyBuild = field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field);
                }
            }
        }
        String query = "delete from " + getClass().getConstructors()[0].getAnnotation(SSCDatabaseTable.class).tableName() + " where " + keyBuild;
        return executeQuery(query);
    }

    public String updateData() {
        boolean firstCol = true;
        String fieldBuild = "";
        String keyBuild = "";
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                if (!field.getAnnotation(SSCDatabaseField.class).sql_col_key()) {
                    if (firstCol) {
                        firstCol = false;
                        fieldBuild = field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field);
                    } else {
                        fieldBuild = fieldBuild + "," + field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field);
                    }
                } else {
                    keyBuild = field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field);
                }

            }
        }
        String query = "update " + getClass().getConstructors()[0].getAnnotation(SSCDatabaseTable.class).tableName() + " set " + fieldBuild + " where " + keyBuild;
        //System.out.println("query "+query);
        return executeQuery(query);
    }

    public String getValue(Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(this);
            return (value == null ? "" : value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getValueInsertDB(Field field) {
        if (field.getAnnotation(SSCDatabaseField.class).sql_col_type().contains(ColType.Text) || field.getAnnotation(SSCDatabaseField.class).sql_col_type().contains(ColType.VARCHAR)) {
            try {
                field.setAccessible(true);
                Object value = field.get(this);
                return "'" + (value == null ? "" : SQLIteHelper.enCodeSQLString(value.toString())) + "'";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                field.setAccessible(true);
                Object value = field.get(this);
                return value.toString();
            } catch (Exception e) {
                e.printStackTrace();
                //Notification.ListNotificaiton.getInstance().showErrorNotification(field.getName() + " Dữ liệu SAI " + e.getMessage(), "");
            }
        }
        return "";
    }

    public String insertData() {
        boolean firstCol = true;
        String fieldBuild = "";
        String valueBuild = "";
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                if (firstCol) {
                    firstCol = false;
                    fieldBuild = field.getAnnotation(SSCDatabaseField.class).sql_col_name();
                    String value = getValueInsertDB(field);
                    valueBuild = field.getAnnotation(SSCDatabaseField.class).sql_col_key() ? (value.length() == 0 ? "'" + System.currentTimeMillis() + "'" : value) : value;
                } else {
                    fieldBuild = fieldBuild + "," + field.getAnnotation(SSCDatabaseField.class).sql_col_name();
                    valueBuild = valueBuild + "," + (field.getAnnotation(SSCDatabaseField.class).sql_col_key() ? "" + System.currentTimeMillis() : getValueInsertDB(field));
                }
            }
        }
        String query = "INSERT INTO " + getClass().getConstructors()[0].getAnnotation(SSCDatabaseTable.class).tableName() + " (" + fieldBuild + ")VALUES(" + valueBuild + ")";
        //System.out.println("Insert Data from BaseModel "+query);
        String insertMessage = executeQuery(query);
        return insertMessage;
    }

    public String insertForeData() {
        boolean firstCol = true;
        String fieldBuild = "";
        String valueBuild = "";
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null) {
                if (firstCol) {
                    firstCol = false;
                    fieldBuild = field.getAnnotation(SSCDatabaseField.class).sql_col_name();
                    String value = getValueInsertDB(field);
                    valueBuild = field.getAnnotation(SSCDatabaseField.class).sql_col_key() ? (value.length() == 0 ? "'" + System.currentTimeMillis() + "'" : value) : value;
                } else {
                    fieldBuild = fieldBuild + "," + field.getAnnotation(SSCDatabaseField.class).sql_col_name();
                    valueBuild = valueBuild + "," + (field.getAnnotation(SSCDatabaseField.class).sql_col_key() ? "" + System.currentTimeMillis() : getValueInsertDB(field));
                }
            }
        }

        if (isExits()) {
            return "Đã có record này";
        }

        String query = "INSERT INTO " + getClass().getConstructors()[0].getAnnotation(SSCDatabaseTable.class).tableName() + " (" + fieldBuild + ")VALUES(" + valueBuild + ")";
        //System.out.println("Insert Data from BaseModel "+query);
        String insertMessage = executeQuery(query);
        return insertMessage;
    }

    private boolean isExits() {
        boolean firstCol = true;
        List<String> arrWhere = new ArrayList<>();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null && field.getAnnotation(SSCDatabaseField.class).sql_col_foren_key()) {
                if (firstCol) {
                    firstCol = false;
                    arrWhere.add(field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field));
                } else {
                    arrWhere.add(" and " + field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field));
                }
            }
        }
        String query = "Select * from " + getClass().getConstructors()[0].getAnnotation(SSCDatabaseTable.class).tableName() + " where " + String.join(" ", arrWhere);
        Statement stmt = null;
        ResultSet rs = null;
        Connection c = SQLiteConnection.getInstance().getConnection();
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                rs.close();
                stmt.close();
                return true;
            }
            rs.close();
            stmt.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public String deleteFore() {
        boolean firstCol = true;
        List<String> arrWhere = new ArrayList<>();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SSCDatabaseField.class) != null && field.getAnnotation(SSCDatabaseField.class).sql_col_foren_key()) {
                if (firstCol) {
                    firstCol = false;
                    arrWhere.add(field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field));
                } else {
                    arrWhere.add(" and " + field.getAnnotation(SSCDatabaseField.class).sql_col_name() + "=" + getValueInsertDB(field));
                }
            }
        }
        String query = "Delete from " + getClass().getConstructors()[0].getAnnotation(SSCDatabaseTable.class).tableName() + " where " + String.join(" ", arrWhere);
       //System.out.println("Insert Data from BaseModel "+query);
        String insertMessage = executeQuery(query);
        return insertMessage;
    }

}
