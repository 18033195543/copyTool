package com.cjf.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSouce {

    public static Connection getConnection(String url,String username,String password) {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
