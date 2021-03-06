package api.auto.zDailyExercise.day03;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientUtil {

    public static String get(String url, Map<String,String> parameterMap){
        try {
            HttpGet get;
            if(parameterMap != null){
                List<NameValuePair> parameters = new ArrayList<>();
                Set<String> keySet = parameterMap.keySet();
                for(String key: keySet){
                    parameters.add(new BasicNameValuePair(key,parameterMap.get(key)));
                }
                get = new HttpGet(url+"?"+URLEncodedUtils.format(parameters,"utf-8"));
            }else{
                get = new HttpGet(url);
            }
            CloseableHttpResponse resp;
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                resp = client.execute(get);
            }
            return EntityUtils.toString(resp.getEntity()); //返回响应结果
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String post(String url, Map<String,String> parameterMap){
        try {
            HttpPost post = new HttpPost(url);
            if(parameterMap != null){
                Set<String> keySet = parameterMap.keySet();
                List<NameValuePair> parameters = new ArrayList<>();
                for(String key: keySet){
                    parameters.add(new BasicNameValuePair(key,parameterMap.get(key)));
                }
                post.setEntity(new UrlEncodedFormEntity(parameters));
            }
            CloseableHttpResponse resp;
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                resp = client.execute(post);
            }
            return EntityUtils.toString(resp.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";


    }
}
