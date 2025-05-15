package com.hy.smartwork.ast.dto;

import lombok.Data;

/**
 * 分析URL的访问权限，主要指对token的要求
 * 分为3种场景：必需token，可选token，不需要token
 * 如果是白名单的配置，包含可选token和不需要token两类
 *
 * @author golden
 */
@Data
public class UrlTokenDto {

    private String url;
    /**
     * 0 必需token
     * 1 可选token
     * 2 不需要token
     */
    private Integer tokenType;

    /**
     * 普通的输出，用于粘贴到对外输出的表格里
     *
     * @return
     */
    public String output() {
        StringBuffer output = new StringBuffer();
        output.append(url);
        output.append("\t");
        if (0 == tokenType) {
            output.append("必需token");
            output.append("\t");
            output.append("否");
        } else if (1 == tokenType) {
            output.append("可选token");
            output.append("\t");
            output.append("是");
        } else {
            output.append("不需要token");
            output.append("\t");
            output.append("是");
        }
        return output.toString();
    }

    /**
     * 输出给程序对比使用，只输出可选和不需要token的URL（白名单）
     * 并且格式可以直接粘贴到String[]里
     *
     * @return
     */
    public String outputForProgram() {
        if (0 == tokenType) {
            return "";
        }

        StringBuffer output = new StringBuffer();
        output.append("\"");
        output.append(url);
        output.append("\",");
        return output.toString();
    }

    /**
     * 输出给配置使用，只输出可选和不需要token的URL（白名单）
     * 带/vau前缀
     *
     * @return
     */
    public String outputForYml() {
        if (0 == tokenType) {
            return "";
        }

        StringBuffer output = new StringBuffer();
        output.append("/vau");
        output.append(url);
        return output.toString();
    }
}
