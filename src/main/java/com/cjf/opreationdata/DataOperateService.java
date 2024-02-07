package com.cjf.opreationdata;


import com.cjf.MainForm;
import com.cjf.util.C3p0ConnectionUtil1Impl;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 两个数据库之间导数据工具类
 */
public class DataOperateService {

    private String outSql;
    private String tableName;
    private JTextArea outLog;

    public DataOperateService(String outSql, String tableName, JTextArea outLog) {
        this.outSql = outSql;
        this.tableName = tableName;
        this.outLog = outLog;
    }

    public void reportLeaning() throws SQLException {
        Connection connection = MainForm.connectionPool1.getConnection();
        String countSql = "select count(*) co from ( " + outSql + " ) a ";
        PreparedStatement preparedStatement1 = connection.prepareStatement(countSql);
        ResultSet countResult = preparedStatement1.executeQuery();

        List<Integer> list3 = new ArrayList<>();
        while (countResult.next()) {
            list3.add(countResult.getInt("co"));
        }
        int count = 0;
        Integer total = list3.get(0);
        C3p0ConnectionUtil1Impl.releaseConnection(connection);
        int split = total / 20000;
        int a = total % 20000;
        if (a != 0) {
            split++;
        }
        CountDownLatch countDownLatch = new CountDownLatch(split);
        for (int i = 0; i < split; i++) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ExcutThread excutThread = null;
            if (MainForm.isGj && "mysql".equals(MainForm.dbType)) {
                excutThread = new ExcutThread(count, 20000, outSql, tableName, outLog, countDownLatch);
                count += 20000;
            } else if (MainForm.isGj && "oracle".equals(MainForm.dbType)) {
                excutThread = new ExcutThread(count, count += 20000, outSql, tableName, outLog, countDownLatch);
            } else{
                excutThread = new ExcutThread(count, count += 20000, outSql, tableName, outLog, countDownLatch);
            }
            ExcutThread finalExcutThread = excutThread;
            MainForm.executor.execute(() -> {
                try {
                    finalExcutThread.excut();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        try {
            countDownLatch.await();
            outLog.append("结束！\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            System.out.println("结束！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
