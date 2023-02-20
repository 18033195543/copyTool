package com.cjf.listener.fileImport;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashSet;

public class ChooseFileActionListener implements ActionListener {

    private JTextArea outLog;

    public ChooseFileActionListener(JTextArea outLog) {
        this.outLog = outLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
//        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(FileImportForm.ft.substring(FileImportForm.ft.lastIndexOf(".")+1),"*");
//        jFileChooser.setFileFilter(fileFilter);
        int returnVal = jFileChooser.showOpenDialog(new JButton());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File[] arrfiles = jFileChooser.getSelectedFiles();
            if (arrfiles == null || arrfiles.length == 0) {
                return;
            }
            FileInputStream input = null;
            FileOutputStream out = null;
            String path = "./";
            try {
                for (File f : arrfiles) {
                    File dir = new File(path);
                    /** 目标文件夹 * */
                    File[] fs = dir.listFiles();
                    HashSet<String> set = new HashSet<String>();
                    for (File file : fs) {
                        set.add(file.getName());
                    }
                    /** 判断是否已有该文件* */
                    if (set.contains(f.getName())) {
                        JOptionPane.showMessageDialog(new JDialog(),
                                f.getName() + ":该文件已存在！");
                        return;
                    }
                    input = new FileInputStream(f);
                    byte[] buffer = new byte[1024];
                    File des = new File(path, f.getName());
                    out = new FileOutputStream(des);
                    int len = 0;
                    while (-1 != (len = input.read(buffer))) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    input.close();
                }
                outLog.append("上传成功！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                new MyDialog("上传成功！");
//                JOptionPane.showMessageDialog(null, "上传成功！", "提示",
//                        JOptionPane.INFORMATION_MESSAGE);


            } catch (FileNotFoundException e1) {
                outLog.append("上传失败！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                JOptionPane.showMessageDialog(null, "上传失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (IOException e1) {
                outLog.append("上传失败！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                JOptionPane.showMessageDialog(null, "上传失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        }
    }
}
