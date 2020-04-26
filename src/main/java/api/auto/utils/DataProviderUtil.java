package api.auto.utils;

import api.auto.pojo.ApiInfo;
import api.auto.pojo.CaseInfo;
import api.auto.pojo.CellData;
import api.auto.pojo.SQLInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProviderUtil {
    private static List<CellData> dataToWriteBackList = new ArrayList<>();
    public static void addCellData(CellData cellData){
        dataToWriteBackList.add(cellData);
    }
    public static List<CellData> getDataToWriteBackList(){
        return dataToWriteBackList;
    }

    private static List<CellData> sqlToWriteBackList = new ArrayList<>();
    public static void addSqlData(CellData cellData){
        sqlToWriteBackList.add(cellData);
    }
    public static List<CellData> getSqlToWriteBackList(){
        return sqlToWriteBackList;
    }
    public static Object[][] getTestCaseData(int caseSheetInd,int apiSheetInt){
        Object[][] data;
        List<CaseInfo> caseInfoList = (List<CaseInfo>) ExcelUtil.readExcel_V1("/testdata/APITest_Data.xlsx",caseSheetInd,CaseInfo.class);
        List<ApiInfo> apiInfoList = (List<ApiInfo>) ExcelUtil.readExcel_V1("/testdata/APITest_Data.xlsx",apiSheetInt,ApiInfo.class);
        Map<String,ApiInfo> apiInfoMap = new HashMap<>();
        /**
         * 如果是从list里面通过匹配apiId去找对应的apiInfo信息,会增加很多不必要的循环,性能也比较差
         * 解决:将apiInfoList转成一个map去存储,key为apiId,value为apiInfo对象
         * 这样caseInfo再去设值apiInfo时只需要去map里取就行了
         */
        for(ApiInfo apiInfo:apiInfoList){
            apiInfoMap.put(apiInfo.getId(),apiInfo);
        }
        //读取所有的数据验证sql信息
        List<SQLInfo> sqlInfoList = (List<SQLInfo>) ExcelUtil.readExcel_V1("/testdata/APITest_Data.xlsx",3,SQLInfo.class);
        Map<String,List<SQLInfo>> sqlMap = new HashMap<>();
        for(SQLInfo sqlInfo: sqlInfoList){
            String mapKey = sqlInfo.getId() + "-" + sqlInfo.getType();
            List<SQLInfo> subSqlList = sqlMap.get(mapKey);
            if(subSqlList == null){
                subSqlList = new ArrayList<>();
            }
            subSqlList.add(sqlInfo);
            sqlMap.put(mapKey,subSqlList);
        }
        data = new Object[caseInfoList.size()][];
        for (int i = 0; i< caseInfoList.size(); i++) {
            CaseInfo caseInfo = caseInfoList.get(i);
            caseInfo.setApiInfo(apiInfoMap.get(caseInfo.getApiId()));
            //设置当前caseInfo的前置sql和后置sql列表
            String caseId = caseInfo.getApiInfo().getId();
            caseInfo.setBeforeSqlList(sqlMap.get(caseId+"-before"));
            caseInfo.setAfterSqlList(sqlMap.get(caseId+"-after"));
            CaseInfo[] itemArr = {caseInfo};
            //设置对应的apiInfo对象
            data[i] = itemArr;
        }
        return data;
    }
}
