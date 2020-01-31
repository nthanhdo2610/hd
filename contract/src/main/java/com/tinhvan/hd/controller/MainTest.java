package com.tinhvan.hd.controller;

import com.google.common.collect.Iterators;
import com.tinhvan.hd.dto.ObjectSendMailIT;
import nl.flotsam.xeger.Xeger;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainTest {
    public static void main(String[] args) {
        try {
            String regex = "[0-9]{9}";
            Xeger generator = new Xeger(regex);
            String[] arr = {"ED", "MC", "CL"};
            Random r = new Random();

            //create file template on local
            File file = new File("400k.xlsx");
            FileInputStream fileIS = new FileInputStream(file);
            Workbook workbookIn = new XSSFWorkbook(fileIS);
            Sheet sheetIn = workbookIn.getSheetAt(0);
            Iterator<Row> rowIteratorIn = sheetIn.iterator();
            for (int i = 0; i <= 400000; i++) {
                int randomNumber = r.nextInt(arr.length);
                Row row = sheetIn.createRow(i + 5);
                row.createCell(0).setCellValue(arr[randomNumber] + generator.generate());
            }

            FileOutputStream fileOS = new FileOutputStream(file.getName());
            workbookIn.write(fileOS);
            workbookIn.close();
            fileIS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
