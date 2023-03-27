package com.cjf.opreationdata;

import com.cjf.FileImportForm;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SqlFileExcutThread implements IExcutThread {

    private JTextArea outLog;
    public static CountDownLatch countDownLatch;
    private AtomicInteger atomicInteger;
    private String sql;

    public SqlFileExcutThread( JTextArea outLog,AtomicInteger atomicInteger, String sql,CountDownLatch countDownLatch) {
        this.outLog = outLog;
        this.atomicInteger = atomicInteger;
        this.sql = sql;
        this.countDownLatch = countDownLatch;
        this.outLog = outLog;
    }

    @Override
    public void excut() {
        try {
            Connection connection = FileImportForm.connectionPool1.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.close();
            System.out.println(atomicInteger.getAndIncrement() + "执行SQL!==>" + sql );
            outLog.append(atomicInteger.getAndIncrement() + "执行SQL!==>" + sql + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } catch (Exception e) {
            outLog.append("SQL导入异常!==>" + sql + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } finally {
            countDownLatch.countDown();
        }
    }
}
