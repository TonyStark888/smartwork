package com.hy.smartwork.excel.aggregator.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yingh
 */
@Data
public class McSymbol implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID")
    private Long id;

    /**
     * 交易品种代码
     */
    @Excel(name = "交易品种代码")
    private String mcSymbol;

    /**
     * 交易品种类型-枚举值
     */
    @Excel(name = "交易品种类型")
    private String symbolType;

    /**
     * 交易品种子类型-枚举值
     */
    @Excel(name = "交易品种子类型")
    private String symbolSubType;

    /**
     * 状态：0 关闭, 1 打开
     */
    @Excel(name = "状态")
    private Integer status;

    /**
     * 基础货币
     */
    @Excel(name = "基础货币")
    private String baseCurrency;

    /**
     * 计价货币
     */
    @Excel(name = "计价货币")
    private String pricingCurrency;

    /**
     * 价格精度
     */
    @Excel(name = "价格精度")
    private Integer pricingDegree;

    /**
     * 数量精度
     */
    @Excel(name = "数量精度")
    private Integer qtyDegree;

    /**
     * 点差精度
     */
    @Excel(name = "点差精度")
    private Integer spreadDegree;
}
