package com.cjf.listener.fileImport;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileImportCloseConnectionActionListener implements ActionListener {
    
    private JTextArea outLog;

    public FileImportCloseConnectionActionListener(JTextArea outLog) {
        this.outLog = outLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        outLog.append("关闭线程池\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        if (FileImportForm.executor != null) {
            FileImportForm.executor.shutdown();
        }
        outLog.append("关闭连接\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        if (FileImportForm.connectionPool1 != null) {
            try {
                FileImportForm.connectionPool1.close();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        if (FileImportForm.connectionPool1 != null) {
            try {
                FileImportForm.connectionPool1.close();
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }
        outLog.append("连接池关闭成功！\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        new MyDialog("连接池关闭成功！");
    }
}
