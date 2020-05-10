package api.auto.utils;

import api.auto.pojo.CaseInfo;
import api.auto.pojo.ExtractRespDataObject;
import api.auto.pojo.HeaderOpt;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class ClientUtil {
    private static Logger logger = Logger.getLogger(ClientUtil.class);

    public static String get(String url, Map<String, String> parameterMap, CaseInfo caseInfo){
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
            handleReqHeader(get,caseInfo); //设置请求头,解决鉴权的问题
            CloseableHttpResponse resp;
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                resp = client.execute(get);
            }
            //如果respHeader为空, 不需要提取header;否则需要提取
            handleRespHeader(resp,caseInfo);

            return EntityUtils.toString(resp.getEntity()); //返回响应结果
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 处理响应头部信息的提取
     * @param resp
     * @param caseInfo
     */
    public static void handleRespHeader(CloseableHttpResponse resp,CaseInfo caseInfo) {
        if(!StringUtil.isEmpty(caseInfo.getRespHeader())){
            String caseHeader = caseInfo.getRespHeader();
            List<HeaderOpt> headerOptList = JSONObject.parseArray(caseHeader,HeaderOpt.class);
            for (HeaderOpt headerOpt: headerOptList) {
            String cookieName = headerOpt.getHeaderValue();
            Header firstHeader = resp.getFirstHeader(headerOpt.getHeaderName());
            HeaderElement[] headerElements = firstHeader.getElements();
                HeaderElement firstHeaderE = headerElements[0];
                String cookieVal = firstHeaderE.getName()+"="+firstHeaderE.getValue();
                //保存到全局数据池
                ParameterUtil.addGlobalData(cookieName,cookieVal);
            }
        }
    }

    /**
     * 设置响应头
     * @param request
     * @param caseInfo
     */
    public static void handleReqHeader(HttpRequest request, CaseInfo caseInfo){
        if(!StringUtil.isEmpty(caseInfo.getReqHeader())){
            String reqHeader = caseInfo.getReqHeader();
            List<HeaderOpt> headerOptList = JSONObject.parseArray(reqHeader,HeaderOpt.class);
            for (HeaderOpt headerOpt:headerOptList
            ) {
                String headerName = headerOpt.getHeaderName();
                String cookieName = headerOpt.getHeaderValue();
                String headerValue = ParameterUtil.getGlobalData(cookieName);
                request.setHeader(headerName,headerValue);
            }
        }
    }

    public static String post(String url, Map<String, String> parameterMap, CaseInfo caseInfo){
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
            handleReqHeader(post,caseInfo);
            CloseableHttpResponse resp;
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                resp = client.execute(post);
            }
            handleRespHeader(resp,caseInfo);
            return EntityUtils.toString(resp.getEntity());
        } catch (IOException e) {
            logger.error("调用post方法出现error!!!");
        }
        return "";
    }

    public static String get(CaseInfo caseInfo){
        //发起请求前,先对用例中的参数进行替换处理---参数化
        String replacedRequestParam = ParameterUtil.getReplaceStr(caseInfo.getRequestParams());
        Map<String,String> parameterMap = (Map<String, String>) JSONObject.parse(replacedRequestParam);
        String url = caseInfo.getApiInfo().getUrl();
        return get(url,parameterMap,caseInfo);
    }

    public static String post(CaseInfo caseInfo){
        //发起请求前,先对用例中的参数进行替换处理---参数化
        String replacedRequestParam = ParameterUtil.getReplaceStr(caseInfo.getRequestParams());
        Map<String,String> parameterMap = (Map<String, String>) JSONObject.parse(replacedRequestParam);
        String url = caseInfo.getApiInfo().getUrl();
        return post(url,parameterMap,caseInfo);
    }

    public static String request(CaseInfo caseInfo){
        String type = caseInfo.getApiInfo().getType();
        String result = "";
        if("get".equalsIgnoreCase(type)){
            result = get(caseInfo);
            logger.info("以get请求方式发送请求: "+caseInfo.getApiInfo().getName());
        }else if("post".equalsIgnoreCase(type)){
            result = post(caseInfo);
            logger.info("以post请求方式发送请求: "+caseInfo.getApiInfo().getName());
        }
        return result;
    }

    public static void main(String[] args) {
        /**
         * 鉴权:
         * 一般需要在请求头/参数中间加入鉴权的参数,如 session, cookie, token等
         */
    }

    /**
     * 从响应体中间去除数据,保存到全局数据池
     * @param result
     * @param caseInfo
     */
    public static void extractRespData(String result, CaseInfo caseInfo) {
        if(!StringUtil.isEmpty(caseInfo.getExtractRespData())){
            List<ExtractRespDataObject> objectList = JSONObject.parseArray(result,ExtractRespDataObject.class);
            for (ExtractRespDataObject respDataObj : objectList
            ) {
                String paramName = respDataObj.getParameterName();
                String jsonPath = respDataObj.getJsonPath();
                //一次性解析,后续就不用重复解析json了
                Object doc = Configuration.defaultConfiguration().jsonProvider().parse(result);
                //提取出来的值
                String extractResp = JsonPath.read(doc,jsonPath);
                //保存到全局数据池
                ParameterUtil.addGlobalData(paramName,extractResp);
            }
        }

    }
}
