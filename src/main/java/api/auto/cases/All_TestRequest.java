package api.auto.cases;

import api.auto.pojo.CaseInfo;
import api.auto.pojo.CellData;
import api.auto.utils.*;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class All_TestRequest {

    @DataProvider
    public Object[][] getTestData(){
        return DataProviderUtil.getTestCaseData(1,2);
    }

    @Test(dataProvider = "getTestData")
    public void  all_test_case(CaseInfo caseInfo){
        String result = ClientUtil.request(caseInfo);
        Assert.assertTrue(result.contains(caseInfo.getExpectResponseData()));
        CellData caseCellData = new CellData(caseInfo.getRowNum(),4,result+": pass");
        DataProviderUtil.addCellData(caseCellData);
        DatabaseCheckUtil.beforeCheck(caseInfo);
        DatabaseCheckUtil.afterCheck(caseInfo);
    }

    @AfterTest
    public void afterTest(){
        //TODO:将文件目录用properties文件处理一下,解决下硬编码---目前写不回去,读取文件目录时报找不到文件目录的错

        ExcelUtil.batchWriteBackToExcel("/testdata/APITest_Data.xlsx","/target/classes/testdata/APITest_Data_1.xlsx");
    }
}
