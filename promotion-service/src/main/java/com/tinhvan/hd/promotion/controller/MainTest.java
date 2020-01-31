package com.tinhvan.hd.promotion.controller;

import com.tinhvan.hd.base.HDUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StopWatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class MainTest {
    public static void main(String[] args) throws IOException {
        StopWatch sw = new StopWatch();
        sw.start();
        FileInputStream fileIS = null;
        Workbook workbookIn = null;
        fileIS = new FileInputStream("1M.xlsx");
        workbookIn = new XSSFWorkbook(fileIS);
        Sheet sheetIn = workbookIn.getSheetAt(0);
        Iterator<Row> rowIteratorIn = sheetIn.iterator();
        while (rowIteratorIn.hasNext()) {
            Row rowIn = rowIteratorIn.next();
            //if(rowIn.getRowNum()>=5) {
                Iterator<Cell> cellIteratorIn = rowIn.iterator();
                while (cellIteratorIn.hasNext()) {
                    Cell cellIn = cellIteratorIn.next();
                    if (!HDUtil.isNullOrEmpty(cellIn.getStringCellValue()))
                        System.out.println(rowIn.getRowNum());
                }
            //}
        }
        workbookIn.close();
        fileIS.close();
        sw.stop();
        System.out.println(sw.getTotalTimeMillis());
    }
}
