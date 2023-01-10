package com.cjf;

import com.cjf.dialog.MyDialog;
import com.cjf.listener.CloseConnectionActionListener;
import com.cjf.listener.GetConnectionPoolActionListener;
import com.cjf.listener.StartCopyActionListener;
import com.cjf.listener.UrlPopupMenuListener;
import com.cjf.opreationdata.DataOperateService;
import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainForm {

    public static ConnectionPoolImpl1 connectionPool1;
    public static ConnectionPoolImpl2 connectionPool2;
    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 10, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2000000));
    public static List<String> url_item;

    public MainForm() {
        startButton.addActionListener(new StartCopyActionListener(getSql, tableName));
        cancelButton.addActionListener(new CloseConnectionActionListener());
        connectButton.addActionListener(new GetConnectionPoolActionListener(url1, url2,  userName2, userName1, password1, password2));
        // 添加弹出菜单侦听器
        url1.addPopupMenuListener(new UrlPopupMenuListener(url1, url2));
        url2.addPopupMenuListener(new UrlPopupMenuListener(url1, url2));

        //设置下拉框可编辑
        url1.setEditable(true);
        url2.setEditable(true);
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("MainForm");
        MainForm mainForm = new MainForm();
        frame.setContentPane(mainForm.mainForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 500));
        frame.pack();
        frame.setVisible(true);

    }


    private JPanel mainForm;
    private JTextArea textArea1;

    private JButton connectButton;
    private JButton cancelButton;
    private JButton startButton;
    private JPanel connectInfo;
    private JPanel logPanel;
    private JPanel buttonPanel;
    private JTextArea getSql;
    private JLabel inSql;
    private JLabel outSql;
    private JTextField tableName;
    private JComboBox url1;
    private JTextField userName1;
    private JPasswordField password2;
    private JComboBox url2;
    private JTextField userName2;
    private JPasswordField password1;


    private void createUIComponents() {
    }
}
