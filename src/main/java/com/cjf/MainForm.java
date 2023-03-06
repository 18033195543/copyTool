package com.cjf;

import com.cjf.listener.mainImport.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.ThreadPoolExecutor;

public class MainForm {

    public static ComboPooledDataSource connectionPool1;
    public static ComboPooledDataSource connectionPool2;
    public static ThreadPoolExecutor executor;
    public static String dbType = "oracle";

    // 判断是否打开高级模式
    public static boolean isGj;

    public MainForm() {
        startButton.addActionListener(new StartCopyActionListener(getSql, tableName, outLog));
        cancelButton.addActionListener(new CloseConnectionActionListener(outLog));
        connectButton.addActionListener(new GetConnectionPoolActionListener(url1, url2, userName2, userName1, password1, password2, outLog, threadPoolCoreSize, threadPoolMaxSize, connectionPoolSize, databaseType));
        MyApp.url_item.forEach(x -> {
            url1.addItem(x);
            url2.addItem(x);
        });
        // 监听是否开启高级模式
        advancedMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && "是".equals(e.getItem().toString())) {
                    // 可编辑
                    gjLabelPanel.setVisible(true);
                    gjValuePanel.setVisible(true);
                    isGj = true;
                } else {
                    // 不可编辑
                    gjLabelPanel.setVisible(false);
                    gjValuePanel.setVisible(false);
                    isGj = false;
                }

            }
        });

        // 设置默认值
        threadPoolCoreSize.setValue(1);
        threadPoolCoreSize.addChangeListener(new JSpinnerChangeListener("核心线程数", outLog));
        threadPoolMaxSize.setValue(1);
        threadPoolMaxSize.addChangeListener(new JSpinnerChangeListener("最大线程数", outLog));
        // 设置默认值
        connectionPoolSize.setValue(1);
        connectionPoolSize.addChangeListener(new JSpinnerChangeListener("连接数", outLog));
        databaseType.addItemListener(new DataBaseTypeItemListener());

        //设置下拉框可编辑
        url1.setEditable(true);
        url2.setEditable(true);
        ((JSpinner.DefaultEditor) threadPoolCoreSize.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) threadPoolMaxSize.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) connectionPoolSize.getEditor()).getTextField().setEditable(false);
        gjLabelPanel.setVisible(false);
        gjValuePanel.setVisible(false);
    }


    private JPanel mainForm;
    private JButton connectButton;
    private JButton cancelButton;
    private JButton startButton;
    private JPanel connectInfo;
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
    private JSpinner threadPoolCoreSize;
    private JSpinner connectionPoolSize;
    private JComboBox databaseType;
    private JTextArea outLog;
    private JComboBox advancedMode;
    private JPanel gjLabelPanel;
    private JPanel gjValuePanel;
    private JSpinner threadPoolMaxSize;
    private JPanel copyPanel;

    public JPanel getMainForm() {
        return mainForm;
    }

    private void createUIComponents() {
    }
}
