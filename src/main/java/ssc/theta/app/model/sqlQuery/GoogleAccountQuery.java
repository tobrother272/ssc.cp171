/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.model.sqlQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ssc.base.database.SQLIteHelper;
import static ssc.base.database.SQLIteHelper.executeQuery;
import static ssc.base.database.SQLIteHelper.getLongFromRS;
import ssc.base.global.SQLiteConnection;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_LOGIN_STATUS;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_STATUS;

/**
 *
 * @author PC
 */
public class GoogleAccountQuery {

    public static ObservableList<GoogleAccount> getListGoogleAccount(String whereQuery) {
        ObservableList<GoogleAccount> data = FXCollections.observableArrayList();
        try {
            String query = " Select *,count(tiktok_id) as countVideo,g.id as aid,g.username as gusername from gmail_account g "
                    + " left join  (select * from gmail_tiktok bt , tiktok_video tv where tv.nickname=bt.username ) as tt "
                    + " on tt.account_id=g.id  group by g.id";
            Statement stmt = null;
            ResultSet rs = null;
            Connection c = SQLiteConnection.getInstance().getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                GoogleAccount account = new GoogleAccount(data.size() + 1, rs);

                if (account != null) {
                    account.setId(getLongFromRS("aid", rs));
                    account.setUsername(SQLIteHelper.getStringFromRS("gusername", rs));
                    data.add(account);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Boolean checkEmail(String gmail_recover) {
        boolean have = false;
        try {
            String query = "select * from gmail_account where gmail_recover='" + gmail_recover + "'";
            Statement stmt = null;
            ResultSet rs = null;
            Connection c = SQLiteConnection.getInstance().getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                have = true;
            }
        } catch (Exception e) {
        }
        return have;
    }

    public static Boolean checkEmailHave(String username) {
        boolean have = false;
        try {
            String query = "select * from gmail_account where username='" + username + "'";
            Statement stmt = null;
            ResultSet rs = null;
            Connection c = SQLiteConnection.getInstance().getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                have = true;
            }
        } catch (Exception e) {
        }
        return have;
    }
    public static GoogleAccount getAccountView(String listId) {
        try {
            String query = "select * from gmail_account where note=1 "
                    + " and status ='" + ACCOUNT_STATUS.LIVE.getValue() + "' "
                    + " and login_status ='" + ACCOUNT_LOGIN_STATUS.LOGINED.getValue() + "' "
                    //+ " and premium_expired > " + System.currentTimeMillis() + " "
                    + " and id not in (" + listId + ") order by last_time asc ";
            Statement stmt = null;
            ResultSet rs = null;
            Connection c = SQLiteConnection.getInstance().getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                GoogleAccount account = new GoogleAccount(0, rs);
                if (account != null) {
                    return account;

                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void resetTime() {
        try {
            String query = "update gmail_account set last_time=" + (System.currentTimeMillis() - (1000 * 6 * 60 * 60)) + " where 1";
            executeQuery(query);
        } catch (Exception e) {
        }
    }

    public static void resetGoThe() {
        try {
            String query = "update gmail_account set out_card=0 where ("+System.currentTimeMillis()+"-out_card)/1000/60/60/24 >1  ";
            System.out.println("query "+query);
            executeQuery(query);
        } catch (Exception e) {
        }
    }
}
