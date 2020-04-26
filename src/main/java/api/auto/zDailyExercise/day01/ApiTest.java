package api.auto.zDailyExercise.day01;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiTest {

    @Test
    //Get请求
    public void test_case_01() throws IOException {
        /**
         * 发包
         * 1. 准备url
         * 2. 设置请求方法
         * 3. 发包,拿到响应
         * 4. 验证响应结果是否符合预期
         */
        String uri = "";  //1
        HttpGet get = new HttpGet(uri); //2
        CloseableHttpClient client = HttpClients.createDefault(); //3.1(发包)
        CloseableHttpResponse resp = client.execute(get);   //3.2(得到响应)
        //状态行/响应头/响应体
        StatusLine statusLine = resp.getStatusLine();
        System.out.println(statusLine.getProtocolVersion().toString()+" ");
        System.out.println(statusLine.getReasonPhrase()+" ");
        System.out.println(statusLine.getStatusCode()+" ");

        Header[] headers = resp.getAllHeaders();
        for (Header header : headers) {
            System.out.println(header.getName()+": "+header.getValue());
        }
        String entityStr = EntityUtils.toString(resp.getEntity());
        System.out.println(entityStr);
        Assert.assertTrue(entityStr.contains("指定的文字"));//简单的断言

    }

    @Test
    //post请求
    public void test_case_02() throws IOException {
        String uri = "testUrl";  //1
        HttpPost post = new HttpPost(uri); //2
        //post请求设置请求体(参数列表)
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("paramName1","ParamValue1"));
        params.add(new BasicNameValuePair("paramName2","ParamValue2"));
        params.add(new BasicNameValuePair("paramName3","ParamValue3"));

        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpClient client = HttpClients.createDefault(); //3.1(发包)
        CloseableHttpResponse resp = client.execute(post);   //3.2(得到响应)

        StatusLine statusLine = resp.getStatusLine();
        System.out.println(statusLine.getProtocolVersion().toString()+" ");
        System.out.println(statusLine.getReasonPhrase()+" ");
        System.out.println(statusLine.getStatusCode()+" ");

        Header[] headers = resp.getAllHeaders();
        for (Header header : headers) {
            System.out.println(header.getName()+": "+header.getValue());
        }
        String entityStr = EntityUtils.toString(resp.getEntity());
        System.out.println(entityStr);
        Assert.assertTrue(entityStr.contains("指定的文字"));//简单的断言
        EntityUtils.consume(resp.getEntity()); //

    }

}
