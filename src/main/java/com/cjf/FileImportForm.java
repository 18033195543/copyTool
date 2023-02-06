package com.cjf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class FileImportForm {
    private JPanel mainForm;
    private JPanel sqlUploadView;
    private JPanel chooseSqlPanel;
    private JButton startImportButton;
    private JButton chooseSqlFileButton;
    private JPanel logPanel;
    private JTextArea sqlOutLog;
    private JComboBox fileType;
    private JComboBox url;
    private JTextField userName;
    private JPasswordField password;
    private JButton getConnectionButton;
    private JButton closeConnectionButton;
    private JComboBox dbType;


    public FileImportForm() {

    }

    public JPanel getMainForm() {
        return mainForm;
    }
}
