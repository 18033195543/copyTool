package com.cjf.listener;

import com.cjf.MainForm;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class UrlPopupMenuListener implements PopupMenuListener {

    public UrlPopupMenuListener(JComboBox url1, JComboBox url2) {
        this.url1 = url1;
        this.url2 = url2;
    }

    private JComboBox url1;
    private JComboBox url2;

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        // 下拉时监听
        if (MainForm.url_item == null) {
            MainForm.url_item = init();
            MainForm.url_item.forEach(x -> {
                url1.addItem(x);
                url2.addItem(x);
            });
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {

    }

    private List<String> init() {
        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\url.txt");
        FileReader fileReader = null;
        List<String> list = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            list = new ArrayList<>();
            String b;
            while ((b = bufferedReader.readLine()) != null) {
                list.add(b);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }
}
