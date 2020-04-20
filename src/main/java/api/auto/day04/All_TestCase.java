package api.auto.day04;

import com.alibaba.fastjson.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    }

    @Test(dataProvider = "getTestData")
    public void  post_test_case(CaseInfo caseInfo){
        System.out.println(caseInfo);
        String entityStr = ClientUtil.post(caseInfo);
        Assert.assertTrue(entityStr.contains(caseInfo.getExpectResponseData()));

    }

}
