package api.auto.day05;

import lombok.Data;

@Data
public class CellData extends ExcelObject{
    //要写回excel测试结果的数据信息描述
    private int cellNum;
    private String content;


    public CellData(int rowNum, int cellNum, String content) {
        super(rowNum);
        this.cellNum = cellNum;
        this.content = content;
    }
}
