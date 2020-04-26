package api.auto.pojo;

import lombok.Data;

/**
 * 接口详细信息
 */
@Data
public class ApiInfo extends ExcelObject {
    private String id;
    private String name;
    private String type;
    private String url;

}
