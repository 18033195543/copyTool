package com.cjf.listener.mainImport;

import com.cjf.MainForm;
import com.cjf.dialog.MyDialog;
import com.cjf.opreationdata.ExcutThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.TimeUnit;

public class MyWindowListener implements WindowListener {

    private JFrame jFrame;

    public MyWindowListener(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

        int i = JOptionPane.showConfirmDialog(null, "你确定要退出系统吗？", "退出系统！", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (JOptionPane.YES_OPTION == i) {
            JDialog jDialog = new JDialog(jFrame);

            jDialog.setVisible(true);
            jDialog.setBounds(400, 200, 250, 100);
            jDialog.setTitle("提示消息");
            jDialog.setLocationRelativeTo(null);
            Container contentPane = jDialog.getContentPane();
            contentPane.setLayout(null);

            Label jLabel = new Label("Save data and release resources..");
            jLabel.setVisible(true);
            jLabel.setBounds(10, -30, 200, 100);
            jDialog.add(jLabel);

            boolean flag = true;
            while (flag) {
                // 确保程序没有在读写数据
                if (null == ExcutThread.countDownLatch) {
                    flag = false;
                } else if (ExcutThread.countDownLatch.getCount() != 0) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } else {
                    flag = false;
                }
            }
            // 关闭线程池
            if (MainForm.executor != null) {
                MainForm.executor.shutdown();
            }

            /**      释放资源     **/
            // 关闭连接池1
            if (MainForm.connectionPool1 != null) {
                try {
                    MainForm.connectionPool1.close();
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }
            }
            // 关闭连接池2
            if (MainForm.connectionPool2 != null) {
                try {
                    MainForm.connectionPool2.close();
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }
            }

            /**      释放资源     **/
            System.exit(0);
        } else {
            return;
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
