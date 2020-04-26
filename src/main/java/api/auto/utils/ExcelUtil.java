package api.auto.utils;

import api.auto.pojo.CaseInfo;
import api.auto.pojo.CellData;
import api.auto.pojo.ExcelObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExcelUtil {
    public static void main(String[] args) {
        CaseInfo caseInfo = new CaseInfo();
        writeBackToExcel("",1,caseInfo,4,"hello");
    }

    /**
     * 基于poi技术进行数据回写
     * @param excelPath
     */
    public static void writeBackToExcel(String excelPath, int sheetInd, CaseInfo caseInfo,int cellNo, String contentValue) {
        InputStream is = null;
        Workbook book = null;
        OutputStream os = null;
        try {
           is = new FileInputStream(new File(excelPath));
           book = WorkbookFactory.create(is);
           Sheet sheet = book.getSheetAt(sheetInd);
            //通过caseId拿到对应的行号
           for(int i = 1; i<sheet.getLastRowNum();i++){
               Row currentRow = sheet.getRow(i);  //得到当前行
               Cell caseIdCell = currentRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
               caseIdCell.setCellType(CellType.STRING);
               String caseIdValue = caseIdCell.getStringCellValue();
               if(caseIdValue.equals(caseInfo.getId())){
                   //i此时就是我们需要的行号
                   Cell cell = currentRow.getCell(cellNo-1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                   cell.setCellValue(contentValue);
                   break;
               }
           }
           os = new FileOutputStream(new File(excelPath));
           book.write(os);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeFlow(is,book,os);
        }
    }
    @Deprecated
    public static void batchWriteBackToExcel(String sourcePath,String targetPath, int sheetInd,List<CellData> testResultList) {
        InputStream is = null;
        Workbook book = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File(sourcePath));
            book = WorkbookFactory.create(is);
            Sheet sheet = book.getSheetAt(sheetInd);
            for (CellData testResult: testResultList) {
                int rowNo = testResult.getRowNum();
                int cellNo = testResult.getCellNum();
                String contentValue = testResult.getContent();
                Row currentRow = sheet.getRow(rowNo - 1);
                Cell cell = currentRow.getCell(cellNo - 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(contentValue);

            }
            os = new FileOutputStream(new File(targetPath));
            book.write(os);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeFlow(is,book,os);
        }
    }

    public static void batchWriteBackToExcel(String sourcePath,String targetPath) {
        InputStream is = null;
        Workbook book = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File(sourcePath));
            book = WorkbookFactory.create(is);
            //回写case执行结果
            Sheet caseSheet = book.getSheetAt(1);
            List<CellData> caseResultList = DataProviderUtil.getDataToWriteBackList();
            for (CellData testResult: caseResultList) {
                int rowNo = testResult.getRowNum();
                int cellNo = testResult.getCellNum();
                String contentValue = testResult.getContent();
                Row currentRow = caseSheet.getRow(rowNo - 1);
                Cell cell = currentRow.getCell(cellNo - 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(contentValue);
            }
            //回写sql验证结果
            Sheet sqlSheet = book.getSheetAt(3);
            List<CellData> sqlResultList = DataProviderUtil.getSqlToWriteBackList();
            for (CellData testResult: sqlResultList) {
                int rowNo = testResult.getRowNum();
                int cellNo = testResult.getCellNum();
                String contentValue = testResult.getContent();
                Row currentRow = sqlSheet.getRow(rowNo - 1);
                Cell cell = currentRow.getCell(cellNo - 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(contentValue);
            }
            os = new FileOutputStream(new File(targetPath));
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

    public static List<? extends ExcelObject> readExcel_V1(String excelPath, int sheetInd, Class<? extends ExcelObject> clazz) {
        List<ExcelObject> excelDataList = new ArrayList<>();
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
                ExcelObject sheetData = clazz.getDeclaredConstructor().newInstance();
                sheetData.setRowNum(i+1); //设置行对应的行号属性
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

    @Deprecated
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
