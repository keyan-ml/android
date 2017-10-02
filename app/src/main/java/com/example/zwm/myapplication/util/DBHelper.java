package com.example.zwm.myapplication.util;

/**
 * Created by zwm on 17-10-2.
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.example.zwm.myapplication.model.User;


public class DBHelper {
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://127.0.0.1/test_db";
    public static final String USER = "root";
    public static final String PASSWORD = "12345";

    public Connection conn = null;
    public PreparedStatement pstatement = null;

    public DBHelper() {
        try {
            Class.forName(JDBC_DRIVER); // 注册JDBC驱动
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); // 获得连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int insert(User user) {
        try {
            if (!containsUemailaddress(user.getUemailaddress())) {
                String sql = "insert into user values(\'"
                        + user.getUemailaddress() + "\', \'"
                        + user.getUname() + "\', \'"
                        + user.getUorganization() + "\', \'"
                        + user.getUcontactway() + "\', \'"
                        + user.getUdatetime() + "\');";

                pstatement = conn.prepareStatement(sql);
                int i = pstatement.executeUpdate();
                return i;
            }
            else {
                System.out.println("表中已有相应记录，不能再插入。");
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean containsUemailaddress(String uemailaddress) {
        try {
            String sql = "select uemailaddress from user "
                    + " where uemailaddress=\'" + uemailaddress + "\';";

            pstatement = conn.prepareStatement(sql);
            ResultSet rs = pstatement.executeQuery();
            if (rs.next()) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public ResultSet selectSql(String sql) {
        try {
            pstatement = conn.prepareStatement(sql);
            return pstatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            this.conn.close();
            this.pstatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
