package com.cjf;

import com.alee.extended.layout.TableLayout;
import com.cjf.listener.MyWindowListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyApp extends JFrame implements ActionListener {

    private JPanel jp1;
    private JPanel jp2;
    private JPanel card;//能够替换的部分
    private CardLayout cardLayout;//用来执行替换
    private JMenuBar menuBar;
    private JMenu menu, aboutUs;
    private JMenuItem dbCopy, sqlFile, about;
    private JPanel mainView;//主页面，包括菜单部分和替换部分

    public MyApp() {
        //添加菜单栏
        menuBar = new JMenuBar();
        // 添加文件菜单
        menu = new JMenu("文件");
        dbCopy = new JMenuItem("数据库之间导入数据");
        dbCopy.addActionListener(this);
        sqlFile = new JMenuItem("上传文件导入数据");
        sqlFile.addActionListener(this);
        menu.add(dbCopy);
        menu.add(sqlFile);
        // 添加帮助菜单
        aboutUs = new JMenu("Help");
        about = new JMenuItem("About");
        aboutUs.add(about);
        // 将菜单添加进菜单栏
        menuBar.add(menu);
        menuBar.add(aboutUs);
        double border = 1;
        double size[][] =
                {{border, TableLayout.FILL, border},  // Columns
                        {border, 0.05, 1, TableLayout.FILL, border}}; // Rows

        TableLayout gridLayout = new TableLayout(size);

        mainView = new JPanel(gridLayout);
        mainView.add(menuBar, "1, 1, 1, 1");

        jp1 = new JPanel();
        JPanel mainForm = new MainForm().getMainForm();
        mainForm.setPreferredSize(new Dimension(980, 570));
        jp1.add(mainForm);
        jp2 = new JPanel();
        JPanel mainForm1 = new FileImportForm().getMainForm();
        mainForm1.setPreferredSize(new Dimension(980, 570));
        jp2.add(mainForm1);

        card = new JPanel();
        cardLayout = new CardLayout();

        card.setLayout(cardLayout);
        card.add("card1", jp1);//要在能替换界面的直接位置添加名字
        card.add("card2", jp2);

        mainView.add(card, "1, 3, 1, 3");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 添加皮肤
                try {
                    UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                JFrame frame = new JFrame("数据库工具");
                MyApp mainForm = new MyApp();
                frame.setContentPane(mainForm.mainView);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setPreferredSize(new Dimension(1000, 650));
                // 设置坐标
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                int x = (int) (toolkit.getScreenSize().getWidth() - 1000) / 2;
                int y = (int) (toolkit.getScreenSize().getHeight() - 700) / 2;
                frame.setLocation(x, y);

                frame.pack();
                frame.setVisible(true);
                frame.addWindowListener(new MyWindowListener(frame));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dbCopy) {
            cardLayout.show(card, "card1");//根据名字切换界面
        } else if (e.getSource() == sqlFile) {
            cardLayout.show(card, "card2");
        } else {
        }
    }


}
