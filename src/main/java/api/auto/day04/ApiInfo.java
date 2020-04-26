package api.auto.day04;

import api.auto.day05.ExcelObject;
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
