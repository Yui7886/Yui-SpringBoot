package com.yui.base.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.mybatisflex.annotation.Column;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础实体类
 *
 * @author yui
 */
@Data
@ApiModel(value = "基础实体类")
public class BaseEntity implements Serializable {

    /**
     * 当前页面数据量
     */
    @ApiModelProperty(value = "当前页面数据量")
    @ExcelIgnore
    @Column(ignore = true)
    private int pageSize;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    @ExcelIgnore
    @Column(ignore = true)
    private int pageNum;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    @ExcelIgnore
    @Column(ignore = true)
    private String field = "id";

    /**
     * 排序规则，asc升序，desc降序
     */
    @ApiModelProperty(value = "排序规则")
    @ExcelIgnore
    @Column(ignore = true)
    private String order = "desc";

    /**
     * 自定义排序字符串
     */
    @ApiModelProperty(value = "自定义排序字符串")
    @ExcelIgnore
    @Column(ignore = true)
    private String columnsOrderStr;

    /**
     * 快速搜索条件
     */
    @ApiModelProperty(value = "快速搜索条件")
    @ExcelIgnore
    @Column(ignore = true)
    private String searchValue;

}
