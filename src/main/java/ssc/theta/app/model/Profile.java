/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONObject;

/**
 *
 * @author PC
 */
public class Profile {

    private String id;
    private String fake_data;
    private long date_insert;
    private int admin_id;
    private String proxy;
    private String user_agent;
    private String screen_solution;
    private String language;
    private String platform;
    private int cpu;
    private int ram;
    private String canvas;
    private String name;
    private String tag;
    private String note;
    private int folder_id;
    private String folder_name;
    private String audio;
    private String rectangle;
    private String proxy_type;
    private String opengl;
    //
    
    
    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getRectangle() {
        return rectangle;
    }

    public void setRectangle(String rectangle) {
        this.rectangle = rectangle;
    }

    public String getProxy_type() {
        return proxy_type;
    }

    public void setProxy_type(String proxy_type) {
        this.proxy_type = proxy_type;
    }

    public String getOpengl() {
        return opengl;
    }

    public void setOpengl(String opengl) {
        this.opengl = opengl;
    }
    
    
    
    
    
    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getScreen_solution() {
        return screen_solution;
    }

    public void setScreen_solution(String screen_solution) {
        this.screen_solution = screen_solution;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    public Profile() {
        this.audio="noise";
        this.opengl="noise";
        this.rectangle="noise";
    }
    public Profile(String username) {
        this.name=username;
        this.canvas="noise";
        this.platform="win";
        this.screen_solution="1280x720";
        this.cpu=4;
        this.ram=4;
        this.proxy="";
        this.tag="";
        this.language="en-US,en;q=0.9";
        this.user_agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36";
        this.audio="noise";
        this.opengl="noise";
        this.rectangle="noise";
    }

    private boolean isThere(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException sqlex) {

        }

        return false;
    }

    public Profile(ResultSet rs) {
        try {
            this.admin_id = rs.getInt("admin_id");
            this.date_insert = rs.getLong("date_insert");
            this.id = rs.getString("id");
            this.fake_data = rs.getString("fake_data");
            //
            this.proxy = rs.getString("proxy");
            this.user_agent = rs.getString("user_agent");
            this.screen_solution = rs.getString("screen_solution");
            this.language = rs.getString("language");
            this.platform = rs.getString("platform");
            this.cpu = rs.getInt("cpu");
            this.ram = rs.getInt("ram");
            this.canvas = rs.getString("canvas");
            this.name = rs.getString("name");
            this.tag = rs.getString("tag");
            this.note = rs.getString("note");
            //
            this.audio = rs.getString("audio");
            this.rectangle = rs.getString("rectangle");
            this.proxy_type = rs.getString("proxy_type");
            this.opengl = rs.getString("opengl");
            
            if (isThere(rs, "folder_name")) {
                this.folder_name = rs.getString("folder_name");
            }
            if (isThere(rs, "folder_id")) {
                this.folder_id = rs.getInt("folder_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonObj(String shareState) {
        JSONObject obj = new JSONObject();
        obj.put("admin_id", admin_id);
        obj.put("fake_data", fake_data);
        obj.put("id", id);
        obj.put("date_insert", date_insert);
        //
        obj.put("proxy", proxy);
        obj.put("user_agent", user_agent);
        obj.put("screen_solution", screen_solution);
        obj.put("language", language);
        obj.put("platform", platform);
        obj.put("cpu", cpu);
        obj.put("ram", ram);
        obj.put("note", note);
        obj.put("canvas", canvas);
        obj.put("name", name);
        obj.put("tag", tag);
        obj.put("state", shareState);
        obj.put("folder_id", folder_id);
        obj.put("folder_name", folder_name);
        //
        obj.put("audio", audio);
        obj.put("rectangle", rectangle);
        obj.put("proxy_type", proxy_type);
        obj.put("opengl", opengl);
    
        return obj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFake_data() {
        return fake_data;
    }

    public void setFake_data(String fake_data) {
        this.fake_data = fake_data;
    }

    public long getDate_insert() {
        return date_insert;
    }

    public void setDate_insert(long date_insert) {
        this.date_insert = date_insert;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

}
