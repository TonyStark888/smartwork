package com.hy.smartwork.excel.aggregator.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class MarketSourceSymbol implements Serializable {
    /**
     * 主键ID
     */
    @Excel(name = "主键ID")
    private Long id;

    /**
     * LP交易品种名称
     */
    @Excel(name = "LP交易品种编码")
    private String lpSymbol;

    /**
     * 行情源ID
     */
    @Excel(name = "行情源ID")
    private Long marketSourceId;

    /**
     * LP编码
     */
    @Excel(name = "LP编码")
    private String lpCode;

    /**
     * 交易所
     */
    @Excel(name = "交易所编号")
    private String exchange;

    /**
     * 交易货币
     */
    @Excel(name = "币种编码")
    private String currency;

    /**
     * 交易品种类型 - 枚举值
     */
    @Excel(name = "交易品种类型")
    private String symbolType;

    /**
     * 交易品种子类型 - 枚举值
     */
    @Excel(name = "交易品种子类型")
    private String symbolSubType;

    /**
     * 状态：0 关闭, 1 打开
     */
    @Excel(name = "状态")
    private Integer status;

    /**
     * 报价状态：0:未知、1:连接、 2:断连、 3:异常 (参见QuoteStatusEnum)
     */
    @Excel(name = "报价状态")
    private Integer currentStatus;
}
