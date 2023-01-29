package com.cjf.listener;

import com.cjf.MainForm;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DataBaseTypeItemListener implements ItemListener {
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED && "mysql".equals(e.getItem().toString())) {
            // 可编辑
            MainForm.dbType = "mysql";
        }else if (e.getStateChange() == ItemEvent.SELECTED && "oracle".equals(e.getItem().toString())){
            // 不可编辑
            MainForm.dbType = "oracle";
        }
    }
}
