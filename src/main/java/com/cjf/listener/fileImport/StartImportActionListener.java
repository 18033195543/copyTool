package com.cjf.listener.fileImport;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;
import com.cjf.entity.ColumnEntity;
import com.cjf.opreationdata.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class StartImportActionListener implements ActionListener {

    private JTextArea outLog;
    private JTextField tableName;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public static CountDownLatch countDownLatch;

    public StartImportActionListener(JTextArea outLog, JTextField tableName) {
        this.outLog = outLog;
        this.tableName = tableName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int i = JOptionPane.showConfirmDialog(null, "是否开始导入SQL?", "导入SQL", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (JOptionPane.YES_NO_OPTION == i) {// 选择导入sql

            if (FileImportForm.connectionPool1 == null) {
                new MyDialog("请先获取数据库连接！");
                outLog.append("请先获取数据库连接！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            if ("*.csv".equals(FileImportForm.ft)) { // 如果是csv文件需要先对数据进行处理成可执行的sql
                if (tableName == null || tableName.getText().length() == 0) {
                    outLog.append("请输入导入表名！\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                    new MyDialog("请输入导入表名！");
                    return;
                }
                FileOperateService csvFileOperateService = new CsvFileOperateService(outLog, atomicInteger, tableName);
                new Thread(()->{
                    try {
                        csvFileOperateService.excute();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();


            } else if ("*.sql".equals(FileImportForm.ft)) {
                if (FileImportForm.fileCacheList == null || FileImportForm.fileCacheList.size() == 0) {
                    new MyDialog("请选择导入文件！");
                    outLog.append("请选择导入文件！\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                    return;
                }
                FileOperateService sqlFileOperateService = new SqlFileOperateService(atomicInteger, outLog);
                new Thread(() -> {
                    try {
                        sqlFileOperateService.excute();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();


            }

        }
    }

}
