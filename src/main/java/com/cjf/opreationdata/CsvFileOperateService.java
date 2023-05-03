package com.cjf.opreationdata;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;
import com.cjf.entity.ColumnEntity;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CsvFileOperateService implements FileOperateService {
    private JTextArea outLog;
    private AtomicInteger atomicInteger;
    private JTextField tableName;

    public CsvFileOperateService(JTextArea outLog, AtomicInteger atomicInteger, JTextField tableName) {
        this.outLog = outLog;
        this.atomicInteger = atomicInteger;
        this.tableName = tableName;
    }

    @Override
    public void excute() throws SQLException {

        List<String> columuNames = null;
        Connection connection = null;
        try {
            connection = FileImportForm.connectionPool1.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet result = metaData.getColumns(null, null, tableName.getText().toUpperCase(Locale.ROOT), null);
            List<ColumnEntity> columuList = new ArrayList<>();
            while (result.next()) {
                ColumnEntity columnEntity = new ColumnEntity();
                columnEntity.setColumnName(result.getString("COLUMN_NAME"));
                columnEntity.setTypeName(result.getString("TYPE_NAME"));
                columnEntity.setColumnSize(result.getString("COLUMN_SIZE"));
                columuList.add(columnEntity);
            }
            result.close();
            columuNames = columuList.stream().map(x -> x.getColumnName()).distinct().collect(Collectors.toList());
        } catch (SQLException throwables) {
            outLog.append("异常！==>" + throwables.getMessage() + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            new MyDialog("异常！==>" + throwables.getMessage());
            throwables.printStackTrace();
            return;
        }
//        String s = FileImportForm.csvRecodeList.get(0);
        List<String> tempList = new ArrayList<>();
        for (int i1 = 0; i1 < FileImportForm.csvRecodeList.size(); i1++) {
            tempList.add(FileImportForm.csvRecodeList.get(i1));
        }
        String remove = tempList.remove(0);

        String[] split = remove.split(",");
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(tableName.getText()).append(" (");
        boolean flag = true;
        for (String s1 : split) {
            sb.append(s1).append(",");
            flag = true;
            for (String s2 : columuNames) {
                if (s2.equalsIgnoreCase(s1)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                outLog.append("字段：" + s1 + "与数据库表无对应字段\n");
                outLog.setCaretPosition(outLog.getDocument().getLength());
                new MyDialog("字段：" + s1 + "与数据库表无对应字段");
                return;
            }
        }
        String s = sb.substring(0, sb.length() - 1).toString();
        sb = new StringBuffer();
        sb.append(s).append(") values ( ");
        for (int i = 0; i < split.length; i++) {
            sb.append("?,");
        }
        s = sb.substring(0, sb.length() - 1).toString() + ")";
        PreparedStatement preparedStatement = connection.prepareStatement(s);

        int count = 0;
        for (String s1 : tempList) {
            String[] split1 = s1.split(",");
            for (int i = 0; i < split1.length; i++) {
                preparedStatement.setObject(i + 1, split1[i]);
            }
            preparedStatement.addBatch();
            count++;
            if (count % 1000 == 0 || count == tempList.size()) {
                try {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                    outLog.append("插入" + count + "条\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                } catch (Exception e) {
                    outLog.append("插入异常条：" + e.getMessage() + "\n");
                    outLog.setCaretPosition(outLog.getDocument().getLength());
                    new MyDialog("插入异常条：" + e.getMessage());
                }

            }
        }
        preparedStatement.close();
        connection.close();
    }
}
