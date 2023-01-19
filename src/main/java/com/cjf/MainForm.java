package com.cjf;

import com.cjf.dialog.MyDialog;
import com.cjf.listener.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainForm {

    public static ComboPooledDataSource connectionPool1;
    public static ComboPooledDataSource connectionPool2;
    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 10, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2000000));
    public static List<String> url_item;

    public MainForm() {
        JMenu jMenu = new JMenu("文件");
        JMenuItem jMenuItem = new JMenuItem("上传");
        JMenuItem jMenuItem1 = new JMenuItem("下载");
        jMenu.add(jMenuItem);
        jMenu.add(jMenuItem1);
        menuBar.add(jMenu);
        startButton.addActionListener(new StartCopyActionListener(getSql, tableName, outLog));
        cancelButton.addActionListener(new CloseConnectionActionListener(outLog));
        connectButton.addActionListener(new GetConnectionPoolActionListener(url1, url2, userName2, userName1, password1, password2, outLog));
        // 添加弹出菜单侦听器
        url1.addPopupMenuListener(new UrlPopupMenuListener(url1, url2));
        url2.addPopupMenuListener(new UrlPopupMenuListener(url1, url2));

        advancedMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && "是".equals(e.getItem().toString())) {
                    // 可编辑
                    gjLabelPanel.setVisible(true);
                    gjValuePanel.setVisible(true);
                }else{
                    // 不可编辑
                    gjLabelPanel.setVisible(false);
                    gjValuePanel.setVisible(false);
                }

            }
        });

        // 设置默认值
        threadPoolSize.setValue(1);
        threadPoolSize.addChangeListener(new JSpinnerChangeListener("线程数",outLog));
        // 设置默认值
        connectionPoolSize.setValue(1);
        connectionPoolSize.addChangeListener(new JSpinnerChangeListener("连接数",outLog));

        //设置下拉框可编辑
        url1.setEditable(true);
        url2.setEditable(true);
        ((JSpinner.DefaultEditor)threadPoolSize.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor)connectionPoolSize.getEditor()).getTextField().setEditable(false);
        gjLabelPanel.setVisible(false);
        gjValuePanel.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
               // 添加皮肤
                try {
                    UIManager.setLookAndFeel ( "com.alee.laf.WebLookAndFeel" );
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                JFrame frame = new JFrame("MainForm");
                MainForm mainForm = new MainForm();
                frame.setContentPane(mainForm.mainForm);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setPreferredSize(new Dimension(1000, 700));
                // 设置坐标
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                int x = (int) (toolkit.getScreenSize().getWidth() - 1000) / 2;
                int y = (int) (toolkit.getScreenSize().getHeight() - 700) / 2;
                frame.setLocation(x, y);

                frame.pack();
                frame.setVisible(true);
                frame.addWindowListener(new MyWindowListener(frame));
            }
        } );


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
    private JSpinner threadPoolSize;
    private JSpinner connectionPoolSize;
    private JComboBox databaseType;
    private JTextArea outLog;
    private JComboBox advancedMode;
    private JPanel gjLabelPanel;
    private JPanel gjValuePanel;
    private JMenuBar menuBar;


    private void createUIComponents() {
    }
}
