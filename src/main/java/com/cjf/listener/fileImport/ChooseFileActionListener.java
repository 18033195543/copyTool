package com.cjf.listener.fileImport;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ChooseFileActionListener implements ActionListener {

    private JTextArea outLog;

    public ChooseFileActionListener(JTextArea outLog) {
        this.outLog = outLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileImportForm.fileCacheMap = new ArrayList<>();
        outLog.append("清除缓存文件成功！\n");
        outLog.setCaretPosition(outLog.getDocument().getLength());
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled(false);
        if (FileImportForm.ft == null) {
            FileImportForm.ft = "*.sql";
        }
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(FileImportForm.ft, FileImportForm.ft.substring(FileImportForm.ft.lastIndexOf(".") + 1));
        jFileChooser.setFileFilter(fileFilter);
        int returnVal = jFileChooser.showOpenDialog(new JButton());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File[] arrfiles = jFileChooser.getSelectedFiles();
            if (arrfiles == null || arrfiles.length == 0) {
                return;
            }
            BufferedReader br = null;
            String path = "./";
            try {
                File f = arrfiles[0];
                FileReader fileReader = new FileReader(f);
                br = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String str;
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                br.close();
                str = stringBuffer.toString();
                // 处理SQL语句
                excutSql(str);
                outLog.append("缓存成功！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                new MyDialog("缓存成功！");
//                JOptionPane.showMessageDialog(null, "上传成功！", "提示",
//                        JOptionPane.INFORMATION_MESSAGE);


            } catch (FileNotFoundException e1) {
                outLog.append("缓存失败！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                JOptionPane.showMessageDialog(null, "缓存失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (IOException e1) {
                outLog.append("缓存失败！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                JOptionPane.showMessageDialog(null, "缓存失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (Exception e1) {
                outLog.append("缓存失败！\n"+e1.getMessage()+"\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
            }
        }
    }

    private void excutSql(String str){
        if (null == str || str.length() == 0)
            return;
        int i = str.indexOf(";");
        if (i == -1) {
            FileImportForm.fileCacheMap.add(str);
            return;
        }
        String substring = str.substring(0, i);
        if (null != substring && substring.length() > 0) {
            FileImportForm.fileCacheMap.add(substring);
        }

        String substring1 = str.substring(i+1);
        excutSql(substring1);
    }
}