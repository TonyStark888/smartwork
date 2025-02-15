package com.hy.smartwork.excel.aggregator.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yingh
 */
@Data
public class McRuleMappingItem {

        private static final long serialVersionUID = 1L;

        /**
         * 主键ID
         */
        @Excel(name = "主键ID")
        private Long id;

        /**
         * 规则映射主键ID
         */
        @Excel(name = "规则映射主键ID")
        private Long mappingId;

        /**
         * 交易品种代码
         */
        @Excel(name = "交易品种代码")
        private String mcSymbol;

        /**
         * 行情源ID
         */
        @Excel(name = "行情源ID")
        private Long marketSourceId;

        /**
         * 交易品种类型-枚举值
         * 0：类型1
         * 1：类型2
         */
        @Excel(name = "交易品种类型")
        private String symbolType;

        /**
         * 交易品种子类型-枚举值
         * 0：子类型1
         * 1：子类型2
         */
        @Excel(name = "交易品种子类型")
        private String symbolSubType;

        /**
         * 行情源品种ID
         */
        @Excel(name = "行情源品种ID")
        private Long marketSourceSymbolId;

        /**
         * 行情源品种名
         */
        @Excel(name = "行情源品种编码")
        private String marketSourceSymbol;

        /**
         * 权重
         */
        @Excel(name = "权重")
        private Integer weight;

        /**
         * LP编码
         * 对应表中的 lp_code 字段
         */
        @Excel(name = "LP编码")
        private String lpCode;
}
