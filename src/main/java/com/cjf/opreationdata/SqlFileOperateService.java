package com.cjf.opreationdata;

import com.cjf.FileImportForm;
import com.cjf.dialog.MyDialog;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SqlFileOperateService implements FileOperateService {

    private CountDownLatch countDownLatch;
    private AtomicInteger atomicInteger;
    public static AtomicInteger insertTotal;
    private JTextArea outLog;

    public SqlFileOperateService(AtomicInteger atomicInteger, JTextArea outLog) {
        this.atomicInteger = atomicInteger;
        this.outLog = outLog;
    }

    @Override
    public void excute() {
        long l = System.currentTimeMillis();
        int i = 0;
        Map<String, List<String>> splitMap = new HashMap<>();
        List<String> paraList = new ArrayList<>();
        for (String s : FileImportForm.fileCacheList) {
            paraList.add(s);
            i++;
            if (i % 2000 == 0 || i == FileImportForm.fileCacheList.size()) {
                splitMap.put(String.valueOf(i), paraList);
                paraList = new ArrayList<>();
            }

        }
        if (insertTotal == null) {
            insertTotal = new AtomicInteger();
        }
        Set<Map.Entry<String, List<String>>> entries = splitMap.entrySet();
        countDownLatch = new CountDownLatch(entries.size());
        for (Map.Entry<String, List<String>> entry : entries) {
            IExcutThread excutThread = new SqlFileExcutThread(outLog, atomicInteger, entry.getValue(), countDownLatch);
            FileImportForm.executor.execute(() -> {
                excutThread.excut();
            });
        }


        try {
            countDownLatch.await();
            outLog.append("SQL导入完毕!一共插入："+insertTotal.get()+"条数据\n");
            insertTotal.set(0);
            outLog.setCaretPosition(outLog.getDocument().getLength());
            long l1 = System.currentTimeMillis();
            long l2 = l1 - l;
            outLog.append("总耗时==>" + (l2 / 1000) + "秒！\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            FileImportForm.fileCacheList = null;
            // 清0计数器
            atomicInteger.set(0);
            outLog.append("清除缓存文件成功！\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
        } catch (InterruptedException ex) {
            outLog.append("出现异常！" + ex.getMessage() + "\n");
            outLog.setCaretPosition(outLog.getDocument().getLength());
            throw new RuntimeException(ex);
        }
    }
}
