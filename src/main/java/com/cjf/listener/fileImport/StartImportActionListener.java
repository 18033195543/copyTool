package com.cjf.listener.fileImport;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class StartImportActionListener implements ActionListener {

    private JTextArea outLog;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public static CountDownLatch countDownLatch;

    public StartImportActionListener(JTextArea outLog) {
        this.outLog = outLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i = JOptionPane.showConfirmDialog(null, "是否开始导入SQL?", "导入SQL", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (JOptionPane.YES_NO_OPTION == i) {// 选择导入sql
            if (FileImportForm.fileCacheMap == null || FileImportForm.fileCacheMap.size() == 0) {
                new MyDialog("请选择导入文件！");
                outLog.append("请选择导入文件！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }
            if (FileImportForm.connectionPool1 == null) {
                new MyDialog("请先获取数据库连接！");
                outLog.append("请先获取数据库连接！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }
            countDownLatch = new CountDownLatch(FileImportForm.fileCacheMap.size());
            long l = System.currentTimeMillis();
            for (String s : FileImportForm.fileCacheMap) {
                FileImportForm.executor.execute(() -> {
                    excuteSql(s, countDownLatch);
                });
            }

            try {
                countDownLatch.await();
                outLog.append("SQL导入完毕!\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                long l1 = System.currentTimeMillis();
                long l2 = l1 - l;
                outLog.append("总耗时==>" + (l2 / 1000) + "秒！");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                FileImportForm.fileCacheMap = null;
                outLog.append("请先获取数据库连接！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // 执行sql
    private void excuteSql(String sql, CountDownLatch cd) {
        try {
            Connection connection = FileImportForm.connectionPool1.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.close();
            outLog.append(atomicInteger.getAndIncrement() + "执行SQL!==>" + sql + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } catch (Exception e) {
            outLog.append("SQL导入异常!==>" + sql + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } finally {
            cd.countDown();
        }
    }
}
