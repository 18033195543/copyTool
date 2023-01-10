package com.cjf.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class C3p0ConnectionUtil {

    public static Connection getConnection() throws SQLException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource("");
        Connection connection = comboPooledDataSource.getConnection();
        return null;
    }
}
