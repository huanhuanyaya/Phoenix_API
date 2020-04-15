package api.auto.day01;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApiTest {
    public static void main(String[] args) throws IOException {
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
    }
}
