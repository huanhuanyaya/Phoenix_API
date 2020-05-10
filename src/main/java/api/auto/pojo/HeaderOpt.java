package api.auto.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class HeaderOpt {
    private String headerName;
    private String headerValue;

    public static void main(String[] args) {
        String str = "[{\"headerName\":\"set-Cookie\",\"headerValue\":\"cookie\"},{\"headerName\":\"example\",\"headerValue\":\"exampleVa\"}]";
        List<HeaderOpt> headerOptList = JSONObject.parseArray(str,HeaderOpt.class);
        for (HeaderOpt header: headerOptList
             ) {
            System.out.println(header);
        }
    }

}
