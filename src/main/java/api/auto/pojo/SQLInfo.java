package api.auto.pojo;

import api.auto.utils.ExcelUtil;
import lombok.Data;

import java.util.List;

/**
 * Excel中第4个sheet每行
 */
@Data
public class SQLInfo extends ExcelObject{
    private String id;
    private String caseId;
    private String type;
    private String sql;
    private String expectedResult;
    private String ActualResult;
    private String testResult;

    public static void main(String[] args) {
        List<SQLInfo> sqlInfoList = (List<SQLInfo>) ExcelUtil.readExcel_V1("/testdata/APITest_Data.xlsx",3,SQLInfo.class);
        for (SQLInfo sqlInfo: sqlInfoList) {

            System.out.println(sqlInfo);
        }
    }
}
