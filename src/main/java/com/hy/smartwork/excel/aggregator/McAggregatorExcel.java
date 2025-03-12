package com.hy.smartwork.excel.aggregator;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.hy.smartwork.excel.aggregator.dto.MarketSourceSymbol;
import com.hy.smartwork.excel.aggregator.dto.McRuleMapping;
import com.hy.smartwork.excel.aggregator.dto.McRuleMappingItem;
import com.hy.smartwork.excel.aggregator.dto.McSymbol;
import com.hy.smartwork.excel.aggregator.enums.TradingInstrumentEnum;
import com.hy.smartwork.excel.aggregator.enums.TradingInstrumentSubTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用EasyExcel工具读取Excel文件拼接成SQL
 *
 * @author yingh
 */
@Slf4j
public class McAggregatorExcel {

    /**
     * 行情源数据初始化
     */
    public static Map<String, Integer> marketSourceMap = new HashMap<String, Integer>();
    /**
     * market_source_symbol表，记录lp_symbol和id的关系
     * key值策略：lp_code + "_" + market_source_symbol "_" + symbol_sub_type
     */
    public static Map<String, Long> lpSymbolMap = new HashMap<String, Long>();
    /**
     * mc_symbol表，记录mc_symbol和id的关系
     */
    public static Map<String, Long> mcSymbolMap = new HashMap<String, Long>();
    /**
     * mc_rule_mapping表，记录mc_symbol和id的关系，用于填充mc_rule_mapping_item表的mapping_id字段
     */
    public static Map<String, Long> mcRuleMappingMap = new HashMap<String, Long>();


    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\yingh\\Desktop\\基础数据配置表.xlsx");

