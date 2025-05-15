package com.hy.smartwork.ast.dto;

import lombok.Data;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller类的URL入口方法参数解析
 */
@Data
public class UrlParamDto implements Serializable {

    /**
     * Controller类名
     */
    private String className;

    /**
     * Controller类方法名
     */
    private String methodName;

    /**
     * 方法对应的URL
     */
    private String url;

    /**
     * 参数列表，包含参数类型和参数名
     * 按顺序展现即可
     * 把注解带上
     */
    private Map<String, Class<?>> paramMap = new LinkedHashMap<>();

    /**
     * 参数列表，包含参数类型和参数名
     * 按顺序展现即可
     * 把注解带上
     */
    private Map<String, Annotation[]> annotationMap = new LinkedHashMap<>();
    /**
     * 是否包含HttpServletRequest类型的请求参数
     */
    private boolean isConcludeRequest;

    /**
     * 是否包含cn.com.vtg.model.event.req.BaseReq类型的请求参数
     */
    private boolean isConcludeBaseReq;

    /**
     * 是否包含其它类型的请求参数
     */
    private boolean isConcludeOther;

    /**
     * 在其他参数中是否含有token字样的参数
     * 或是包装类属性中包含token字样
     */
    private boolean isConcludeToken;

    /**
     * 备注，主要记录哪些字段包含token字样的参数
     */
    private String remark;

    /**
     * 接口请求方法的Content-Type值
     * 根据@RequestBody注解来判断，默认为空
     */
    private String contentType;

    public void addParameter(Parameter[] parameters) {
        if (null != parameters) {
            for (Parameter p : parameters) {
                paramMap.put(p.getName(), p.getType());
            }
        }
    }

    public void addParamAnnotation(Parameter parameter, Annotation[] annotations) {
        annotationMap.put(parameter.getName(), annotations);
    }

    public Map<String, Class<?>> getClassMap() {
        return paramMap;
    }

    public String getParamMap() {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, Class<?>> entry : paramMap.entrySet()) {
            paramStr.append(getAnnotation(entry.getKey()));
            paramStr.append(entry.getValue().getSimpleName()).append(" ").append(entry.getKey()).append(", ");
        }
        if (paramStr.length() > 0) {
            paramStr = paramStr.delete(paramStr.length() - 2, paramStr.length());
        }
        return paramStr.toString();
    }

    public String getAnnotation(String name) {
        StringBuilder annoStr = new StringBuilder();
        Annotation[] annotations = annotationMap.get(name);
        if (null != annotations) {
            for (Annotation anno : annotations) {
                annoStr.append("@");
                annoStr.append(anno.annotationType().getSimpleName());
                annoStr.append(" ");
            }
        }
        return annoStr.toString();
    }

    public String output() {
        StringBuffer info = new StringBuffer();
        info.append(getClassName());
        info.append("\t");
        info.append(getMethodName());
        info.append("\t");
        info.append(getUrl());
        info.append("\t");
        info.append(getParamMap());
        info.append("\t");
        info.append(isConcludeRequest());
        info.append("\t");
        info.append(isConcludeBaseReq());
        info.append("\t");
        info.append(isConcludeOther());
        info.append("\t");
        info.append(isConcludeToken());
        info.append("\t");
        info.append(getRemark());
        info.append("\t\n");
        return info.toString();
    }
}
