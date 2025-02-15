package com.hy.smartwork.excel.aggregator.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author yingh
 */
@Data
public class McRuleMapping {

    /**
     * 主键ID
     */
    @Excel(name = "主键ID")
    private Long id;

    /**
     * 交易品种ID
     */
    @Excel(name = "交易品种ID")
    private Long symbolId;

    /**
     * 交易品种代码
     */
    @Excel(name = "交易品种代码")
    private String mcSymbol;

    /**
     * 聚合机制
     */
    @Excel(name = "聚合机制")
    private String aggregatorType;

    /**
     * 最小点差
     */
    @Excel(name = "最小点差")
    private Integer spreadMin;

    /**
     * 最大点差
     */
    @Excel(name = "最大点差")
    private Integer spreadMax;

    /**
     * 延迟阈值（毫秒）
     */
    @Excel(name = "延迟阈值")
    private Integer latencyThreshold;

    /**
     * 价差阈值（bps）
     */
    @Excel(name = "价差阈值")
    private Integer spreadThreshold;

    /**
     * 波动率阈值
     */
    @Excel(name = "波动幅度阈值")
    private BigDecimal volatilityThreshold;

    /**
     * 状态：0 关闭, 1 打开
     */
    @Excel(name = "状态")
    private Integer status;

    @Excel(name = "报价状态")
    private Integer currentStatus;

    /**
     * 合约单位
     */
    @Excel(name = "合约单位")
    private BigDecimal contractUnit;

    /**
     * 单边最大挂单敞口，单位为手
     */
    @Excel(name = "单边最大挂单敞口")
    private BigDecimal algoMaxUnilateralOpenPosition;

    /**
     * 最大允许仓位，单位为手
     */
    @Excel(name = "最大允许仓位")
    private BigDecimal algoMaxAllowedPosition;

    /**
     * 算法启用状态：0 关闭, 1 打开
     */
    @Excel(name = "算法启用状态")
    private Integer algoStatus;
}