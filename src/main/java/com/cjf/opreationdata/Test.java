package com.cjf.opreationdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private static List<String> url_item = new ArrayList<>();
    static{
        url_item.add("111");
        url_item.add("222");
        url_item.add("w4234tge");
        url_item.add("22255");
        url_item.add("wghgzsxfge");
        url_item.add("sdfgsdf");
        url_item.add("awegwg");
    }

    public static void main(String[] args) {
        updateUrlFile();
    }
    private static void updateUrlFile() {
        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\url.txt");
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (String s : url_item) {
                bufferedWriter.write(s+"\n");
            }
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
