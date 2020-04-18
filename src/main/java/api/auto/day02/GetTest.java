package api.auto.day02;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetTest {

    @Test(dataProvider = "getTestData")
    //Get请求
    public void test_case_01(String paramStr,String expectResult) throws IOException {

        /**
         * 发包
         * 1. 准备url
         * 2. 设置请求方法
         * 3. 发包,拿到响应
         * 4. 验证响应结果是否符合预期
         */
        String baseUri = "localhost:8888/api/user/register";
        HttpGet get = new HttpGet(baseUri+"?"+paramStr); //2
        CloseableHttpClient client = HttpClients.createDefault(); //3.1(发包)
        CloseableHttpResponse resp = client.execute(get);   //3.2(得到响应)
        String entityStr = EntityUtils.toString(resp.getEntity());
        System.out.println(entityStr);
        Assert.assertTrue(entityStr.contains(expectResult));//简单的断言

    }


    @Test
    public void  test_case_02(){
        String uri = "/api/user/login";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("paramName1","ParamValue1"));
        params.add(new BasicNameValuePair("paramName2","ParamValue2"));
        String entityStr = ClientUtil.get(uri, (Map<String, String>) params);
        Assert.assertTrue(entityStr.contains("expectResult"));
    }

    @Test(dataProvider = "getTestData")
    public void  test_case_03(String mobilePhone,String password,String expectResult){
        String uri = "/api/user/login";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("paramName1",mobilePhone));
        params.add(new BasicNameValuePair("paramName2",password));
        String entityStr = ClientUtil.get(uri, (Map<String, String>) params);
        Assert.assertTrue(entityStr.contains(expectResult));
    }

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

}
