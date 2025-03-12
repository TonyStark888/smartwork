package com.hy.smartwork.dify;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author yingh
 */
@Data
public class McMarketData {
    /**
     * 客户问的问题
     */
    @Excel(name = "customer_question", width = 50)
    private String customerQuestion;
    /**
     * 标准问题
     */
    @Excel(name = "standard_question", width = 50)
    private String standardQuestion;

    /**
     * 标准回答
     */
    @Excel(name = "standard_answer", width = 100)
    private String standardAnswer;
}
