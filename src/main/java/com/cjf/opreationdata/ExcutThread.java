package com.cjf.opreationdata;

import com.cjf.MainForm;
import com.cjf.util.C3p0ConnectionUtil1Impl;
import com.cjf.util.C3p0ConnectionUtil2Impl;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ExcutThread {
    private int a;
    private int b;
    private String outSql;
    private String tableName;
    private JTextArea outLog;
    public static CountDownLatch countDownLatch;

    public ExcutThread(int a, int b, String outSql, String tableName, JTextArea outLog, CountDownLatch countDownLatch) {
        this.a = a;
        this.b = b;
        this.outSql = outSql;
        this.tableName = tableName;
        this.outLog = outLog;
        this.countDownLatch = countDownLatch;
    }

    public void excut() throws SQLException {
        try {
            Connection connection = MainForm.connectionPool1.getConnection();
            String sql = null;
            if (MainForm.isGj && "mysql".equals(MainForm.dbType)) {
                sql = "select * from (" + outSql + ") a limit ?, ?";
            } else if (MainForm.isGj && "oracle".equals(MainForm.dbType)) {
                sql = "select * from  ( select rownum n,e.* from ( " + outSql + " ) e) where n > ?  and n <= ?";
            }

            System.out.println(sql + " 参数：->" + a + "->" + b);
            outLog.append(sql + " 参数：->" + a + "->" + b + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, a);
            preparedStatement.setInt(2, b);
            ResultSet resultSet = preparedStatement.executeQuery();

            // 解析结果
            Map<String, Object> stringObjectMap = resolveResult(resultSet);
            preparedStatement.close();
            C3p0ConnectionUtil1Impl.releaseConnection(connection);

            List<Map> list = (List<Map>) stringObjectMap.get("resultList");
            List<String> list1 = (List<String>) stringObjectMap.get("columnNames");

            insertDate(tableName, list1, list);
        }catch (Exception e){
            e.printStackTrace();
            outLog.append(Thread.currentThread().getName() + "---->" + e.getMessage() + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        }finally {
            countDownLatch.countDown();
        }

    }

    /**
     * 解析结果
     *
     * @param resultSet
     * @return
     */
    private static Map<String, Object> resolveResult(ResultSet resultSet) throws SQLException {
        List<Map> list = new ArrayList<>();
        boolean c = true;
        List<String> list1 = new ArrayList<>();
        while (resultSet.next()) {
            Map row = new HashMap();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int i = 1;
            boolean b = true;
            while (b && c) {
                try {
                    list1.add(metaData.getColumnLabel(i));
                    i++;
                } catch (SQLException e) {
                    b = false;
                    c = false;
                }
            }

            for (String s : list1) {
                row.put(s, resultSet.getObject(s));
            }

            list.add(row);
        }
        resultSet.close();
        Map<String, Object> map = new HashMap<>(2);
        map.put("resultList", list);
        map.put("columnNames", list1);
        return map;
    }


    private void insertDate(String tableName, List<String> columns, List<Map> dateRowList) throws SQLException {
        if (MainForm.isGj && "oracle".equals(MainForm.dbType)) {
            columns.remove(0);
        }
        String insertSql = "insert into " + tableName + " ( ";
        String collect = columns.stream().collect(Collectors.joining(","));
        insertSql += collect;
        insertSql += ") values (";
        String collect1 = columns.stream().map(x -> "?").collect(Collectors.joining(","));
        insertSql += collect1;
        insertSql += ")";
        Connection connection = MainForm.connectionPool2.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(insertSql);
            // 组装参数
            int count = 0;
            for (Map row : dateRowList) {
                for (int i1 = 0; i1 < columns.size(); i1++) {
                    preparedStatement.setObject(i1 + 1, row.get(columns.get(i1)));
                }
                preparedStatement.addBatch();
                count++;
                if (count % 1000 == 0 || count == dateRowList.size()) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                    System.out.println(Thread.currentThread().getName() + "---->" + count);
                    outLog.append(Thread.currentThread().getName() + "---->" + count + "\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                }
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            outLog.append(Thread.currentThread().getName() + "---->" + e.getMessage() + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            C3p0ConnectionUtil2Impl.releaseConnection(connection);
        } finally {
            C3p0ConnectionUtil2Impl.releaseConnection(connection);

        }

    }
}
