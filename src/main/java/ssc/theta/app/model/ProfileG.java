/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;
import ssc.base.annotation.SSCDatabaseField;
import ssc.base.annotation.SSCDatabaseTable;
import ssc.base.database.BaseModel;
import ssc.base.database.BaseModel.ColType;
import ssc.base.database.BaseModel.ViewType;
import ssc.base.global.SQLiteConnection;
import ssc.base.ultil.StringUtils;

/**
 * @author PC
 */
public class ProfileG extends BaseModel {

    @SSCDatabaseField(sql_col_name = "id", sql_col_type = ColType.VARCHAR, sql_col_key = true, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private String id;
    @SSCDatabaseField(not_null = true, sql_col_name = "name", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 255, view_name = "Tên profile", view_type = ViewType.TextField)
    private String name;
    @SSCDatabaseField(not_null = true, start_value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36", sql_col_name = "user_agent", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 255, view_name = "Useragent", view_type = ViewType.TextField)
    private String user_agent;
    @SSCDatabaseField(not_null = false, sql_col_name = "fake_data", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 10000, view_name = "Tên profile", view_type = ViewType.NOVIEW)
    private String fake_data;
    @SSCDatabaseField(not_null = true, start_value = "off", sql_col_name = "canvas", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 10, view_name = "Canvas mode", view_type = ViewType.ComboBox, defaultValue = {"noise", "off"})
    private String canvas;
    @SSCDatabaseField(not_null = false, sql_col_name = "date_insert", txt_format = true, sql_col_type = ColType.BIGINT, sql_col_tyle_length = 140, view_name = "Ngày tạo", view_type = ViewType.NOVIEW)
    private long date_insert;
    @SSCDatabaseField(not_null = true, start_value = "8", sql_col_name = "cpu", txt_format = true, sql_col_type = ColType.INTEGER, sql_col_tyle_length = 5, view_name = "Số Core Cpu", view_type = ViewType.TextField)
    private int cpu;
    @SSCDatabaseField(not_null = true, start_value = "8", sql_col_name = "ram", txt_format = true, sql_col_type = ColType.INTEGER, sql_col_tyle_length = 5, view_name = "Dung lượng ram", view_type = ViewType.TextField)
    private int ram;
    @SSCDatabaseField(not_null = true, start_value = "en-US,en;q=0.9", sql_col_name = "language", txt_format = true, sql_col_type = ColType.VARCHAR, view_name = "Ngôn ngữ", view_type = ViewType.TextField)
    private String language;
    @SSCDatabaseField(not_null = true, start_value = "1280x720", sql_col_name = "screen_solution", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 20, view_name = "Kích thước trình duyệt", view_type = ViewType.TextField)
    private String screen_solution;
    @SSCDatabaseField(not_null = true, start_value = "win", sql_col_name = "platform", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 20, view_name = "Hệ điều hành", view_type = ViewType.ComboBox, defaultValue = {"win", "mac", "linux"})
    private String platform;
    @SSCDatabaseField(not_null = false, sql_col_name = "proxy", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 140, view_name = "Proxy", view_type = ViewType.TextField)
    private String proxy;
    @SSCDatabaseField(not_null = true, sql_col_name = "admin_id", txt_format = true, sql_col_type = ColType.INTEGER, sql_col_tyle_length = 5, view_name = "Admin Id", view_type = ViewType.NOVIEW)
    private int admin_id;
    @SSCDatabaseField(not_null = false, sql_col_name = "tag", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 255, view_name = "Tags", view_type = ViewType.TextField)
    private String tag;
    @SSCDatabaseField(not_null = false, sql_col_name = "state", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 255, view_name = "state", view_type = ViewType.NOVIEW)
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String date_insertString;

    public String getDate_insertString() {
        return StringUtils.convertLongToDataTime("dd/MM HH:mm", date_insert);
    }

    public void setDate_insertString(String date_insertString) {
        this.date_insertString = StringUtils.getDateTimeStringByFormat(platform);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getFake_data() {
        return fake_data;
    }

    public void setFake_data(String fake_data) {
        this.fake_data = fake_data;
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    public long getDate_insert() {
        return date_insert;
    }

    public void setDate_insert(long date_insert) {
        this.date_insert = date_insert;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreen_solution() {
        return screen_solution;
    }

    public void setScreen_solution(String screen_solution) {
        this.screen_solution = screen_solution;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    
    @SSCDatabaseTable(tableName = "gologin_profile")
    public ProfileG(int stt) {
        super(stt);
    }
    @SSCDatabaseTable(tableName = "gologin_profile")
    public ProfileG(int stt,GoogleAccount account) {
        super(stt);
        setName(account.getUsername());
        setCanvas("noise");
        setPlatform("win");
        setTag("");
        setScreen_solution("1280x720");
        setCpu(4);
        setRam(4);
        setLanguage("en-US,en;q=0.9");
        setUser_agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
    }
    @SSCDatabaseTable(tableName = "gologin_profile")
    public ProfileG(int stt, ResultSet rs) {
        super(stt);
        initValueFromResultSet(rs);
    }

    @SSCDatabaseTable(tableName = "gologin_profile")
    public ProfileG(JSONObject object, int stt) {
        super(object, stt);
    }

    public static ObservableList<ProfileG> getData(String _query) {
        ObservableList<ProfileG> datas = FXCollections.observableArrayList();
        String query = "select * from  gologin_profile " + _query;
        //System.out.println("query "+query);
        Statement stmt = null;
        ResultSet rs = null;
        Connection c = SQLiteConnection.getInstance().getConnection();
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            int stt = 1;
            while (rs.next()) {
                ProfileG profile = new ProfileG(stt++, rs);
                if (profile != null) {
                    datas.add(profile);
                }
            }
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {

            }
        }
    }


}
