package api.auto.day02;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login_TestCase {

    @DataProvider
    public Object[][] getTestData(){
        Object[][] data;
        data = new Object[][]{
                {"13200000001","passwd1","登录成功"},
                {"13200001","passwd1","账号不匹配"},
                {"13200000002","pass","账号不存在"}
        };
        return data;
    }

    @Test(dataProvider = "getTestData")
    public void test_case(String mobilePhone,String password,String expectResult){
        String uri = "/api/user/login";
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("paramName1",mobilePhone);
        paramsMap.put("paramName2",password);
        String entityStr = ClientUtil.post(uri,paramsMap);
        Assert.assertTrue(entityStr.contains(expectResult));
    }
}
