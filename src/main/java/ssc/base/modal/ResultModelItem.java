/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import ssc.base.ultil.StringUtils;
import ssc.base.ui.components.SSCTableColum;

/**
 *
 * @author PC
 */
public class ResultModelItem {

    private String dateTime;
    private String title;
    private int stt;
    public ResultModelItem(int stt,String title) {
        this.dateTime = StringUtils.convertLongToDataTime("dd/MM hh:mm", System.currentTimeMillis());
        this.title = title;
        this.stt=stt;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    
    public static List<TableColumn> getArrCol() {
        List<TableColumn> arrCol = new ArrayList<>();
        arrCol.add(SSCTableColum.getTableColInteger("STT", "stt", 50, false));
        arrCol.add(SSCTableColum.getTableColString("Ngày tạo", "dateTime", 100, false));
        arrCol.add(SSCTableColum.getTableColString("Trạng thái", "title", 400, false));
        return arrCol;
    }

}
