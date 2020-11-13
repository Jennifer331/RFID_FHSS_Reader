/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.rascal.rfidfhssreader;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Xiaoyue Lei
 */
public class StorageManager {    
    static StorageManager sm = null;
    static List<Record> records;
    public static StorageManager getIntance() {
        if (null == sm) {
            sm = new StorageManager();
            records = new LinkedList<>();
        }
        return sm;
    }
    
    public void save(List<Record> rs) {
        records.addAll(rs);
    }
    
    public void saveToFile(String name) {
        try {
            Writer writer = new FileWriter("d:\\Atom\\Dependency\\" + name + ".csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(records);
            writer.close();
        } catch (IOException|CsvRequiredFieldEmptyException|CsvDataTypeMismatchException ex) {
            ex.printStackTrace();
        }
    }
}
