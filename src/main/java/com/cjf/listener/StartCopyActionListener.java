package com.cjf.listener;

import com.cjf.MainForm;
import com.cjf.dialog.MyDialog;
import com.cjf.opreationdata.DataOperateService;
import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 开始拷贝数据按钮监听
 */
public class StartCopyActionListener implements ActionListener {


    private JTextArea getSql;

    private JTextField tableName;
    private JTextArea outLog;

    public StartCopyActionListener(JTextArea getSql, JTextField tableName, JTextArea outLog) {
        this.getSql = getSql;
        this.tableName = tableName;
        this.outLog = outLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("开始按钮");
        outLog.append("开始按钮\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        if (MainForm.connectionPool1 == null) {
            new MyDialog("请先获取数据库1的连接池 !");
            return;
        }
        if (MainForm.connectionPool2 == null) {
            new MyDialog("请先获取数据库2的连接池 !");
            return;
        }
        if (getSql == null || getSql.getText() == null || "".equals(getSql.getText())) {
            new MyDialog("请输入输出sql !");
            return;
        }
        if (tableName == null || tableName.getText() == null || "".equals(tableName.getText())) {
            new MyDialog("请输入表名 !");
            return;
        }

        DataOperateService dataOperateService = new DataOperateService(getSql.getText(), tableName.getText(),outLog);
        try {
            dataOperateService.reportLeaning();
            outLog.append("成功 !\n");
            new MyDialog("成功 !");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } catch (Exception exception) {
            new MyDialog(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
