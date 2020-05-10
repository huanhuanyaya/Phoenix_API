package api.auto.pojo;
import lombok.Data;

import java.util.List;

/**
 * 测试用例详细信息
 */
@Data
public class CaseInfo extends ExcelObject {
    private String id;
    private String apiId;
    private String requestParams;
    private String expectResponseData;
    private String actualResponseData;
    private String isExcute;
    private List<SQLInfo> beforeSqlList;
    private List<SQLInfo> afterSqlList;
    private ApiInfo apiInfo;
    private String testResult;
    private String sqlCheckResult;
    private String reqHeader;
    private String respHeader;
    private String extractRespData;
    private String assertKeyInfo;
}
