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
    private JLabel fileName;

    public ChooseFileActionListener(JTextArea outLog, JLabel fileName) {
        this.outLog = outLog;
        this.fileName = fileName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileImportForm.fileCacheList = new ArrayList<>();
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
            try {
                File f = arrfiles[0];
                String fName = f.getName();
                String sufix = fName.substring(fName.lastIndexOf(".") + 1);
                String sufix1 = FileImportForm.ft.substring(FileImportForm.ft.lastIndexOf(".") + 1);
                if (!sufix1.equals(sufix)) {
                    outLog.append("文件类型错误！\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                    JOptionPane.showMessageDialog(null, "文件类型错误！", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                fileName.setToolTipText(fName);
                if (fName.length() > 20) {
                    String substring = fName.substring(fName.length() - 20);
                    fName = "..." + substring;
                }
                fileName.setText(fName);
                FileReader fileReader = new FileReader(f);
                br = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                if ("*.sql".equals(FileImportForm.ft)) {
                    String str;
                    while ((str = br.readLine()) != null) {
                        stringBuffer.append(str);
                    }
                    br.close();
                    str = stringBuffer.toString();
                    // 处理SQL语句
                    excutSql(str);
                } else if ("*.csv".equals(FileImportForm.ft)) {
                    FileImportForm.csvRecodeList = new ArrayList<>();
                    String str;
                    while ((str = br.readLine()) != null) {
                        FileImportForm.csvRecodeList.add(str);
                    }
                    br.close();
                }
                outLog.append("缓存成功！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                new MyDialog("缓存成功！");


            } catch (FileNotFoundException e1) {
                outLog.append("缓存失败！" + e1.getMessage() + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                JOptionPane.showMessageDialog(null, "缓存失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (IOException e1) {
                outLog.append("缓存失败！" + e1.getMessage() + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                JOptionPane.showMessageDialog(null, "缓存失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (Exception e1) {
                outLog.append("缓存失败！\n" + e1.getMessage() + "\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                JOptionPane.showMessageDialog(null, "缓存失败！", "提示",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excutSql(String str) {
        if (null == str || str.length() == 0)
            return;
        String tempStr = str;
        boolean b = true;
        int index = 0;
        while (b) {
            int i = tempStr.indexOf(";", index);
            if (i == -1) {
                String substring = tempStr.substring(index);
                FileImportForm.fileCacheList.add(substring);
                break;
            } else {
                String substring = tempStr.substring(index, i);
                if (null != substring && substring.length() > 0) {
                    FileImportForm.fileCacheList.add(substring);
                    index = i + 1;
                } else {
                    b = false;
                }
            }
        }
    }
}
