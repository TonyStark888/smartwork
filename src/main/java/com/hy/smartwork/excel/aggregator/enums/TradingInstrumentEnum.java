package com.hy.smartwork.excel.aggregator.enums;

/**
 * Title:       [交易品种枚举]
 * Description: [用于表示不同的交易品种，包括股票、期货、外汇、加密货币和期权]
 * Created on:  2025-01-01
 * Author:      mark
 */
public enum TradingInstrumentEnum {

    /**
     * 股票
     */
    STOCK(1, "股票"),

    /**
     * 期货
     */
    FUTURES(2, "期货"),

    /**
     * 外汇
     */
    FOREX(3, "外汇"),

    /**
     * 加密货币
     */
    CRYPTO(4, "数字货币"),

    /**
     * 期权
     */
    OPTIONS(5, "期权"),

    /**
     * CFD
     */
    CFD(6, "CFD"),

    /**
     * 未知
     */
    UNKNOWN(0, "-");

    private final int code;
    private final String description;

    TradingInstrumentEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据中文描述获取编码
     *
     * @param desc
     * @return
     */
    public static Integer fromDesc(String desc) {
        for (TradingInstrumentEnum instrument : values()) {
            if (instrument.description.equalsIgnoreCase(desc)) {
                return instrument.getCode();
            }
        }
        return UNKNOWN.getCode();
    }

    /**
     * 根据中文描述获取编码
     *
     * @param desc
     * @return
     */
    public static TradingInstrumentEnum enumFromDesc(String desc) {
        for (TradingInstrumentEnum instrument : values()) {
            if (instrument.description.equalsIgnoreCase(desc)) {
                return instrument;
            }
        }
        return UNKNOWN;
    }
}