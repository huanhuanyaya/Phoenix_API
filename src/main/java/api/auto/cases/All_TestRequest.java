package api.auto.cases;

import api.auto.pojo.CaseInfo;
import api.auto.pojo.CellData;
import api.auto.utils.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class All_TestRequest {

    @BeforeSuite
    public void beforeSuite(){
        //参数化,设置参数
        ParameterUtil.addGlobalData("mobile","12312300001");
        ParameterUtil.addGlobalData("pwd","121212");
        ParameterUtil.addGlobalData("nick","haha");
    }

    @DataProvider
    public Object[][] getTestData(){
        return DataProviderUtil.getTestCaseData(1,2);
    }

    @Test(dataProvider = "getTestData")
    public void  all_test_case(CaseInfo caseInfo){
        DatabaseCheckUtil.beforeCheck(caseInfo);

        String result = ClientUtil.request(caseInfo);
        ClientUtil.extractRespData(result,caseInfo);

        CellData caseCellData = new CellData(caseInfo.getRowNum(),4,result+": pass");
        DataProviderUtil.addCellData(caseCellData);
        DatabaseCheckUtil.afterCheck(caseInfo);

        //断言响应结果
        AssertUtil.assertRespEntity(caseInfo,result);
    }

    @AfterTest
    public void afterTest(){
        //TODO:将文件目录用properties文件处理一下,解决下硬编码---目前写不回去,读取文件目录时报找不到文件目录的错

        ExcelUtil.batchWriteBackToExcel("/testdata/APITest_Data.xlsx","/target/classes/testdata/APITest_Data_1.xlsx");
    }

    public static void main(String[] args) {
        ParameterUtil.addGlobalData("mobile","12312300001");
        ParameterUtil.addGlobalData("pwd","123456");
        String targetStr = "{\"mobile\":\"${mobile}\",\"pwd\":\"${pwd}\",\"regName\":\"test\"}";
        String replacedStr = ParameterUtil.getReplaceStr(targetStr);
        System.out.println(replacedStr);

    }
}
