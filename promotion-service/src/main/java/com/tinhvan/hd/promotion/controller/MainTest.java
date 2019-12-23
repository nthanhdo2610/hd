//package com.tinhvan.hd.promotion.controller;
//
//import com.tinhvan.hd.base.HDUtil;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Iterator;
//
//public class MainTest {
//    public static void main(String[] args) throws IOException {
//        FileInputStream fileIS = null;
//        Workbook workbookIn = null;
//        fileIS = new FileInputStream("hdsaison_template_news.xlsx");
//        workbookIn = new XSSFWorkbook(fileIS);
//        Sheet sheetIn = workbookIn.getSheetAt(0);
//        Iterator<Row> rowIteratorIn = sheetIn.iterator();
//        while (rowIteratorIn.hasNext()) {
//            Row rowIn = rowIteratorIn.next();
//            if(rowIn.getRowNum()>=5) {
//                Iterator<Cell> cellIteratorIn = rowIn.iterator();
//                while (cellIteratorIn.hasNext()) {
//                    Cell cellIn = cellIteratorIn.next();
//                    if (!HDUtil.isNullOrEmpty(cellIn.getStringCellValue()))
//                        System.out.println(cellIn.getStringCellValue());
//                }
//                System.out.println("-----");
//            }
//        }
//        workbookIn.close();
//        fileIS.close();
//    }
//}
