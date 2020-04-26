package api.auto.utils;

import api.auto.pojo.ApiInfo;
import api.auto.pojo.CaseInfo;
import api.auto.pojo.CellData;

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
        data = new Object[caseInfoList.size()][];
        for (int i = 0; i< caseInfoList.size(); i++) {
            CaseInfo caseInfo = caseInfoList.get(i);
//            String apiId = caseInfo.getApiId();
//            ApiInfo apiInfo = apiInfoMap.get(apiId);
            caseInfo.setApiInfo(apiInfoMap.get(caseInfo.getApiId()));
            CaseInfo[] itemArr = {caseInfo};
            //设置对应的apiInfo对象
            data[i] = itemArr;
        }
        return data;
    }
}
