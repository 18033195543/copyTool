package com.cjf;

import com.cjf.listener.fileImport.ChooseFileActionListener;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.cjf.listener.fileImport.FileImportCloseConnectionActionListener;
import com.cjf.listener.fileImport.FileImportGetConnectionActionListener;
import com.cjf.listener.fileImport.StartImportActionListener;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


public class FileImportForm {
    private JPanel mainForm;
    private JPanel sqlUploadView;
    private JPanel chooseSqlPanel;
    private JButton startImportButton;
    private JButton chooseSqlFileButton;
    private JPanel logPanel;
    private JTextArea outLog;
    private JComboBox fileType;
    private JComboBox url;
    private JTextField userName;
    private JPasswordField password;
    private JButton getConnectionButton;
    private JButton closeConnectionButton;
    private JComboBox dbType;
    private JTextField tableName;
    private JLabel fileName;
    private JLabel tableNameField;

    public static String ft;
    public static ThreadPoolExecutor executor;
    public static ComboPooledDataSource connectionPool1;
    public static List<String> csvRecodeList;

    public static List<String> fileCacheList;

    public FileImportForm() {
        init();
        ft = fileType.getToolTipText();
        // 添加文件类型监听
        fileType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 可编辑
                    ft = e.getItem().toString();
                    if ("*.csv".equals(ft)) {
                        tableNameField.setVisible(true);
                        tableName.setVisible(true);
                    }else {
                        tableNameField.setVisible(false);
                        tableName.setVisible(false);
                    }
                }
            }
        });
        // 添加选择文件监听
        chooseSqlFileButton.addActionListener(new ChooseFileActionListener(outLog,fileName));

        closeConnectionButton.addActionListener(new FileImportCloseConnectionActionListener(outLog));
        // 执行sql监听
        startImportButton.addActionListener(new StartImportActionListener(outLog,tableName));
    }
    private void init () {
        MyApp.url_item.forEach(x -> {
            url.addItem(x);
        });
        getConnectionButton.addActionListener(new FileImportGetConnectionActionListener(url, userName, password, outLog, dbType));
        tableNameField.setVisible(false);
        tableName.setVisible(false);
    }

    public JPanel getMainForm () {
        return mainForm;
    }
}
