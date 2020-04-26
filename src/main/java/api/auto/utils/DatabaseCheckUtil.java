package api.auto.utils;

import api.auto.pojo.CaseInfo;
import api.auto.pojo.CellData;
import api.auto.pojo.SQLInfo;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;

public class DatabaseCheckUtil {
    public static void beforeCheck(CaseInfo caseInfo){
        List<SQLInfo> beforeSqlList = caseInfo.getBeforeSqlList();
        if(beforeSqlList == null){
            return;
        }
        for (SQLInfo sqlInfo:beforeSqlList) {
            String sql = sqlInfo.getSql();
            String expectResult = sqlInfo.getExpectedResult();
            List<HashMap<String,String>> actualResult = JDBCUtil.query(sql);
            String actualResultStr = JSONObject.toJSONString(actualResult);
            DataProviderUtil.addSqlData(new CellData(sqlInfo.getRowNum(),6,actualResultStr));
            if(actualResultStr.equalsIgnoreCase(expectResult)){
                DataProviderUtil.addCellData(new CellData(sqlInfo.getRowNum(),7,"pass"));
            }else{
                DataProviderUtil.addCellData(new CellData(sqlInfo.getRowNum(),7,"fail"));
            }
        }
    }

    public static void afterCheck(CaseInfo caseInfo){
        List<SQLInfo> afterSqlList = caseInfo.getBeforeSqlList();
        if(afterSqlList == null){
            return;
        }
        for (SQLInfo sqlInfo:afterSqlList) {
            String sql = sqlInfo.getSql();
            String expectResult = sqlInfo.getExpectedResult();
            List<HashMap<String,String>> actualResult = JDBCUtil.query(sql);
            String actualResultStr = JSONObject.toJSONString(actualResult);
            DataProviderUtil.addSqlData(new CellData(sqlInfo.getRowNum(),6,actualResultStr));
            if(actualResultStr.equalsIgnoreCase(expectResult)){
                DataProviderUtil.addCellData(new CellData(sqlInfo.getRowNum(),7,"pass"));
            }else{
                DataProviderUtil.addCellData(new CellData(sqlInfo.getRowNum(),7,"fail"));
            }
        }
    }

}
