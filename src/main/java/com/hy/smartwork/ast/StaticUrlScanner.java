package com.hy.smartwork.ast;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author huangying
 */
public class StaticUrlScanner {

    private static final LoginCheckAnalyzer loginAnalyzer = new LoginCheckAnalyzer();

    private static StringBuffer urlStr = new StringBuffer();

    public static void main(String[] args) {
        // 按包扫描，包目录下全部类都需要
        scanControllers("cn.com.vtg.controller.activities", null);
        scanControllers("cn.com.vtg.controller.crm.crmactivity", null);
        scanControllers("cn.com.vtg.controller.events", null);

        // 仅需要指定包目录下的部分类
        Set<Class<?>> controllers = new HashSet<>();
//        controllers.add(UserCouponController.class);
        scanControllers("cn.com.vtg.controller.user", controllers);

        System.out.println(urlStr);
    }

    private static void scanControllers(String basePackage, Set<Class<?>> controllers) {
        try {
            loginAnalyzer.analyzeControllers(basePackage);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Set<URL> packageUrls = getPackageUrls(basePackage);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(packageUrls)
                .setScanners(
                        Scanners.TypesAnnotated, // 扫描类注解
                        Scanners.MethodsAnnotated // 扫描方法注解
                ));

        // 获取所有 Controller 类（支持 @Controller 和 @RestController）
        if (null == controllers) {
            controllers = reflections.getTypesAnnotatedWith(RestController.class);
        }

        if (controllers.isEmpty()) {
            System.err.println("未找到任何 Controller 类，请检查包路径和注解配置！");
            return;
        }
        System.err.println(String.format("扫描 %s 包下面共 %d 个类", basePackage, controllers.size()));

        for (Class<?> clazz : controllers) {
            String classPath = getClassPath(clazz);
            for (Method method : clazz.getDeclaredMethods()) {
                String[] urls = getMethodUrls(method, classPath);
                if (urls.length == 0) {
                    continue;
                }

                // 从缓存中获取登录校验结果
                String className = clazz.getSimpleName();
                String key = className + "-" + method.getName();
                boolean requiresLogin = loginAnalyzer.requiresLogin(key);
                if (requiresLogin) {
                    urlStr.append(String.format("%-20s \t 需要token", urls[0]));
                    urlStr.append("\n");
                } else {
                    urlStr.append(String.format("%-20s \t 可选token", urls[0]));
                    urlStr.append("\n");
                }
            }
        }
    }

    // 获取指定包对应的物理路径（如 com.example.controller → /project/src/main/java/com/example/controller）
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

    private static String getClassPath(Class<?> clazz) {
        RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
        if (classMapping != null && classMapping.value().length > 0) {
            return classMapping.value()[0];
        }
        return "";
    }

    private static void scanMethods(Class<?> clazz, String classPath) {
        for (Method method : clazz.getDeclaredMethods()) {
            processMappingAnnotation(method, GetMapping.class, "GET", classPath);
            processMappingAnnotation(method, PostMapping.class, "POST", classPath);
            processMappingAnnotation(method, PutMapping.class, "PUT", classPath);
            processMappingAnnotation(method, DeleteMapping.class, "DELETE", classPath);
            processMappingAnnotation(method, PatchMapping.class, "PATCH", classPath);
            processMappingAnnotation(method, RequestMapping.class, null, classPath);
        }
    }

    private static void processMappingAnnotation(Method method,
                                                 Class<? extends Annotation> annotationClass,
                                                 String httpMethod,
                                                 String classPath) {
        Annotation annotation = method.getAnnotation(annotationClass);
        if (annotation != null) {
            String[] methodPaths = getAnnotationPaths(annotation);

            for (String path : methodPaths) {
                String fullPath = combinePaths(classPath, path);
//                System.out.printf("%s%n", fullPath);
                urlStr.append(fullPath);
                urlStr.append("\n");
            }
        }
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
    private static String[] getMethodUrls(Method method, String classPath) {
        List<String> urls = new ArrayList<>();
        processMappingAnnotation(method, GetMapping.class, "GET", classPath, urls);
        processMappingAnnotation(method, PostMapping.class, "POST", classPath, urls);
        processMappingAnnotation(method, RequestMapping.class, "", classPath, urls);
        // 其他 HTTP 方法处理...
        return urls.toArray(new String[0]);
    }

    private static void processMappingAnnotation(Method method,
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

    private static String[] getHttpMethods(Annotation annotation) {
        if (annotation instanceof RequestMapping) {
            RequestMapping mapping = (RequestMapping) annotation;
            if (mapping.method().length > 0) {
                return Arrays.stream(mapping.method())
                        .map(Enum::name)
                        .toArray(String[]::new);
            }
            return new String[]{"ANY"};
        }
        return new String[]{"ANY"};
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