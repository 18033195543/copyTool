package com.cjf.listener.fileImport;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;
import com.cjf.entity.ColumnEntity;
import com.cjf.opreationdata.FileOperateService;
import com.cjf.opreationdata.IExcutThread;
import com.cjf.opreationdata.SqlFileExcutThread;
import com.cjf.opreationdata.SqlFileOperateService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class StartImportActionListener implements ActionListener {

    private JTextArea outLog;
    private JTextField tableName;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public static CountDownLatch countDownLatch;

    public StartImportActionListener(JTextArea outLog, JTextField tableName) {
        this.outLog = outLog;
        this.tableName = tableName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int i = JOptionPane.showConfirmDialog(null, "是否开始导入SQL?", "导入SQL", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (JOptionPane.YES_NO_OPTION == i) {// 选择导入sql

            if (FileImportForm.fileCacheList == null || FileImportForm.fileCacheList.size() == 0) {
                new MyDialog("请选择导入文件！");
                outLog.append("请选择导入文件！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }
            if (FileImportForm.connectionPool1 == null) {
                new MyDialog("请先获取数据库连接！");
                outLog.append("请先获取数据库连接！\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                return;
            }

            if ("*.csv".equals(FileImportForm.ft)) { // 如果是csv文件需要先对数据进行处理成可执行的sql
                if (tableName == null || tableName.getText().length() == 0) {
                    outLog.append("请输入导入表名！\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                    new MyDialog("请输入导入表名！");
                    return;
                }
                List<ColumnEntity> columuList = new ArrayList<>();
                try {
                    Connection connection = FileImportForm.connectionPool1.getConnection();
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet result = metaData.getColumns(null, "%", tableName.getText().toUpperCase(Locale.ROOT), "%");
                    while (result.next()) {
                        ColumnEntity columnEntity = new ColumnEntity();
                        columnEntity.setColumnName(result.getString("COLUMN_NAME"));
                        columnEntity.setTypeName(result.getString("TYPE_NAME"));
                        columnEntity.setColumnSize(result.getString("COLUMN_SIZE"));
                        columuList.add(columnEntity);
                    }
                } catch (SQLException throwables) {
                    outLog.append("异常！==>" + throwables.getMessage() + "\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                    new MyDialog("异常！==>" + throwables.getMessage());
                    throwables.printStackTrace();
                    return;
                }
                String s = FileImportForm.fileCacheList.get(0);
                List<String> tempList = new ArrayList<>();
                for (int i1 = 1; i1 <= FileImportForm.fileCacheList.size(); i1++) {
                    tempList.add(FileImportForm.fileCacheList.get(i1));
                }

                String[] split = s.split(",");

            } else if ("*.sql".equals(FileImportForm.ft)) {
                FileOperateService sqlFileOperateService = new SqlFileOperateService(atomicInteger, outLog);
                new Thread(() -> {
                    sqlFileOperateService.excute();
                }).start();


            }

        }
    }

}
