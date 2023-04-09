package com.cjf.opreationdata;

import com.cjf.FileImportForm;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SqlFileExcutThread implements IExcutThread {

    private JTextArea outLog;
    public static CountDownLatch countDownLatch;
    private AtomicInteger atomicInteger;
    private List<String> sqlList;

    public SqlFileExcutThread(JTextArea outLog, AtomicInteger atomicInteger, List<String> sqlList, CountDownLatch countDownLatch) {
        this.outLog = outLog;
        this.atomicInteger = atomicInteger;
        this.sqlList = sqlList;
        this.countDownLatch = countDownLatch;
        this.outLog = outLog;
    }

    @Override
    public void excut() {
        try {
            Connection connection = FileImportForm.connectionPool1.getConnection();
            connection.setAutoCommit(false);
            for (String sql : sqlList) {
                if (sql.length() == 0) {
                    continue;
                }
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                SqlFileOperateService.insertTotal.incrementAndGet();
            }
            connection.commit();
            connection.close();
            outLog.append("执行SQL!==>第" + atomicInteger.getAndIncrement() + "批\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } catch (Exception e) {
            outLog.append("SQL导入出现异常!==>\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } finally {
            countDownLatch.countDown();
        }
    }
}
