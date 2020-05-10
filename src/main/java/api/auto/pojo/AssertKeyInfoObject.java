package api.auto.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class AssertKeyInfoObject {
    private String jsonPath;
    private String expectedValue;


    public static void main(String[] args) {
        String json = "[{\"jsonPath\":\"$.msg\",\"expectedValue\":\"注册成功\"},{\"jsonPath\":\"$.code\",\"expectedValue\":\"200\"}]";
        List<AssertKeyInfoObject> keyInfoObjects = JSONObject.parseArray(json,AssertKeyInfoObject.class);
        for (AssertKeyInfoObject object:keyInfoObjects
             ) {
            System.out.println(object);
        }

    }
}
