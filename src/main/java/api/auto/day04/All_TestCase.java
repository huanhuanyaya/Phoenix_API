package api.auto.day04;

import api.auto.day05.CellData;
import api.auto.day05.ExcelUtil;
import api.auto.day05.DataProviderUtil;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class All_TestCase {

    @DataProvider
    public Object[][] getTestData(){
        return DataProviderUtil.getTestCaseData(1,2);
    }

    @Test(dataProvider = "getTestData")
    public void  register_test_case(CaseInfo caseInfo){
        System.out.println(caseInfo);
        String entityStr = ClientUtil.get(caseInfo);
        Assert.assertTrue(entityStr.contains(caseInfo.getExpectResponseData()));
        ExcelUtil.writeBackToExcel("/target/classes/testdata/APITest_Data.xlsx",1,caseInfo,4,entityStr);

    }

    @Test(dataProvider = "getTestData")
    public void  post_test_case(CaseInfo caseInfo){
        String entityStr = ClientUtil.post(caseInfo);
        Assert.assertTrue(entityStr.contains(caseInfo.getExpectResponseData()));
        CellData cellData = new CellData(caseInfo.getRowNum(),4,entityStr);
        DataProviderUtil.addCellData(cellData);
    }

    @AfterSuite
    public void afterSuite(){
        //收集测试结果数据,回写到excel表格中
        List<CellData> testResultList = DataProviderUtil.getDataToWritebackList();
        ExcelUtil.batchWriteBackToExcel("/testdata/APITest_Data.xlsx","/target/classes/testdata/APITest_Data.xlsx",1,testResultList);
    }


}
