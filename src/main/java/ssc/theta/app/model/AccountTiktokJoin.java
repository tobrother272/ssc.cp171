/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.model;

import ssc.base.annotation.SSCDatabaseField;
import ssc.base.annotation.SSCDatabaseTable;
import ssc.base.database.BaseModel;

/**
 *
 * @author PC
 */
public class AccountTiktokJoin extends BaseModel {

    @SSCDatabaseTable(tableName = "gmail_tiktok")
    public AccountTiktokJoin(int stt, String line) {
        super(stt, line);
    }

    @SSCDatabaseTable(tableName = "gmail_tiktok")
    public AccountTiktokJoin(int stt) {
        super(stt);
    }

    @SSCDatabaseTable(tableName = "gmail_tiktok")
    public AccountTiktokJoin(int stt, String username, long account_id) {
        super(0);
        this.username = username;
        this.account_id = account_id;
    }

    @SSCDatabaseField(sql_col_name = "username", sql_col_type = ColType.VARCHAR, sql_col_foren_key = true, sql_col_tyle_length = 120, view_type = ViewType.NOVIEW)
    private String username;
    @SSCDatabaseField(sql_col_name = "account_id", sql_col_type = ColType.BIGINT, sql_col_foren_key = true, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private long account_id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

}
