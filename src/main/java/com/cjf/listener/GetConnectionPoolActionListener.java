package com.cjf.listener;

import com.cjf.MainForm;
import com.cjf.dialog.MyDialog;
import com.cjf.util.C3p0ConnectionUtil1Impl;
import com.cjf.util.C3p0ConnectionUtil2Impl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public GetConnectionPoolActionListener(JComboBox url1, JComboBox url2, JTextField userName2,
                                           JTextField userName1, JPasswordField password1,
                                           JPasswordField password2, JTextArea outLog) {
        this.url1 = url1;
        this.url2 = url2;
        this.userName2 = userName2;
        this.userName1 = userName1;
        this.password1 = password1;
        this.password2 = password2;
        this.outLog = outLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("连接按钮开始");
        outLog.append("连接按钮开始\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        try {
            String str1 = url1.getSelectedItem().toString();
            String str2 = url2.getSelectedItem().toString();

            C3p0ConnectionUtil1Impl c3p0ConnectionUtil1 = new C3p0ConnectionUtil1Impl(url1.getSelectedItem().toString(), userName1.getText(), password1.getText(), 5);
            MainForm.connectionPool1 = c3p0ConnectionUtil1.getDataSource();
            C3p0ConnectionUtil2Impl c3p0ConnectionUtil2 = new C3p0ConnectionUtil2Impl(url2.getSelectedItem().toString(), userName2.getText(), password2.getText(), 5);
            MainForm.connectionPool2 = c3p0ConnectionUtil2.getDataSource();
            updateUrlItem(str1, 1);
            if (!str1.equals(str2)) {
                updateUrlItem(str2, 2);
            }
            System.out.println(MainForm.connectionPool1);
            System.out.println(MainForm.connectionPool2);
            // 保存历史记录

        } catch (Exception exception) {
            new MyDialog(exception.getMessage());
            outLog.append(exception.getMessage() + "\n");
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
            }else{
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
