package com.hy.smartwork.excel.aggregator.enums;

/**
 * Title:       [交易品种子类型枚举]
 * Description: [用于表示不同交易品种的子类型，带有数字值]
 * Created on:  2025-01-01
 * Author:      mark
 */
public enum TradingInstrumentSubTypeEnum {

    // 股票子类型
    STOCK_HK(0, "STOCK", "港股"),
    STOCK_US(1, "STOCK", "美股"),
    STOCK_CN(2, "STOCK", "A股"),

    // 期货子类型
    FUTURES_STOCK(0, "FUTURES", "个股"),
    FUTURES_ENERGY(1, "FUTURES", "能源"),
    FUTURES_METAL(2, "FUTURES", "金属"),
    FUTURES_INDEX(3, "FUTURES", "指数"),

    // 加密货币子类型
    CRYPTO_SPOT(0, "CRYPTO", "现货"),
    CRYPTO_CONTRACT(1, "CRYPTO", "永续合约"),

    // 期权子类型
    OPTIONS_STOCK(0, "OPTIONS", "个股"),
    OPTIONS_ENERGY(1, "OPTIONS", "能源"),
    OPTIONS_METAL(2, "OPTIONS", "金属"),
    OPTIONS_INDEX(3, "OPTIONS", "指数"),
    OPTIONS_CRYPTO(4, "OPTIONS", "加密货币"),

    /**
     * 未知
     */
    UNKNOWN(0, "UNKNOWN", "-");

    private final int code;
    private final String parentType;
    private final String subTypeDescription;

    TradingInstrumentSubTypeEnum(int code, String parentType, String subTypeDescription) {
        this.code = code;
        this.parentType = parentType;
        this.subTypeDescription = subTypeDescription;
    }

    public int getCode() {
        return code;
    }

    public String getParentType() {
        return parentType;
    }

    public String getSubTypeDescription() {
        return subTypeDescription;
    }

    public static Integer fromDesc(String parentDesc, String subTypeDescription) {
        if("/".equalsIgnoreCase(subTypeDescription)) {
            return 0;
        }

        TradingInstrumentEnum parent = TradingInstrumentEnum.enumFromDesc(parentDesc);
        for (TradingInstrumentSubTypeEnum subType : values()) {
            if (subType.parentType.equals(parent.name()) && subType.subTypeDescription.equals(subTypeDescription)) {
                return subType.code;
            }
        }
        return UNKNOWN.code;
    }
}


