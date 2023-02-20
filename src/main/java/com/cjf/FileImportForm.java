package com.cjf;

import com.cjf.listener.fileImport.ChooseFileActionListener;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

    public static String ft;


    public FileImportForm() {
        // 添加选择文件监听
        chooseSqlFileButton.addActionListener(new ChooseFileActionListener(outLog));
// 监听是否开启高级模式
        fileType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED ) {
                    // 可编辑
                    ft = e.getItem().toString();
                }

            }
        });
    }

    public JPanel getMainForm() {
        return mainForm;
    }
}
