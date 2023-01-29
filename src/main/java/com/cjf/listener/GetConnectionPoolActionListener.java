package com.cjf.listener;

import com.cjf.MainForm;
import com.cjf.dialog.MyDialog;
import com.cjf.util.C3p0ConnectionUtil1Impl;
import com.cjf.util.C3p0ConnectionUtil2Impl;
import com.mchange.v2.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 获取连接池监听
 */
public class GetConnectionPoolActionListener implements ActionListener {
    private JComboBox url1;
    private JComboBox url2;
    private JTextField userName2;
    private JTextField userName1;
    private JPasswordField password1;
    private JPasswordField password2;
    private JTextArea outLog;
    private JSpinner threadPoolCoreSize;
    private JSpinner threadPoolMaxSize;
    private JSpinner connectionPoolSize;
    private JComboBox databaseType;

    public GetConnectionPoolActionListener(JComboBox url1, JComboBox url2, JTextField userName2,
                                           JTextField userName1, JPasswordField password1,
                                           JPasswordField password2, JTextArea outLog, JSpinner threadPoolCoreSize,
                                           JSpinner threadPoolMaxSize, JSpinner connectionPoolSize, JComboBox databaseType) {
        this.url1 = url1;
        this.url2 = url2;
        this.userName2 = userName2;
        this.userName1 = userName1;
        this.password1 = password1;
        this.password2 = password2;
        this.outLog = outLog;
        this.threadPoolCoreSize = threadPoolCoreSize;
        this.threadPoolMaxSize = threadPoolMaxSize;
        this.connectionPoolSize = connectionPoolSize;
        this.databaseType = databaseType;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (MainForm.isGj) {
            // 开启了高级模式
            MainForm.executor = new ThreadPoolExecutor((int) threadPoolCoreSize.getValue(), (int) threadPoolMaxSize.getValue(), 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2000000));
        } else {
            int i = Runtime.getRuntime().availableProcessors();
            // 开启了高级模式
            MainForm.executor = new ThreadPoolExecutor(i + 1, (i + 1) + (i + 1) / 2, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2000000));
        }
        outLog.append("连接按钮开始\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        try {
            String url1Text = url1.getSelectedItem() == null ? "" : url1.getSelectedItem().toString();
            if (!StringUtils.nonEmptyString(url1Text)) {
                new MyDialog("url1不能为空!");
                outLog.append("url1不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            String userNameText1 = userName1.getText();
            if (!StringUtils.nonEmptyString(userNameText1)) {
                new MyDialog("用户名1不能为空!");
                outLog.append("用户名1不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            String passwordText1 = password1.getText();
            if (!StringUtils.nonEmptyString(passwordText1)) {
                new MyDialog("密码1不能为空!");
                outLog.append("密码1不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            String url2Text = url2.getSelectedItem() == null ? "" : url2.getSelectedItem().toString();
            if (!StringUtils.nonEmptyString(url2Text)) {
                new MyDialog("url2不能为空!");
                outLog.append("url2不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }
            String userNameText2 = userName2.getText();
            if (!StringUtils.nonEmptyString(userNameText2)) {
                new MyDialog("用户名2不能为空!");
                outLog.append("用户名2不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            String passwordText2 = password2.getText();
            if (!StringUtils.nonEmptyString(passwordText2)) {
                new MyDialog("密码2不能为空!");
                outLog.append("密码2不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            C3p0ConnectionUtil1Impl c3p0ConnectionUtil1 = null;
            C3p0ConnectionUtil2Impl c3p0ConnectionUtil2 = null;
            if (MainForm.isGj && connectionPoolSize.getValue() != null) {
                c3p0ConnectionUtil1 = new C3p0ConnectionUtil1Impl(url1Text, userNameText1, passwordText1, (int) connectionPoolSize.getValue(),databaseType);
                c3p0ConnectionUtil2 = new C3p0ConnectionUtil2Impl(url2Text, userNameText2, passwordText2, (int) connectionPoolSize.getValue(),databaseType);
            } else {
                c3p0ConnectionUtil1 = new C3p0ConnectionUtil1Impl(url1Text, userNameText1, passwordText1, 5, databaseType);
                c3p0ConnectionUtil2 = new C3p0ConnectionUtil2Impl(url2Text, userNameText2, passwordText2, 5, databaseType);
            }
            MainForm.connectionPool1 = c3p0ConnectionUtil1.getDataSource();
            MainForm.connectionPool2 = c3p0ConnectionUtil2.getDataSource();
            updateUrlItem(url1Text, 1);
            if (!url1Text.equals(url2Text)) {
                updateUrlItem(url2Text, 2);
            }
            System.out.println(MainForm.connectionPool1);
            System.out.println(MainForm.connectionPool2);
            // 保存历史记录

        } catch (Exception exception) {
            new MyDialog(exception.toString());
            outLog.append(exception.toString() + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            return;
        }
        if (MainForm.connectionPool1 == null) {
            new MyDialog("数据库1获取连接池失败！");
            outLog.append("数据库1获取连接池失败！\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            return;
        }
        if (MainForm.connectionPool2 == null) {
            new MyDialog("数据库2获取连接池失败！");
            outLog.append("数据库2获取连接池失败！\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            return;
        }

        new MyDialog("数据库获取连接池成功！");
        outLog.append("数据库获取连接池成功！\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
    }

    private void updateUrlItem(String str1, int flag) {
        boolean b = false;
        for (String x : MainForm.url_item) {
            if (str1.equals(x)) {
                b = true;
                break;
            }
        }
        if (!b) {
            List<String> list = new ArrayList<>();
            list.add(str1);
            list.addAll(MainForm.url_item);
            MainForm.url_item = list;
            url1.removeAllItems();
            url2.removeAllItems();
            MainForm.url_item.forEach(y -> {
                url1.addItem(y);
                url2.addItem(y);
            });
        } else {
            MainForm.url_item.remove(str1);
            List<String> list = new ArrayList<>();
            list.add(str1);
            list.addAll(MainForm.url_item);
            MainForm.url_item = list;
            if (flag == 1) {
                url1.removeAllItems();
            } else {
                url1.addItem(str1);
            }
            url2.removeAllItems();
            MainForm.url_item.forEach(y -> {
                if (flag == 1) {
                    url1.addItem(y);
                }
                url2.addItem(y);
            });
        }
        updateUrlFile();
    }

    private void updateUrlFile() {
        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\url.txt");
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (String s : MainForm.url_item) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
