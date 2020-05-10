package api.auto.utils;

import api.auto.pojo.AssertKeyInfoObject;
import api.auto.pojo.CaseInfo;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.testng.Assert;

import java.util.List;

public class AssertUtil {
    /**
     * 断言响应体
     * @param caseInfo
     * @param result
     */
    public static void assertRespEntity(CaseInfo caseInfo, String result) {
        List<AssertKeyInfoObject> keyInfoObjects = JSONObject.parseArray(caseInfo.getAssertKeyInfo(),AssertKeyInfoObject.class);
        for (AssertKeyInfoObject object:keyInfoObjects
        ) {
            String jsonPath = object.getJsonPath();
            String expectedValue = object.getExpectedValue();

            Object doc = Configuration.defaultConfiguration().jsonProvider().parse(result);
            //提取出来的值
            Object extractData = JsonPath.read(doc,jsonPath);
            //断言
            Assert.assertEquals(extractData.toString(),expectedValue);
        }
    }
}
