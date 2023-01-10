package com.cjf.listener;

import com.cjf.MainForm;
import com.cjf.dialog.MyDialog;
import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseConnectionActionListener implements ActionListener {

    private JTextArea outLog;

    public CloseConnectionActionListener(JTextArea outLog) {
        this.outLog = outLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("关闭连接");
        outLog.append("关闭连接\n");
        if (MainForm.connectionPool1 != null) {
            try {
                MainForm.connectionPool1.destory();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        if (MainForm.connectionPool2 != null) {
            try {
                MainForm.connectionPool2.destory();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        outLog.append("连接池关闭成功！\n");
        new MyDialog("连接池关闭成功！");
    }
}
