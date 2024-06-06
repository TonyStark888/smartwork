package com.hy.smartwork.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 *  https://www.toolscat.com/db/sql-java
 * 工具网站由SQL生成JavaBean对象时，会带有一个pre标签，不是我想要的，写个快速工具类移除掉
 *
 * 生成的示例及处理完的效果，查看RemovePreTitle.md
 *
 * @author huangying1
 */
public class RemovePreTitle {

    public static void main(String[] args) {
        String str = "/**\n" +
                "     * <pre>\n" +
                "     * 主键\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Long\tpid;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 姓名\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private String\tcustomerName;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 主体类型: 0.个人Individual、1.企业Corporate\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Integer\tentityType;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 主体类型: 0.Resident\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Integer\tcustomerType;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 国籍\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Integer\tcustomerNationality;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 0.身份证\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Integer\tdocumentType;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 身份证ID\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private String\tdocumentNumber;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 创建人\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Long\tcreator;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 修改人\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Long\tupdater;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 创建时间\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Long\tcreateTime;\n" +
                "\n" +
                "    /**\n" +
                "     * <pre>\n" +
                "     * 修改时间\n" +
                "     * </pre>\n" +
                "     */\n" +
                "    private Long\tupdateTime;";


        try (BufferedReader reader = new BufferedReader(new StringReader(str))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 在这里处理每一行的内容，比如打印到控制台
                if(line.contains("<pre>") || line.contains("</pre>")) {
                    continue;
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
