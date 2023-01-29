package com.cjf.util;

import com.cjf.MainForm;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3p0ConnectionUtil1Impl {

    private static String url;
    private static String username;
    private static String password;
    private JComboBox databaseType;
    private static int intiConnectionNum = 1;

    public C3p0ConnectionUtil1Impl(String url, String username, String password, int intiConnectionNum, JComboBox databaseType) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.intiConnectionNum = intiConnectionNum;
        this.databaseType = databaseType;
    }

    public ComboPooledDataSource getDataSource() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        if (MainForm.isGj && "mysql".equals(databaseType.getSelectedItem())) {
            comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        } else if (MainForm.isGj && "oracle".equals(databaseType.getSelectedItem())) {
            comboPooledDataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
        }else {
            comboPooledDataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
        }
        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(username);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setInitialPoolSize(intiConnectionNum);
        return comboPooledDataSource;
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

}
