package com.tinhvan.hd.controller;

import com.tinhvan.hd.dto.ObjectSendMailIT;
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
        SimpleDateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        /*for (int i = 0; i < adjConfigs.size(); i++) {
                    if (i < 9) {
                        String name = "name" + i;
                        String oldValue = "oldValue" + i;
                        String newValue = "newValue" + i;
                        Field f = adjustmentFrom.getClass().getField(name);
                        f.set(adjustmentFrom, adjConfigs.get(i).getName());

                        f = adjustmentFrom.getClass().getField(oldValue);
                        f.set(adjustmentFrom, adjConfigs.get(i).getOldValue());

                        f = adjustmentFrom.getClass().getField(newValue);
                        f.set(adjustmentFrom, adjConfigs.get(i).getNewValue());
                    }
                }*/
        try {
//            File file = new File("HDSaison_template_Adjustment_for_IT.xlsx");
//            List<ObjectSendMailIT> resultSearchs = new ArrayList<>();
//            resultSearchs.add(new ObjectSendMailIT("aaa", "key", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
//            resultSearchs.add(new ObjectSendMailIT("aaa", "key1", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
//            resultSearchs.add(new ObjectSendMailIT("aaa", "key2", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
//            resultSearchs.add(new ObjectSendMailIT("bbb", "key3", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
//            resultSearchs.add(new ObjectSendMailIT("bbb", "key4", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
//            resultSearchs.add(new ObjectSendMailIT("ccc", "key3", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
//            resultSearchs.add(new ObjectSendMailIT("ccc", "key4", "name", "valueOld", "valueAdjustmentInfo", "staffName", new Date()));
//            //create file template on local
//            FileInputStream fileIS = new FileInputStream(file);
//            Workbook workbookIn = new XSSFWorkbook(fileIS);
//            Sheet sheetIn = workbookIn.getSheetAt(0);
//            Cell c0 = sheetIn.getRow(3).getCell(0);
//            c0.setCellValue("");
//            Cell c1 = sheetIn.getRow(3).getCell(1);
//            c1.setCellValue("");
//            int rowIn = sheetIn.getLastRowNum() + 1; //=6
//            int stt = 1;
//            String contractCode = "";
//            boolean isMerge;
//            int rowFrom = rowIn;
//            int rowTo = rowIn;
//            for (int i = 0; i < resultSearchs.size(); i++) {
//                ObjectSendMailIT result = resultSearchs.get(i);
//                isMerge = false;
//                if (result.getContractCode().equals(contractCode))
//                    isMerge = true;
//                Cell c;
//                if (i % 2 == 0)
//                    c = c0;
//                else
//                    c = c1;
//                int currentRow = rowIn + i;
//                Row row = sheetIn.createRow(currentRow);
//                row.createCell(0).setCellStyle(c0.getCellStyle());
//                row.createCell(1).setCellStyle(c0.getCellStyle());
//                if (!isMerge) {
//                    row.getCell(0).setCellValue(stt);
//                    stt++;
//                    if (result.getContractCode() != null) {
//                        row.getCell(1).setCellValue(result.getContractCode());
//                        contractCode = result.getContractCode();
//                    }
//                    if (rowFrom != rowTo) {
//                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 0, 0));
//                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 1, 1));
//                    }
//                    rowFrom = rowTo = currentRow;
//                } else {
//                    rowTo++;
//                    if (i == resultSearchs.size() - 1 && rowFrom != rowTo) {
//                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 0, 0));
//                        sheetIn.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, 1, 1));
//                    }
//                }
//
//                row.createCell(2).setCellStyle(c.getCellStyle());
//                if (result.getKey() != null)
//                    row.getCell(2).setCellValue(result.getKey());
//
//                row.createCell(3).setCellStyle(c.getCellStyle());
//                if (result.getName() != null)
//                    row.getCell(3).setCellValue(result.getName());
//
//                row.createCell(4).setCellStyle(c.getCellStyle());
//                if (result.getValueOld() != null)
//                    row.getCell(4).setCellValue(result.getValueOld());
//
//                row.createCell(5).setCellStyle(c.getCellStyle());
//                if (result.getValueAdjustmentInfo() != null)
//                    row.getCell(5).setCellValue(result.getValueAdjustmentInfo());
//
//                row.createCell(6).setCellStyle(c.getCellStyle());
//                if (result.getStaffName() != null)
//                    row.getCell(6).setCellValue(result.getStaffName());
//
//                row.createCell(7).setCellStyle(c.getCellStyle());
//                if (result.getConfirmDate() != null)
//                    row.getCell(7).setCellValue(df.format(result.getConfirmDate()));
//
//            }
//            c0.setCellStyle(null);
//            c1.setCellStyle(null);
//            String result = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getName());
//            FileOutputStream fileOS = new FileOutputStream(result);
//            workbookIn.write(fileOS);
//            workbookIn.close();
//            fileIS.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
