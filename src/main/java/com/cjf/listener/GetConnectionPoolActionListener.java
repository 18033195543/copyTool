package com.cjf.listener;

import com.cjf.MainForm;
import com.cjf.dialog.MyDialog;
import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

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

    public GetConnectionPoolActionListener(JComboBox url1, JComboBox url2, JTextField userName2, JTextField userName1, JPasswordField password1, JPasswordField password2) {
        this.url1 = url1;
        this.url2 = url2;
        this.userName2 = userName2;
        this.userName1 = userName1;
        this.password1 = password1;
        this.password2 = password2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("连接按钮开始");
        try {
            String str1 = url1.getSelectedItem().toString();
            String str2 = url2.getSelectedItem().toString();

            MainForm.connectionPool1 = new ConnectionPoolImpl1(url1.getSelectedItem().toString(), userName1.getText(), password1.getText());
            MainForm.connectionPool2 = new ConnectionPoolImpl2(url2.getSelectedItem().toString(), userName2.getText(), password2.getText());
            MainForm.connectionPool1.init(1);

            updateUrlItem(str1);

            MainForm.connectionPool2.init(1);
            if (!str1.equals(str2)) {
                updateUrlItem(str2);
            }
            System.out.println(MainForm.connectionPool1);
            System.out.println(MainForm.connectionPool2);
            // 保存历史记录

        } catch (Exception exception) {
            new MyDialog(exception.getMessage());
            return;
        }
        if (MainForm.connectionPool1 == null) {
            new MyDialog("数据库1获取连接池失败！");
            return;
        }
        if (MainForm.connectionPool2 == null) {
            new MyDialog("数据库2获取连接池失败！");
            return;
        }

        new MyDialog("数据库获取连接池成功！");
    }

    private void updateUrlItem(String str1) {
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
            url1.removeAllItems();
            url2.removeAllItems();
            MainForm.url_item.forEach(y -> {
                url1.addItem(y);
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
