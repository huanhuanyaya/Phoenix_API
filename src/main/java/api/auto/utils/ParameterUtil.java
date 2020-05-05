package api.auto.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 接口参数的参数化
 * 1. 完成接口的关联
 * 2. 分离出静态的数据,使接口测试用例更便于维护
 */
public class ParameterUtil {
    //创建参数化的容器
    private static Map<String,String> globalDataMap = new HashMap<>();

    public static String getGlobalData(String parameterKey){
        return globalDataMap.get(parameterKey);
    }

    public static void addGlobalData(String parameterKey,String parameterValue){
        globalDataMap.put(parameterKey,parameterValue);
    }

    public static String getReplaceStr(String targetStr){
        //把所有符合参数化规则的字符串提取出来(通过正则表达式)
        String regex = "\\$\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(targetStr);
        while(matcher.find()){
            targetStr = targetStr.replace(matcher.group(0),getGlobalData(matcher.group(1)));
        }
            return targetStr;
    }

    //TODO:使用json-path完成接口自动化测试中参数关联和数据验证
    public static void main(String[] args) {
        String jsonStr = "";
        Object doc = Configuration.defaultConfiguration().jsonProvider().parse(jsonStr);
        Object idStr = JsonPath.read(doc,"$.data.id");
        System.out.println(idStr);


    }
}
