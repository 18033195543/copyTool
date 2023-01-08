package com.cjf.listener;

import com.cjf.dialog.MyDialog;
import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseConnectionActionListener implements ActionListener {
    private ConnectionPoolImpl1 connectionPool1;
    private ConnectionPoolImpl2 connectionPool2;

    public CloseConnectionActionListener(ConnectionPoolImpl1 connectionPool1, ConnectionPoolImpl2 connectionPool2) {
        this.connectionPool1 = connectionPool1;
        this.connectionPool2 = connectionPool2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("关闭连接");
        if (connectionPool1 != null) {
            try {
                connectionPool1.destory();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        if (connectionPool2 != null) {
            try {
                connectionPool2.destory();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }

        new MyDialog("连接池关闭成功！");
    }
}
