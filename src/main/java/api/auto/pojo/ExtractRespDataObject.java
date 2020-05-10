package api.auto.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class ExtractRespDataObject {
    private String parameterName;
    private String jsonPath;

    public static void main(String[] args) {

        String json = "[{\"parameterName\":\"userId\",\"jsonPath\":\"$.data.id\"},{\"parameterName\":\"leaveAmount\",\"jsonPath\":\"$.data.leaveamount\"}]";
        List<ExtractRespDataObject> objectList = JSONObject.parseArray(json,ExtractRespDataObject.class);
        for (ExtractRespDataObject e:objectList
             ) {
            System.out.println(e);
        }

    }
}
