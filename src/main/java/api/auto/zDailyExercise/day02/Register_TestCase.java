package api.auto.zDailyExercise.day02;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

public class Register_TestCase {

    @DataProvider
    public Object[][] getTestData(){
        Object[][] data;
        data = new Object[][]{
                {"13200000001","passwd1","号码已注册"},
                {"13200001","passwd1","号码格式不正确"},
                {"13200000002","pass","长度必须为6~18位"}
        };
        return data;
    }

    @Test(dataProvider = "getTestData")
    public void  test_case(String apiUrl,String mobilePhone,String password,String expectResult){
//        String uri = "/api/user/register";
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("paramName1",mobilePhone);
        paramsMap.put("paramName2",password);
        String entityStr = ClientUtil.get(apiUrl,paramsMap);
        Assert.assertTrue(entityStr.contains(expectResult));
    }
}
