package api.auto.utils;

import api.auto.pojo.CaseInfo;
import api.auto.pojo.CellData;
import api.auto.pojo.SQLInfo;
import com.alibaba.fastjson.JSONObject;
import java.util.LinkedHashMap;
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
            List<LinkedHashMap<String, String>> actualResult = JDBCUtil.query(sql);
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
        Boolean flag = true;
        for (SQLInfo sqlInfo:afterSqlList) {
            String sql = sqlInfo.getSql();
            String expectResult = sqlInfo.getExpectedResult();
            List<LinkedHashMap<String, String>> actualResult = JDBCUtil.query(sql);
            String actualResultStr = JSONObject.toJSONString(actualResult);
            DataProviderUtil.addSqlData(new CellData(sqlInfo.getRowNum(),6,actualResultStr));
            if(actualResultStr.equalsIgnoreCase(expectResult)){
                DataProviderUtil.addCellData(new CellData(sqlInfo.getRowNum(),7,"pass"));
            }else{
                DataProviderUtil.addCellData(new CellData(sqlInfo.getRowNum(),7,"fail"));
                flag = false;
            }
        }
        if(flag){
            //验证通过
            DataProviderUtil.addCellData(new CellData(caseInfo.getRowNum(),7,"通过"));
        }else{
            //不通过的时候写回到excel中为红色字体,poi样式,后面找一下
            DataProviderUtil.addCellData(new CellData(caseInfo.getRowNum(),7,"不通过"));
        }
    }

}
