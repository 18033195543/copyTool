package com.cjf.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolImpl1 implements ConnectionPool {
    private AtomicInteger connectioCount = new AtomicInteger();
    private Integer maxSize;
    private String url;
    private String username;
    private String password;


    private static LinkedBlockingQueue<Connection> idle;
    private static LinkedBlockingQueue<Connection> busy;

    public ConnectionPoolImpl1(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void init(Integer maxSize) {
        this.maxSize = maxSize;
        this.idle = new LinkedBlockingQueue<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            System.out.println("ConnectionPoolImpl1生产连接：" + (i + 1));
            idle.offer(DataSouce.getConnection(url, username, password));
        }
        this.busy = new LinkedBlockingQueue<>(maxSize);
    }

    @Override
    public void destory() {
        if (busy.size() == 0) {
            try {
                Connection con;
                while ((con = idle.poll()) != null) {
                    if (con != null) {
                        con.close();
                        System.out.println("ConnectionPoolImpl1:close one connection..");
                    }
                }
                idle = null;
                busy = null;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    @Override
    public Connection getConnection() {
        Connection connection = idle.poll();

        if (connection != null) {
            System.out.println("ConnectionPoolImpl1获取连接：" + connection);
            busy.offer(connection);
            return connection;
        }

        if (connectioCount.get() < maxSize) {
            if (connectioCount.incrementAndGet() <= maxSize) {
                connection = DataSouce.getConnection(url, username, password);
                System.out.println("ConnectionPoolImpl1生产连接：" + connection);
                busy.offer(connection);
                return connection;
            }
        }

        try {
            connection = idle.poll(30, TimeUnit.SECONDS);
            busy.offer(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) {
        System.out.println("ConnectionPoolImpl1:release connection .." + connection);
        busy.remove(connection);
        idle.offer(connection);
    }
}
