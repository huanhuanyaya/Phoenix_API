package api.auto.day03;

import com.alibaba.fastjson.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class Login_TestCase {

    @DataProvider
    public Object[][] getTestData(){
        Object[][] data;
        data = ExcelUtil.readExcel("/testdata/APITest_Data.xlsx",2);

        return data;
    }

    @Test(dataProvider = "getTestData")
    public void test_case(String apiUrl,String jsonParams,String expectResult){
        HashMap<String,String> paramsMap = (HashMap<String, String>) JSONObject.parse(jsonParams);
        String entityStr = ClientUtil.post(apiUrl,paramsMap);
        Assert.assertTrue(entityStr.contains(expectResult));
    }
}
