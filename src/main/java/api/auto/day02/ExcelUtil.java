package api.auto.day02;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;

public class ExcelUtil {
    public static void main(String[] args) {
        Object[][] testData = readExcel_V1("/testdata/APITest_Data.xlsx",0);
        for (Object[] row:testData
             ) {
            for (Object cellValue:row
                 ) {
                System.out.print(cellValue+", ");
            }
            System.out.println();
        }

    }

    private static Object[][] readExcel_V1(String excelPath,int sheetInd) {
        Object[][] excelDataArray = null;
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = ExcelUtil.class.getResourceAsStream(excelPath);
            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetInd);
            int lastRowNum = sheet.getLastRowNum();
            excelDataArray = new Object[lastRowNum][];
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                int lastCellNum = row.getLastCellNum();
                Object[] excelCellData = new Object[lastCellNum];
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    excelCellData[j] = cell.getStringCellValue();
                }
                excelDataArray[i-1] = excelCellData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeFlow(inputStream, workbook);
        }
        return excelDataArray;
    }

    private static void closeFlow(InputStream inputStream, Workbook workbook) {
        if(workbook != null){
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
