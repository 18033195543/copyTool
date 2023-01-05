package com.cjf;

import javax.swing.*;
import java.awt.*;

public class TestForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("TestForm");
        frame.setContentPane(new TestForm().testForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,500));
        frame.pack();

        frame.setVisible(true);
    }

    private JPanel testForm;
    private JLabel url;
    private JLabel userName1;
    private JLabel password;
    private JTextField urlField;
    private JTextField textField2;
    private JTextField textField3;
    private JTextArea sssTextArea;

    public void setTestForm(JPanel testForm) {
        this.testForm = testForm;
    }

    public void setUrl(JLabel url) {
        this.url = url;
    }

    public void setUserName1(JLabel userName1) {
        this.userName1 = userName1;
    }

    public void setPassword(JLabel password) {
        this.password = password;
    }

    public void setUrlField(JTextField urlField) {
        this.urlField = urlField;
    }

    public void setTextField2(JTextField textField2) {
        this.textField2 = textField2;
    }

    public void setTextField3(JTextField textField3) {
        this.textField3 = textField3;
    }

}
