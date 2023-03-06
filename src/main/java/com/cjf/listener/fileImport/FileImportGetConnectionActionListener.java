package com.cjf.listener.fileImport;

import com.cjf.FileImportForm;
import com.cjf.MyApp;
import com.cjf.dialog.MyDialog;
import com.cjf.util.C3p0ConnectionUtil3Impl;
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

public class FileImportGetConnectionActionListener implements ActionListener {

    private JComboBox url1;
    private JTextField userName1;
    private JPasswordField password1;
    private JTextArea outLog;
    private JComboBox databaseType;

    public FileImportGetConnectionActionListener(JComboBox url1, JTextField userName1, JPasswordField password1, JTextArea outLog, JComboBox databaseType) {
        this.url1 = url1;
        this.userName1 = userName1;
        this.password1 = password1;
        this.outLog = outLog;
        this.databaseType = databaseType;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int i = Runtime.getRuntime().availableProcessors();
        FileImportForm.executor = new ThreadPoolExecutor(i + 1, (i + 1) + (i + 1) / 2, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2000000));
        outLog.append("连接按钮开始\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        try {
            String url1Text = url1.getSelectedItem() == null ? "" : url1.getSelectedItem().toString();
            if (!StringUtils.nonEmptyString(url1Text)) {
                new MyDialog("url不能为空!");
                outLog.append("url不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            String userNameText1 = userName1.getText();
            if (!StringUtils.nonEmptyString(userNameText1)) {
                new MyDialog("用户名不能为空!");
                outLog.append("用户名不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            String passwordText1 = password1.getText();
            if (!StringUtils.nonEmptyString(passwordText1)) {
                new MyDialog("密码不能为空!");
                outLog.append("密码不能为空!" + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }


            C3p0ConnectionUtil3Impl c3p0ConnectionUtil3 = new C3p0ConnectionUtil3Impl(url1Text, userNameText1, passwordText1, 5, databaseType);
            FileImportForm.connectionPool1 = c3p0ConnectionUtil3.getDataSource();
            updateUrlItem(url1Text);
            System.out.println(FileImportForm.connectionPool1);
            // 保存历史记录

        } catch (Exception exception) {
            new MyDialog(exception.toString());
            outLog.append(exception.toString() + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            return;
        }
        if (FileImportForm.connectionPool1 == null) {
            new MyDialog("数据库获取连接池失败！");
            outLog.append("数据库获取连接池失败！\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            return;
        }

        new MyDialog("数据库获取连接池成功！");
        outLog.append("数据库获取连接池成功！\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
    }

    private void updateUrlItem(String str1) {
        boolean b = false;
        for (String x : MyApp.url_item) {
            if (str1.equals(x)) {
                b = true;
                break;
            }
        }
        if (!b) {
            List<String> list = new ArrayList<>();
            list.add(str1);
            list.addAll(MyApp.url_item);
            MyApp.url_item = list;
            url1.removeAllItems();
            MyApp.url_item.forEach(y -> {
                url1.addItem(y);
            });
        } else {
            MyApp.url_item.remove(str1);
            List<String> list = new ArrayList<>();
            list.add(str1);
            list.addAll(MyApp.url_item);
            MyApp.url_item = list;
            url1.removeAllItems();
            MyApp.url_item.forEach(y -> {
                url1.addItem(y);
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
            for (String s : MyApp.url_item) {
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
