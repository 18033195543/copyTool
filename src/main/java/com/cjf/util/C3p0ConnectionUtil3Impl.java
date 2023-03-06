package com.cjf.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3p0ConnectionUtil3Impl {

    private static String url;
    private static String username;
    private static String password;
    private static JComboBox databaseType;
    private static int intiConnectionNum = 1;

    public C3p0ConnectionUtil3Impl(String url, String username, String password, int intiConnectionNum, JComboBox databaseType) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.intiConnectionNum = intiConnectionNum;
        this.databaseType = databaseType;

    }

    public ComboPooledDataSource getDataSource() throws PropertyVetoException, SQLException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        if ("mysql".equals(databaseType.getSelectedItem())) {
            comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        } else if ("oracle".equals(databaseType.getSelectedItem())) {
            comboPooledDataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
        }else {
            comboPooledDataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
        }
        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(username);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setInitialPoolSize(intiConnectionNum);
        Connection connection = comboPooledDataSource.getConnection();
        connection.close();
        return comboPooledDataSource;
    }

    public static void releaseConnection(Connection connection) {
        if(connection!=null){
            try{
                connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
            connection = null;
        }
    }

}
