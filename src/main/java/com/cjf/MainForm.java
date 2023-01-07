package com.cjf;

import com.cjf.opreationdata.DataOperateService;
import com.cjf.util.ConnectionPoolImpl1;
import com.cjf.util.ConnectionPoolImpl2;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainForm {

    private static ConnectionPoolImpl1 connectionPool1;
    private static ConnectionPoolImpl2 connectionPool2;
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 10, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(2000000));
    private static List<String> url_item;
    private static Deque URL_QUEUE;

    public MainForm() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("开始按钮");
                if (connectionPool1 == null) {
                    new MyDialog("请先获取数据库1的连接池 !");
                    return;
                }
                if (connectionPool2 == null) {
                    new MyDialog("请先获取数据库2的连接池 !");
                    return;
                }
                if (getSql == null || getSql.getText() == null || "".equals(getSql.getText())) {
                    new MyDialog("请输入输出sql !");
                    return;
                }
                if (tableName == null || tableName.getText() == null || "".equals(tableName.getText())) {
                    new MyDialog("请输入表名 !");
                    return;
                }

                DataOperateService dataOperateService = new DataOperateService(connectionPool1, connectionPool2, getSql.getText(), tableName.getText(), executor);
                try {
                    dataOperateService.reportLeaning();
                    new MyDialog("成功 !");
                } catch (Exception exception) {
                    new MyDialog(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("关闭连接");
                if (connectionPool1 != null) {
                    try {
                        connectionPool1.destory();
                    } catch (Exception throwables) {
                        throwables.printStackTrace();
                    }
                }
                if (connectionPool2 != null) {
                    try {
                        connectionPool2.destory();
                    } catch (Exception throwables) {
                        throwables.printStackTrace();
                    }
                }

                new MyDialog("连接池关闭成功！");
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("连接按钮开始");
                try {
                    connectionPool1 = new ConnectionPoolImpl1(url1.getToolTipText(), userName1.getText(), password1.getText());
                    connectionPool2 = new ConnectionPoolImpl2(url2.getToolTipText(), userName2.getText(), password2.getText());
                    connectionPool1.init(5);
                    connectionPool2.init(5);
                    System.out.println(connectionPool1);
                    System.out.println(connectionPool2);
                    // 保存历史记录

                } catch (Exception exception) {
                    new MyDialog(exception.getMessage());
                    return;
                }
                if (connectionPool1 == null) {
                    new MyDialog("数据库1获取连接池失败！");
                    return;
                }
                if (connectionPool2 == null) {
                    new MyDialog("数据库2获取连接池失败！");
                    return;
                }

                new MyDialog("数据库获取连接池成功！");
            }
        });
        // 添加弹出菜单侦听器
        url1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {// 下拉时监听
                if (URL_QUEUE == null) {
                    URL_QUEUE = new ArrayDeque();
                    try {
                        List<String> init = init();
                        for (String s : init) {
                            URL_QUEUE.add(s);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (url_item == null) {
                    try {
                        url_item = init();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    url_item.forEach(x -> {
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
        });
        url2.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (url_item == null) {
                    try {
                        url_item = init();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    url_item.forEach(x -> {
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
        });


        //设置下拉框可编辑
        url1.setEditable(true);
        url2.setEditable(true);
    }

    class MyDialog extends JDialog {
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

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("MainForm");
        MainForm mainForm = new MainForm();
        frame.setContentPane(mainForm.mainForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 500));
        frame.pack();
        frame.setVisible(true);

    }

    private List<String> init() throws IOException {
        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\url.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> list = new ArrayList<>();
        String b;
        while ((b = bufferedReader.readLine()) != null) {
            list.add(b);
        }
        return list;
    }


    private JPanel mainForm;
    private JTextArea textArea1;

    private JButton connectButton;
    private JButton cancelButton;
    private JButton startButton;
    private JPanel connectInfo;
    private JPanel logPanel;
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


    private void createUIComponents() {
    }
}
