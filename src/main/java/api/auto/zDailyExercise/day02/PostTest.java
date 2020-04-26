package api.auto.zDailyExercise.day02;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostTest {
    @Test
    //post请求
    public void test_case_01() throws IOException {
        String uri = "testUrl";  //1
        HttpPost post = new HttpPost(uri); //2
        //post请求设置请求体(参数列表)
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("paramName1","ParamValue1"));
        params.add(new BasicNameValuePair("paramName2","ParamValue2"));
        params.add(new BasicNameValuePair("paramName3","ParamValue3"));

        StringEntity entity = new StringEntity(URLEncodedUtils.format(params,"utf-8"), ContentType.APPLICATION_FORM_URLENCODED);
        post.setEntity(entity);

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

    @Test
    public void test_case_02(){
        String uri = "/api/user/login";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("paramName1","ParamValue1"));
        params.add(new BasicNameValuePair("paramName2","ParamValue2"));
        params.add(new BasicNameValuePair("paramName3","ParamValue3"));
        ClientUtil.post(uri, (Map<String, String>) params);
    }
}
