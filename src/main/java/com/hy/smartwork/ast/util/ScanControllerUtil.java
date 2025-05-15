package com.hy.smartwork.ast.util;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class ScanControllerUtil {

    /**
     * 扫描指定包下面的类
     *
     * @param basePackage     包路径
     * @param isAllController 是否需要全部类
     * @return Controller类的集合
     */
    public static Set<Class<?>> scanControllers(String basePackage, boolean isAllController) {
        Set<Class<?>> controllers = new HashSet<>();
        Set<URL> packageUrls = getPackageUrls(basePackage);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(packageUrls)
                .setScanners(
                        Scanners.TypesAnnotated, // 扫描类注解
                        Scanners.MethodsAnnotated // 扫描方法注解
                ));

        // 获取所有 Controller 类（支持 @Controller 和 @RestController）
        if (isAllController) {
            controllers.addAll(reflections.getTypesAnnotatedWith(RestController.class));
            controllers.addAll(reflections.getTypesAnnotatedWith(Controller.class));

            if (controllers.isEmpty()) {
                System.err.println("未找到任何 Controller 类，请检查包路径和注解配置！");
                return controllers;
            }
        }
        System.out.printf("扫描 %s 包下面共 %d 个类%n", basePackage, controllers.size());

        return controllers;
    }

    /**
     * 获取指定包对应的物理路径（如 com.example.controller → /project/src/main/java/com/example/controller）
     */
    public static Set<URL> getPackageUrls(String basePackage) {
        String relativePath = basePackage.replace('.', File.separatorChar);
        String projectRoot = System.getProperty("user.dir"); // 项目根目录
        File packageDir = new File(projectRoot + "/target/classes/" + relativePath);

        if (!packageDir.exists()) {
            System.err.println("包目录不存在: " + packageDir.getAbsolutePath());
            return Collections.emptySet();
        }

        try {
            return Collections.singleton(packageDir.toURI().toURL());
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    public static String getClassPath(Class<?> clazz) {
        RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
        if (classMapping != null && classMapping.value().length > 0) {
            return classMapping.value()[0];
        }
        return "";
    }

    private static String[] getAnnotationPaths(Annotation annotation) {
        try {
            Method valueMethod = annotation.annotationType().getMethod("value");
            String[] paths = (String[]) valueMethod.invoke(annotation);
            return (paths.length > 0) ? paths : new String[]{""};
        } catch (Exception e) {
            return new String[]{""};
        }
    }

    // 获取方法的 URL（复用之前的逻辑）
    public static String[] getMethodUrls(Method method, String classPath) {
        List<String> urls = new ArrayList<>();
        processMappingAnnotation(method, GetMapping.class, "GET", classPath, urls);
        processMappingAnnotation(method, PostMapping.class, "POST", classPath, urls);
        processMappingAnnotation(method, RequestMapping.class, "", classPath, urls);
        processMappingAnnotation(method, PutMapping.class, "PUT", classPath, urls);
        processMappingAnnotation(method, DeleteMapping.class, "DELETE", classPath, urls);
        processMappingAnnotation(method, PatchMapping.class, "PATCH", classPath, urls);
        // 其他 HTTP 方法处理...
        return urls.toArray(new String[0]);
    }

    public static void processMappingAnnotation(Method method,
                                                Class<? extends Annotation> annotationClass,
                                                String httpMethod,
                                                String classPath,
                                                List<String> urls) {
        Annotation annotation = method.getAnnotation(annotationClass);
        if (annotation != null) {
            String[] methodPaths = getAnnotationPaths(annotation);
            for (String path : methodPaths) {
                urls.add(combinePaths(classPath, path));
            }
        }
    }

    private static String combinePaths(String path1, String path2) {
        if (path1.endsWith("/") && path2.startsWith("/")) {
            return path1 + path2.substring(1);
        } else if (!path1.endsWith("/") && !path2.startsWith("/") && !path1.isEmpty()) {
            return path1 + "/" + path2;
        } else {
            return path1 + path2;
        }
    }
}
