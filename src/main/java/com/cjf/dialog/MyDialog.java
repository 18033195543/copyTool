package com.cjf.dialog;

import javax.swing.*;
import java.awt.*;

public class MyDialog extends JDialog {

    private String msg;

    public MyDialog(String msg) {
        this.msg = msg;
        this.setBounds(400, 200, 200, 100);
        this.setVisible(true);
        this.setTitle("确认连接");
        Container contentPane = this.getContentPane();
        contentPane.setLayout(null);

        JLabel la = new JLabel();
        la.setVisible(true);
        la.setText(msg);
        la.setBounds(10, -30, 200, 100);
        contentPane.add(la);
    }
}
