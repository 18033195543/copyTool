package com.cjf.opreationdata;

import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcutThread {
    private int a;
    private int b;
    private ConnectionPoolImpl1 connectionPoolImpl1;
    private ConnectionPoolImpl2 connectionPoolImpl2;
    private String outSql;
    private String tableName;

    public ExcutThread(int a, int b, ConnectionPoolImpl1 connectionPoolImpl1, ConnectionPoolImpl2 connectionPoolImpl2, String outSql, String tableName) {
        this.a = a;
        this.b = b;
        this.connectionPoolImpl1 = connectionPoolImpl1;
        this.connectionPoolImpl2 = connectionPoolImpl2;
        this.outSql = outSql;
        this.tableName = tableName;
    }

    public void excut() throws SQLException {
        Connection connection = connectionPoolImpl1.getConnection();
        String sql = "SELECT * FROM  ( select rownum n,e.* from ( " + outSql + " ) e) where n > ?  and n <= ?";
        System.out.println(sql + " 参数：->" + a + "->" + b);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, a);
        preparedStatement.setInt(2, b);
        ResultSet resultSet = preparedStatement.executeQuery();

        // 解析结果
        Map<String, Object> stringObjectMap = resolveResult(resultSet);
        preparedStatement.close();
        connectionPoolImpl1.releaseConnection(connection);

        List<Map> list = (List<Map>) stringObjectMap.get("resultList");
        List<String> list1 = (List<String>) stringObjectMap.get("columnNames");

        insertDate(connectionPoolImpl2, tableName, list1, list);
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


    private static void insertDate(ConnectionPoolImpl2 connectionPoolImpl2, String tableName, List<String> columns, List<Map> dateRowList) {
        columns.remove(0);
        String insertSql = "insert into " + tableName + " ( ";
        String collect = columns.stream().collect(Collectors.joining(","));
        insertSql += collect;
        insertSql += ") values (";
        String collect1 = columns.stream().map(x -> "?").collect(Collectors.joining(","));
        insertSql += collect1;
        insertSql += ")";
        Connection connection = connectionPoolImpl2.getConnection();
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
                    System.out.println(Thread.currentThread().getName()+"---->"+count);
                }
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            connectionPoolImpl2.releaseConnection(connection);
        } finally {
            connectionPoolImpl2.releaseConnection(connection);

        }

    }
}
