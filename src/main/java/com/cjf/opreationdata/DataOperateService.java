package com.cjf.opreationdata;


import com.cjf.MainForm;
import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 两个数据库之间导数据工具类
 */
public class DataOperateService {

    private String outSql;
    private String tableName;

    public DataOperateService(String outSql, String tableName) {
        this.outSql = outSql;
        this.tableName = tableName;
    }

    public void reportLeaning() throws SQLException {
        Connection connection = MainForm.connectionPool1.getConnection();
        String countSql = "select count(*) co from ( " + outSql + " ) ";
        PreparedStatement preparedStatement1 = connection.prepareStatement(countSql);
        ResultSet countResult = preparedStatement1.executeQuery();

        List<Integer> list3 = new ArrayList<>();
        while (countResult.next()) {
            list3.add(countResult.getInt("co"));
        }
        int count = 0;
        Integer total = list3.get(0);
        MainForm.connectionPool1.releaseConnection(connection);
        int split = total / 20000;
        int a = total % 20000;
        if (a != 0) {
            split++;
        }
        for (int i = 0; i < split; i++) {
            ExcutThread excutThread = new ExcutThread(count, count += 20000, outSql, tableName);
            MainForm.executor.execute(() -> {
                try {
                    excutThread.excut();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

    }
}
