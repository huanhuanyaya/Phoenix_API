package api.auto.day04;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProviderUtil {

    public static Object[][] getTestCaseData(int caseSheetInd,int apiSheetInt){
        Object[][] data;
        List<Object> caseInfoList = ExcelUtil.readExcel_V1("/testdata/APITest_Data.xlsx",caseSheetInd,CaseInfo.class);
        List<Object> apiInfoList = ExcelUtil.readExcel_V1("/testdata/APITest_Data.xlsx",apiSheetInt,ApiInfo.class);
        Map<String,ApiInfo> apiInfoMap = new HashMap<>();
        data = new Object[caseInfoList.size()][];
        for (int i = 0; i< caseInfoList.size(); i++) {
            CaseInfo[] itemArr = new CaseInfo[1];
            CaseInfo caseInfo = (CaseInfo) caseInfoList.get(i);
            //设置对应的apiInfo对象
            /**
             * 如果是从list里面通过匹配apiId去找对应的apiInfo信息,会增加很多不必要的循环,性能也比较差
             * 解决:将apiInfoList转成一个map去存储,key为apiId,value为apiInfo对象
             * 这样caseInfo再去设值apiInfo时只需要去map里取就行了
             */
            for(Object obj:apiInfoList){
                ApiInfo apiInfo = (ApiInfo) obj;
                apiInfoMap.put(apiInfo.getId(),apiInfo);
            }
            caseInfo.setApiInfo(apiInfoMap.get(caseInfo.getApiId()));
            itemArr[0] = caseInfo;
            data[i] = itemArr;
        }
        return data;
    }
}
