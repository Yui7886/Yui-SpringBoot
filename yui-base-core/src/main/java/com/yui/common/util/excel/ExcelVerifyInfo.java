package com.yui.common.util.excel;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * easypoi 获取行号和错误信息
 *
 * @author yui
 */
@Data
public class ExcelVerifyInfo implements IExcelModel, IExcelDataModel {

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 行数
     */
    private int rowNum;

    @Override
    public Integer getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
