package api.auto.day04;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExcelUtil {
    public static void main(String[] args) {
        writeBackToExcel("","");
    }

    /**
     * 基于poi技术进行数据回写
     * @param sourcePath
     * @param targetPath
     */
    public static void writeBackToExcel(String sourcePath,String targetPath) {
        InputStream is = null;
        Workbook book = null;
        OutputStream os = null;
        try {
           is = ExcelUtil.class.getResourceAsStream(sourcePath);
           book = WorkbookFactory.create(is);
           Sheet sheet = book.getSheetAt(1);
           Row row = sheet.getRow(1);
           Cell cell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
           cell.setCellValue("hello test");

           os = new FileOutputStream(targetPath);
           book.write(os);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeFlow(is,book,os);
        }
    }

    private static void closeFlow(InputStream is, Workbook book, OutputStream os) {
        closeFlow(is,book);
        if(os != null){
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static List<Object> readExcel_V1(String excelPath, int sheetInd, Class clazz) {
        List<Object> excelDataList = new ArrayList<>();
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = ExcelUtil.class.getResourceAsStream(excelPath);
            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetInd);
            int lastRowNum = sheet.getLastRowNum();
            /**------------------读取表头的数据--------------------*/
            Row firstRow = sheet.getRow(0);  //得到表头
            int maxCellNum = firstRow.getLastCellNum();
            //创建一个容器来保存表头的所有字段
            String[] filedArray = new String[maxCellNum];
            for(int i = 0;i<maxCellNum;i++){
                Cell cell = firstRow.getCell(i);
                cell.setCellType(CellType.STRING);
                String firstCellValue = cell.getStringCellValue();
                //处理下表头的字段名(字段名可能有备注信息,要去掉),让它像一个属性值的名字
                String[] fileChar = firstCellValue.split("_");
                IntStream.range(0, fileChar.length).forEach(index -> fileChar[index] = fileChar[index].substring(0, 1).toUpperCase()+fileChar[index].substring(1));
                filedArray[i] = StringUtil.join(fileChar,"");
            }
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                //生成一个对应的对象,要知道拿到的是api信息还是case信息
                Object sheetData = clazz.getDeclaredConstructor().newInstance();
                for (int j = 0; j < maxCellNum; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    String setterName = "set" + filedArray[j];
                    Method method = clazz.getMethod(setterName,String.class);
                    method.invoke(sheetData,cell.getStringCellValue());
                }
                excelDataList.add(sheetData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeFlow(inputStream, workbook);
        }
        return excelDataList;
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

    public static Object[][] readExcel(String excelPath, int sheetInd) {
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

}
