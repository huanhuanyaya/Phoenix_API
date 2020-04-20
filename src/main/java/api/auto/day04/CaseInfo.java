package api.auto.day04;
import lombok.Data;

/**
 * 测试用例详细信息
 */
@Data
public class CaseInfo {
    private String id;
    private String apiId;
    private String requestParams;
    private String expectResponseData;
    private String actualResponseData;
    private String isExcute;
    private ApiInfo apiInfo;


}
