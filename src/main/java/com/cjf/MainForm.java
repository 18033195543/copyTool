package com.cjf;

import com.cjf.listener.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class MainForm {

    public static ComboPooledDataSource connectionPool1;
    public static ComboPooledDataSource connectionPool2;
    public static ThreadPoolExecutor executor;
    public static String dbType = "oracle";
    public static List<String> url_item;
    // 判断是否打开高级模式
    public static boolean isGj;

    public MainForm() {
        JMenu file = new JMenu("文件");
        JMenuItem sqlFile = new JMenuItem("上传sql文件导入数据");
        JMenuItem csvFile = new JMenuItem("上传csv文件导入数据");
        JMenuItem exclFile = new JMenuItem("上传excl文件导入数据");
        file.add(sqlFile);
        file.add(csvFile);
        file.add(exclFile);
        JMenu aboutUs = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        aboutUs.add(about);
        menuBar.add(file);
        menuBar.add(aboutUs);
        startButton.addActionListener(new StartCopyActionListener(getSql, tableName, outLog));
        cancelButton.addActionListener(new CloseConnectionActionListener(outLog));
        connectButton.addActionListener(new GetConnectionPoolActionListener(url1, url2, userName2, userName1, password1, password2, outLog,threadPoolCoreSize,threadPoolMaxSize,connectionPoolSize,databaseType));
        // 添加弹出菜单侦听器
        url1.addPopupMenuListener(new UrlPopupMenuListener(url1, url2));
        url2.addPopupMenuListener(new UrlPopupMenuListener(url1, url2));
//        url1.setPreferredSize(new Dimension(10,20));
        connectInfo.setSize(100,200);
        // 监听是否开启高级模式
        advancedMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && "是".equals(e.getItem().toString())) {
                    // 可编辑
                    gjLabelPanel.setVisible(true);
                    gjValuePanel.setVisible(true);
                    isGj = true;
                }else{
                    // 不可编辑
                    gjLabelPanel.setVisible(false);
                    gjValuePanel.setVisible(false);
                    isGj = false;
                }

            }
        });

        // 设置默认值
        threadPoolCoreSize.setValue(1);
        threadPoolCoreSize.addChangeListener(new JSpinnerChangeListener("核心线程数",outLog));
        threadPoolMaxSize.setValue(1);
        threadPoolMaxSize.addChangeListener(new JSpinnerChangeListener("最大线程数",outLog));
        // 设置默认值
        connectionPoolSize.setValue(1);
        connectionPoolSize.addChangeListener(new JSpinnerChangeListener("连接数",outLog));
        databaseType.addItemListener(new DataBaseTypeItemListener());

        //设置下拉框可编辑
        url1.setEditable(true);
        url2.setEditable(true);
        ((JSpinner.DefaultEditor) threadPoolCoreSize.getEditor()).getTextField().setEditable(false);
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
    private JSpinner threadPoolCoreSize;
    private JSpinner connectionPoolSize;
    private JComboBox databaseType;
    private JTextArea outLog;
    private JComboBox advancedMode;
    private JPanel gjLabelPanel;
    private JPanel gjValuePanel;
    private JMenuBar menuBar;
    private JSpinner threadPoolMaxSize;


    private void createUIComponents() {
    }
}
