/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import ssc.base.ultil.StringUtils;
import ssc.base.ui.components.SSCTableColum;

/**
 *
 * @author PC
 */
public class TaskLog {

    private long dateTime;
    private String dateTimeString;
    private String title;
    private String content;
    private String image;
    private int logType;

    public String getDateTimeString() {
        return StringUtils.convertLongToDataTime("dd/MM HH:mm:ss", dateTime);
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
    }

    
    public TaskLog(long dateTime, String title, String content, String image, int logType) {
        this.dateTime = dateTime;
        this.title = title;
        this.content = content;
        this.image = image;
        this.logType = logType;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    
    public static List<TableColumn> getArrCol() {
        List<TableColumn> arrCol = new ArrayList<>();
        try {
            arrCol.add(SSCTableColum.getTableColString("Thời gian", "dateTimeString", 100, false));
            arrCol.add(SSCTableColum.getTableColString("Nội dung", "title", 1200, false));
        } catch (Exception e) {
        }
        return arrCol;
    }
    
    
}

