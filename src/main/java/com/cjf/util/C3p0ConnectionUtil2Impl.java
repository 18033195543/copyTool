package com.cjf.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class C3p0ConnectionUtil2Impl {

    private static String url;
    private static String username;
    private static String password;
    private static int intiConnectionNum = 1;

    public C3p0ConnectionUtil2Impl(String url, String username, String password, int intiConnectionNum) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.intiConnectionNum = intiConnectionNum;

    }

    public ComboPooledDataSource getDataSource() throws SQLException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(username);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setInitialPoolSize(intiConnectionNum);

        Connection connection = comboPooledDataSource.getConnection();
        releaseConnection(connection);


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
