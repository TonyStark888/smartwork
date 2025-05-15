package com.hy.smartwork.ast;


import com.hy.smartwork.ast.dto.BaseReq;
import com.hy.smartwork.ast.dto.UrlParamDto;
import com.hy.smartwork.ast.util.ScanControllerUtil;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 扫描所有Controller类，并输出所有public方法的参数，并分析统计参数的种类
 */
public class StaticUrlParameterScanner {

    private static List<UrlParamDto> dtoList = new ArrayList<>();

    private static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) {
        Set<Class<?>> controllers = new HashSet<>();
        // 按包扫描，包目录下全部类都需要
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.activities", true));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.crm.crmactivity", true));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.events", true));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.user", false));
        controllers.addAll(ScanControllerUtil.scanControllers("com.hy.controller.invite", true));

        // 额外处理的类
        // 仅需要指定包目录下的某个类
//        controllers.add(UserCouponController.class);
        // 需要移除的类
//        controllers.remove(TestNewcomerCouponController.class);

        System.out.println("total count: " + count.get());

        parseControllers(controllers);
        // 输出对象的结果
        output();
    }

    /**
     * 解析Controller类
     *
     * @param controllers
     */
    private static void parseControllers(Set<Class<?>> controllers) {
        for (Class<?> clazz : controllers) {
            UrlParamDto dto = null;
            String classPath = ScanControllerUtil.getClassPath(clazz);
            for (Method method : clazz.getDeclaredMethods()) {
                String[] urls = ScanControllerUtil.getMethodUrls(method, classPath);
                if (urls.length == 0) {
                    continue;
                }

                dto = new UrlParamDto();
                dto.setClassName(clazz.getName());
                dto.setMethodName(method.getName());
                dto.setUrl(urls[0]);

                Parameter[] parameters = method.getParameters();
                dto.addParameter(parameters);
                StringBuffer remarkStr = new StringBuffer();
                for (Parameter param : parameters) {
                    Class<?> paramType = param.getType();
                    // 判断方法的ContentType
                    // 检查参数是否有@RequestBody注解
                    Annotation[] annotations = param.getAnnotations();
                    if (annotations.length > 0) {
                        dto.addParamAnnotation(param, annotations);
                    }
                    if (param.isAnnotationPresent(RequestBody.class)) {
                        dto.setContentType("application/json");
                    }

                    boolean isRequest = HttpServletRequest.class.isAssignableFrom(paramType);
                    boolean isBaseReq = BaseReq.class.isAssignableFrom(paramType);

                    if (isRequest) {
                        // 如果出现HttpServletRequest参数，则设置isConcludeRequest为true
                        dto.setConcludeRequest(true);
                        continue;
                    }
                    if (isBaseReq) {
                        // 如果出现BaseReq参数，则设置isConcludeBaseReq为true
                        dto.setConcludeBaseReq(true);
                        continue;
                    }

                    // 不是上述两种类型的参数，额外进行分析处理
                    dto.setConcludeOther(true);

                    boolean isCustomCls = isCustomClass(paramType);
                    if (isCustomCls) {
                        // 如果是自定义的类，探查里面的属性名，有没有叫token的
                        for (Field field : paramType.getDeclaredFields()) {
                            if (field.getName().toLowerCase().contains("token")) {
                                remarkStr.append(param.getType().getName());
                                remarkStr.append(" ");
                                remarkStr.append(param.getName());
                                remarkStr.append(".");
                                remarkStr.append(field.getName());
                                remarkStr.append(", ");
                            }
                        }
                    }

                    // 如果是普通类型，如java.lang.String，带token字样的，更要引起注意
                    if (param.getName().toLowerCase().contains("token")) {
                        remarkStr.append(param.getType().getName());
                        remarkStr.append(" ");
                        remarkStr.append(param.getName());
                        remarkStr.append(", ");
                    }
                }
                if (remarkStr.length() > 0) {
                    remarkStr = remarkStr.delete(remarkStr.length() - 2, remarkStr.length());
                    dto.setRemark(remarkStr.toString());
                    // 设置token字样的标识
                    dto.setConcludeToken(true);
                    // 计数
                    count.getAndIncrement();
                }
                // 添加到集合里
                dtoList.add(dto);
            }

        }
    }


    /**
     * 判断类是否是自定义类（非JDK内置类）
     */
    public static boolean isCustomClass(Class<?> clazz) {
        // 处理数组类型：递归检查组件类型
        if (clazz.isArray()) {
            return isCustomClass(clazz.getComponentType());
        }
        // 基本类型直接视为JDK类
        if (clazz.isPrimitive()) {
            return false;
        }
        // 获取包名
        Package pkg = clazz.getPackage();
        if (pkg == null) {
            return false; // 无法确定包名（如动态生成的类）
        }
        String packageName = pkg.getName();
        // 排除JDK核心包
        return !(packageName.startsWith("java.") || packageName.startsWith("javax."));
    }

    public static void output() {
        StringBuffer info = new StringBuffer();
        // 添加表头
        info.append("类名");
        info.append("\t");
        info.append("方法名");
        info.append("\t");
        info.append("URL");
        info.append("\t");
        info.append("参数列表");
        info.append("\t");
        info.append("是否包含HttpServletRequest类型的请求参数");
        info.append("\t");
        info.append("是否包含BaseReq类型的请求参数");
        info.append("\t");
        info.append("是否包含其它类型的请求参数");
        info.append("\t");
        info.append("其他参数中是否含有token字样的参数");
        info.append("\t");
        info.append("备注");
        info.append("\t\n");

        for (UrlParamDto urlParamDto : dtoList) {
            info.append(urlParamDto.output());
        }

        System.out.println(info.toString());
    }
}