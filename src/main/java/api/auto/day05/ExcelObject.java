package api.auto.day05;

import lombok.Data;

@Data
public class ExcelObject {
    private int rowNum; //行号,从1开始

    public ExcelObject(int rowNum) {
        this.rowNum = rowNum;
    }

}
