package com.company.apache;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;

public class Walter {

    String SHEET;
    //    String INVOICE = "C:\\Users\\Professional\\Desktop\\Git\\Інвойси 2021 LKW.xlsx";
    String INVOICE = "/Users/mihajlomelnik/Documents/VAV TRANS/Інвойси 2021 LKW !.xlsx";

    File miFile = new File(INVOICE);
    FileInputStream fileInputStream = new FileInputStream(miFile);
    Workbook workbook = new XSSFWorkbook(fileInputStream);
    XSSFSheet sheet;
    TollCollect tollCollect = new TollCollect();

    public Walter() throws IOException {
    }

    public void start(String sheetStart) throws IOException {
        SHEET = sheetStart;
        startFound();
        tollCollect.summationTollCollectByDates();
        SHEET = sheetStart;
        startFound();
    }

    public void startFound() throws IOException {
        sheet = (XSSFSheet) workbook.getSheet(SHEET);
        System.out.println("Страница " + SHEET);
        for (int i = 4; String.valueOf(sheet.getRow(i).getCell(13)).length() != 0; i++) {
            if ((String.valueOf(sheet.getRow(i).getCell(10))).equals("")) {
                double euroInInvoiceWithEmptyCell = Math.abs(
                        Double.parseDouble(String.valueOf(sheet.getRow(i).getCell(15))));
                Date dataOfCases = sheet.getRow(i).getCell(13).getDateCellValue();
                if (tollCollect.searchInTollCollect(euroInInvoiceWithEmptyCell, dataOfCases)) {
                    writeValueToInvoice(i);
                }
            }
        }

//        Recursion
        SHEET = workbook.getSheetName(workbook.getSheetIndex(sheet) + 1);
        if (sheet.iterator().hasNext() && SHEET.length() > 6) {
            startFound();
        }
        tollCollect.closeInputStreamTollCollect();
        closeInputStreamInvoice();
    }


    public void writeValueToInvoice(int row) throws IOException {

        sheet.getRow(row).getCell(10).setCellValue(tollCollect.numberTollCollect);
        sheet.getRow(row).getCell(11).setCellValue(tollCollect.DATA_TOLL_COLLECT);
        sheet.getRow(row).getCell(12).setCellValue("Toll Collect");

        FileOutputStream out = new FileOutputStream(INVOICE);
        workbook.write(out);
        out.close();
    }

    public void closeInputStreamInvoice() throws IOException {
        fileInputStream.close();
    }
}

