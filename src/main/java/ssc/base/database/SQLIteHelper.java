/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.database;

import ssc.base.global.SQLiteConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.prefs.Preferences;
import static ssc.base.global.SQLiteConnection.DATABASE;
import ssc.base.global.TC;

/**
 *
 * @author PC
 */
public class SQLIteHelper {

    public static Connection initConnection() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE);
            c.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
        if (c == null) {
            System.err.println("null");
        }
        return c;
    }

    public static Connection getConnectionFromDBFile(String DBFile) {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + DBFile);
            c.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
        if (c == null) {
            System.err.println("null");
        }
        return c;
    }

    public static void createTable(String sqlQuery) {
        Statement stmt = null;
        try {
            Connection c = SQLiteConnection.getInstance().getConnection();
            stmt = c.createStatement();
            stmt.executeUpdate(sqlQuery);
            stmt.close();
            //c.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
    }

    public static Boolean checkColumExist(String tableName, String colName) {
        Connection c = SQLiteConnection.getInstance().getConnection();
        String query = "select * from " + tableName;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            rs.findColumn(colName);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    public static String getStringFromRS(String indexString, ResultSet rs) {
        String result = "";
        try {
            if (rs.getString(indexString) != null) {
                result = SQLIteHelper.deCodeSQLString(rs.getString(indexString));
            }
        } catch (Exception e) {

        }
        return result;
    }

    public static int getIntegerFromRS(String indexString, ResultSet rs, int _default) {
        int result = _default;
        try {
            result = rs.getInt(indexString);
        } catch (Exception e) {

        }
        return result;
    }

    public static long getLongFromRS(String indexString, ResultSet rs) {
        long result = -1;
        try {
            result = rs.getLong(indexString);
        } catch (Exception e) {

        }
        return result;
    }

    public static int getIntegerFromRS(String indexString, ResultSet rs) {
        int result = -1;
        try {
            result = rs.getInt(indexString);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static String enCodeSQLString(String inString) {
        return inString.replaceAll("'", "#nhaydon#");
    }

    public static String deCodeSQLString(String inString) {
        return inString.replaceAll("#nhaydon#", "'");
    }

    public static String executeQuery(String query) {
        Statement stmt = null;
        Connection c = SQLiteConnection.getInstance().getConnection();
        try {
            stmt = c.createStatement();
            stmt.executeUpdate(query);
            //System.out.println("query "+query);
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage() + " \n " + query;
        } finally {
            try {
                stmt.close();
                //c.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Column {

        private String col_name;
        private String col_table;
        public static String dup_column_prefix = "duplicate column name: ";

        public String getCol_table() {
            return col_table;
        }

        public void setCol_table(String col_table) {
            this.col_table = col_table;
        }

        public String getCol_name() {
            return col_name;
        }

        public void setCol_name(String col_name) {
            this.col_name = col_name;
        }

        public Column(String col_name, String col_type, String col_default, String col_table) {
            this.col_name = col_name;
            this.col_type = col_type;
            this.col_default = col_default.length() == 0 ? "" : "DEFAULT '" + col_default + "'";
            this.col_table = col_table;
        }

        public Column(String col_name, String col_type, int col_default, String col_table) {
            this.col_name = col_name;
            this.col_type = col_type;
            this.col_default = "DEFAULT " + col_default;
            this.col_table = col_table;
        }

        public Column(String col_name, String col_type, String col_table) {
            this.col_name = col_name;
            this.col_type = col_type;
            this.col_default = "";
            this.col_table = col_table;
        }

        public String getCol_type() {
            return col_type;
        }

        public void setCol_type(String col_type) {
            this.col_type = col_type;
        }

        public String getCol_default() {
            return col_default;
        }

        public void setCol_default(String col_default) {
            this.col_default = col_default;
        }
        private String col_type;
        private String col_default;

    }

    public static void createTableFolderVideo(Connection c) {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS FolderVideo(\n"
                    + "  video_id    VARCHAR (13)  PRIMARY KEY,\n"
                    + "  video_list     TEXT,\n"
                    + "  music_background VARCHAR (140) \n"
                    + ");");
            stmt.close();
            // c.commit();
            c.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void alterTable(Column col) {
        Preferences pre = TC.getInts().getPre();
        //if (pre.get(Column.dup_column_prefix + col.getCol_name() + " " + col.getCol_table(), "false").equals("true")) {
        //    return;
        //}
        Statement stmt = null;
        try {
            Connection c = SQLiteConnection.getInstance().getConnection();
            stmt = c.createStatement();
            stmt.executeUpdate("alter table " + col.getCol_table() + " add column " + col.getCol_name() + " " + col.getCol_type() + " " + col.getCol_default());
            stmt.close();
            //c.commit();
            pre.put(Column.dup_column_prefix + col.getCol_name() + " " + col.getCol_table(), "");
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + col.getCol_table());
            pre.put(e.getMessage() + " " + col.getCol_table(), "true");
        }
    }
}
