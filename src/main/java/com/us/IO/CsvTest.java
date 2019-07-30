package com.us.IO;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.*;
import java.util.List;

/**
 * 读写csv
 *
 * @author yyb
 * @time 2019/7/19
 */
public class CsvTest {
    private static Long pos = 0L;

    private static void read() throws IOException {
        RandomAccessFile rf = new RandomAccessFile("xxx.csv", "r");

        String line=null;
        while((line=rf.readLine())!=null)
        {
            System.out.println(new String(line.getBytes("ISO-8859-1"), "gbk"));

        }
        rf.close();
        System.out.println();

    }

    public  static <T> List<T> getCsvData(Class<T> clazz) {
        InputStreamReader in=null;
        try {
            in = new InputStreamReader(new FileInputStream("xxxxx.csv"), "gbk");
        } catch (Exception e) {

        }

        System.out.println(System.currentTimeMillis());
        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(clazz);

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(in)
                .withSeparator(',')
                .withQuoteChar('\'')
                .withMappingStrategy(strategy).build();
        List<T> list= csvToBean.parse();
        System.out.println(System.currentTimeMillis());
        System.out.println(list.size());
        return list;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("xxx/1.csv");
        Writer writer = new FileWriter(file);
        //分隔符为逗号
        CSVWriter csvWriter = new CSVWriter(writer);
        String[] strs = {"abc" , "abc" , "abc"};
        csvWriter.writeNext(strs);
        csvWriter.close();
    }




}
