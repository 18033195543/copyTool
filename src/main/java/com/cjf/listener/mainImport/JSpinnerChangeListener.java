package com.cjf.listener.mainImport;

import com.cjf.dialog.MyDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JSpinnerChangeListener implements ChangeListener {


    private String titail;
    private JTextArea outLog;

    public JSpinnerChangeListener(String titail, JTextArea outLog) {
        this.titail = titail;
        this.outLog = outLog;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner jSpinner = ((JSpinner) e.getSource());
        int num = (int) jSpinner.getValue();
        if (num < 1) {
            jSpinner.setValue(1);
            new MyDialog(this.titail+"不能小于1");
            outLog.append(this.titail+"不能小于1\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        }
    }
}
