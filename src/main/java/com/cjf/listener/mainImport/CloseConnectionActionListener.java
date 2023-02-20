package com.cjf.listener.mainImport;

import com.cjf.MainForm;
import com.cjf.dialog.MyDialog;

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
        outLog.append("关闭线程池\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        if (MainForm.executor != null) {
            MainForm.executor.shutdown();
        }
        outLog.append("关闭连接\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        if (MainForm.connectionPool1 != null) {
            try {
                MainForm.connectionPool1.close();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        if (MainForm.connectionPool2 != null) {
            try {
                MainForm.connectionPool2.close();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        outLog.append("连接池关闭成功！\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        new MyDialog("连接池关闭成功！");
    }
}