        String fileName = file.getName();
        //验证图片格式
        if (!fileName.isEmpty()) {
            int index = fileName.lastIndexOf(".");
            if (index > 0) {
                String extension = fileName.substring(index + 1).toLowerCase();
                if ("xls".equals(extension) || "xlsx".equals(extension)) {
                    //正常文件
                }
            }

            // 初始化配置数据
            init();

            // 创建导入参数对象
            ImportParams params = new ImportParams();
            // 设置表头行数，通常表头占一行
            params.setHeadRows(1);
            // 文件中第1行为字段注释行，跳过此行(rows从0开始计数)
            params.setStartRows(1);

            String marketSourceSymbolSql = buildMarketSourceSymbolSQL(params, file);

            String mcSymbolSql = buildMcSymbolSQL(params, file);

            String ruleMappingSql = buildMcRuleMappingSQL(params, file);

            String ruleMappingItemSql = buildMcRuleMappingItemSQL(params, file);

            StringBuffer finalSql = new StringBuffer();
            finalSql.append("\n\n");
            finalSql.append(marketSourceSymbolSql);
            finalSql.append("\n\n");
            finalSql.append(mcSymbolSql);
            finalSql.append("\n\n");
            finalSql.append(ruleMappingSql);
            finalSql.append("\n\n");
            finalSql.append(ruleMappingItemSql);
            finalSql.append("\n\n");

            System.out.println(finalSql.toString());
        }
    }

    /**
     * 生成market_source_symbol表的生成SQL
     * 需要记录下lp_symbol对应的ID
     *
     * @param params
     * @param file
     * @return
     */
    private static String buildMarketSourceSymbolSQL(ImportParams params, File file) {
        // 设置页签索引
        params.setStartSheetIndex(0);

        StringBuffer sql = new StringBuffer();
        sql.append("\n# 行情源品种\n");
        sql.append("INSERT INTO `market_source_symbol`(`id`,`lp_symbol`, `market_source_id`, `lp_code`, `symbol_type`, `symbol_sub_type`, `exchange`, `currency`, `status`, `current_status`) VALUES \n");

        try {
            // 使用 Easypoi 读取文件
            List<MarketSourceSymbol> userList = ExcelImportUtil.importExcel(file, MarketSourceSymbol.class, params);
            // 遍历读取到的数据，生成SQL
            // 拼接的SQL格式：(1, 'GC2502', 1, 'FUTU', 2, 2, '', '', 1, 1),
            for (MarketSourceSymbol msSymbol : userList) {
                // 先做一轮替换操作
                if (StringUtils.isBlank(msSymbol.getExchange())) {
                    msSymbol.setExchange("");
                }
                if (StringUtils.isBlank(msSymbol.getCurrency())) {
                    msSymbol.setCurrency("");
                }

                lpSymbolMap.put(convertLpCode(msSymbol.getLpCode()) + "_" + msSymbol.getLpSymbol() + "_" + TradingInstrumentSubTypeEnum.fromDesc(msSymbol.getSymbolType(), msSymbol.getSymbolSubType()), msSymbol.getId());

                // 开始拼接SQL
                sql.append("(");
                sql.append(msSymbol.getId());
                sql.append(", '");
                sql.append(msSymbol.getLpSymbol());
                sql.append("', ");
                sql.append(getMarketSourceId(msSymbol.getLpCode()));
                sql.append(", '");
                sql.append(convertLpCode(msSymbol.getLpCode()));
                sql.append("', ");
                sql.append(TradingInstrumentEnum.fromDesc(msSymbol.getSymbolType()));
                sql.append(", ");
                sql.append(TradingInstrumentSubTypeEnum.fromDesc(msSymbol.getSymbolType(), msSymbol.getSymbolSubType()));
                sql.append(", '");
                sql.append(msSymbol.getExchange());
                sql.append("', '");
                sql.append(msSymbol.getCurrency());
                sql.append("', ");
                sql.append(msSymbol.getStatus());
                sql.append(", ");
                sql.append(msSymbol.getCurrentStatus());
                sql.append("),\n");
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(";\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql.toString();
    }

    /**
     * 生成market_source_symbol表的生成SQL
     * 需要记录下mc_symbol对应的ID
     *
     * @param params
     * @param file
     * @return
     */
    private static String buildMcSymbolSQL(ImportParams params, File file) {
        // 设置页签索引
        params.setStartSheetIndex(1);

        StringBuffer sql = new StringBuffer();
        sql.append("\n# 平台交易品种\n");
        sql.append("INSERT INTO `mc_symbol`(`id`,`mc_symbol`, `symbol_type`, `symbol_sub_type`, `status`, `base_currency`, `pricing_currency`, `pricing_degree`, `qty_degree`, `spread_degree`) VALUES \n");

        try {
            // 使用 Easypoi 读取文件
            List<McSymbol> userList = ExcelImportUtil.importExcel(file, McSymbol.class, params);
            // 遍历读取到的数据，生成SQL
            // 拼接的SQL格式：(1, 'MC-GC', 2, 2, 1, 'USD', 'USD', 1, 1, 1),
            for (McSymbol mcSymbol : userList) {
                // 先做一轮替换操作
                mcSymbolMap.put(mcSymbol.getMcSymbol(), mcSymbol.getId());

                // 开始拼接SQL
                sql.append("(");
                sql.append(mcSymbol.getId());
                sql.append(", '");
                sql.append(mcSymbol.getMcSymbol());
                sql.append("', ");
                sql.append(TradingInstrumentEnum.fromDesc(mcSymbol.getSymbolType()));
                sql.append(", ");
                sql.append(TradingInstrumentSubTypeEnum.fromDesc(mcSymbol.getSymbolType(), mcSymbol.getSymbolSubType()));
                sql.append(", ");
                sql.append(mcSymbol.getStatus());
                sql.append(", '");
                sql.append(mcSymbol.getBaseCurrency());
                sql.append("', '");
                sql.append(mcSymbol.getPricingCurrency());
                sql.append("', ");
                sql.append(mcSymbol.getPricingDegree());
                sql.append(", ");
                sql.append(mcSymbol.getQtyDegree());
                sql.append(", ");
                sql.append(mcSymbol.getSpreadDegree());
                sql.append("),\n");
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(";");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql.toString();
    }

    /**
     * 生成market_source_symbol表的生成SQL
     *
     * @param params
     * @param file
     * @return
     */
    private static String buildMcRuleMappingSQL(ImportParams params, File file) {
        // 设置页签索引
        params.setStartSheetIndex(2);

        StringBuffer sql = new StringBuffer();
        sql.append("\n# 平台交易品种规则映射\n");
        sql.append("INSERT INTO `mc_rule_mapping`(`id`, `symbol_id`, `mc_symbol`, `aggregator_type`, `spread_min`, `spread_max`, `latency_threshold`, `spread_threshold`, `volatility_threshold`, `status`, `current_status`) VALUES \n");

        try {
            // 使用 Easypoi 读取文件
            List<McRuleMapping> userList = ExcelImportUtil.importExcel(file, McRuleMapping.class, params);
            // 遍历读取到的数据，生成SQL
            // 拼接的SQL格式：(1, 1, 'MC-GC', 2, 2, 10, 500, 500, 1, 1),
            for (McRuleMapping mapping : userList) {
                // 先做一轮替换操作
                mcRuleMappingMap.put(mapping.getMcSymbol(), mapping.getId());

                // 开始拼接SQL
                sql.append("(");
                sql.append(mapping.getId());
                sql.append(", ");
                // symbol_id字段由mc_symbol表记录的map对象中获取
                sql.append(mcSymbolMap.get(mapping.getMcSymbol()));
                sql.append(", '");
                sql.append(mapping.getMcSymbol());
                sql.append("', ");
                sql.append(convertAggregateType(mapping.getAggregatorType()));
                sql.append(", ");
                sql.append(mapping.getSpreadMin());
                sql.append(", ");
                sql.append(mapping.getSpreadMax());
                sql.append(", ");
                sql.append(mapping.getLatencyThreshold());
                sql.append(", ");
                sql.append(mapping.getSpreadThreshold());
                sql.append(", ");
                sql.append(mapping.getVolatilityThreshold());
                sql.append(", ");
                sql.append(mapping.getStatus());
                sql.append(", ");
                sql.append(mapping.getCurrentStatus());
                sql.append("),\n");
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(";");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql.toString();
    }

    /**
     * 生成market_source_symbol表的生成SQL
     *
     * @param params
     * @param file
     * @return
     */
    private static String buildMcRuleMappingItemSQL(ImportParams params, File file) {
        // 设置页签索引
        params.setStartSheetIndex(3);

        StringBuffer sql = new StringBuffer();
        sql.append("\n# 平台交易品种映射明细\n");
        sql.append("INSERT INTO `mc_rule_mapping_item`(`mapping_id`, `market_source_id`, `lp_code`, `market_source_symbol_id`, `market_source_symbol`, `symbol_type`, `symbol_sub_type`, `weight`) VALUES \n");

        try {
            // 使用 Easypoi 读取文件
            List<McRuleMappingItem> userList = ExcelImportUtil.importExcel(file, McRuleMappingItem.class, params);
            // 遍历读取到的数据，生成SQL
            // 拼接的SQL格式：(1, 1, 'MC-GC', 2, 2, 10, 500, 500, 1, 1),
            for (McRuleMappingItem item : userList) {
                // 先做一轮替换操作

                // 开始拼接SQL
                sql.append("(");
                sql.append(mcRuleMappingMap.get(item.getMcSymbol()));
                sql.append(", ");
                sql.append(getMarketSourceId(item.getLpCode()));
                sql.append(", '");
                sql.append(convertLpCode(item.getLpCode()));
                sql.append("', ");
                sql.append(lpSymbolMap.get(convertLpCode(item.getLpCode()) + "_" + item.getMarketSourceSymbol() + "_" + TradingInstrumentSubTypeEnum.fromDesc(item.getSymbolType(), item.getSymbolSubType())));
                sql.append(", '");
                sql.append(item.getMarketSourceSymbol());
                sql.append("', ");
                sql.append(TradingInstrumentEnum.fromDesc(item.getSymbolType()));
                sql.append(", ");
                sql.append(TradingInstrumentSubTypeEnum.fromDesc(item.getSymbolType(), item.getSymbolSubType()));
                sql.append(", ");
                sql.append(item.getWeight());
                sql.append("),\n");
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(";");

        } catch (Exception e) {
            log.error("parse sql error: ", e);
        }
        return sql.toString();
    }

    private static Integer getMarketSourceId(String lpCode) {
        // 数据清洗，如大小写
        lpCode = convertLpCode(lpCode);
        return marketSourceMap.get(lpCode);
    }

    /**
     * 行情源LP_CODE字段，目前产品有多种写法，统一转换成大写、统一的编码
     *
     * @param lpCode
     * @return
     */
    private static String convertLpCode(String lpCode) {
        if ("Futu-COMEX".equalsIgnoreCase(lpCode)) {
            lpCode = "Futu";
        }
        if ("直达".equalsIgnoreCase(lpCode)) {
            lpCode = "DA";
        }
        return lpCode.toUpperCase();
    }

    /**
     * 聚合机制的转换关系
     *
     * @param aggregatorType
     * @return
     */
    private static Integer convertAggregateType(String aggregatorType) {
        if ("优先级".equalsIgnoreCase(aggregatorType)) {
            return 2;
        }
        if ("深度合并".equalsIgnoreCase(aggregatorType)) {
            return 1;
        }
        return 0;
    }

    /**
     * 由于行情源数据较少，直接配置在初始化的Map对象里
     */
    private static void init() {
        // 行情源的ID配置
        marketSourceMap.put("FUTU", 1);
        marketSourceMap.put("DA", 2);
        marketSourceMap.put("LMAX", 3);
        marketSourceMap.put("FINALTO", 4);
        marketSourceMap.put("BINANCE", 5);
        marketSourceMap.put("OKX", 6);
        marketSourceMap.put("BYBIT", 7);
    }
}
