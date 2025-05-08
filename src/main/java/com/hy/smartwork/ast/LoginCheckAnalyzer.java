package com.hy.smartwork.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangying
 */
public class LoginCheckAnalyzer {

    // 缓存分析结果：方法名 -> 是否需要登录
    private final Map<String, Boolean> methodLoginMap = new HashMap<>();

    /**
     * 分析指定包下的所有 Controller 类
     */
    public void analyzeControllers(String basePackage) throws FileNotFoundException {
        Path sourcePath = Paths.get("src/main/java", basePackage.replace(".", "/"));
        File sourceDir = sourcePath.toFile();

        if (!sourceDir.exists()) {
            throw new FileNotFoundException("源码目录不存在: " + sourceDir.getAbsolutePath());
        }

        // 遍历所有 Java 文件
        for (File file : sourceDir.listFiles((dir, name) -> name.endsWith(".java"))) {
            CompilationUnit cu = StaticJavaParser.parse(file);
            cu.accept(new ControllerVisitor(), null);
        }
    }

    /**
     * 判断方法是否需要登录
     */
    public boolean requiresLogin(String methodName) {
        return methodLoginMap.getOrDefault(methodName, false);
    }

    // 自定义 Visitor：解析 Controller 类和方法
    private class ControllerVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
            // 仅处理 Controller 类
            if (cid.isAnnotationPresent("Controller") || cid.isAnnotationPresent("RestController")) {
                super.visit(cid, arg);
            }
        }

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            // 检测方法是否包含 Token 校验逻辑
            if (!md.isPublic()) {
                return;
            }

            boolean requiresLogin = checkTokenValidation(md);
            // 获取类名，类名-方法名为key值，防止重复
            String className = md.getParentNode()
                    .filter(node -> node instanceof ClassOrInterfaceDeclaration)
                    .map(node -> (ClassOrInterfaceDeclaration) node)
                    .map(ClassOrInterfaceDeclaration::getNameAsString)
                    .orElse("Unknown");
            String key = className + "-" + md.getNameAsString();
            if (null != methodLoginMap.get(key)) {
                System.out.printf("repeat method name %s%n", key);
            }
            methodLoginMap.put(key, requiresLogin);
        }
    }

    /**
     * 检测方法是否包含 Token 校验逻辑
     */
    private boolean checkTokenValidation(MethodDeclaration md) {
        TokenCheckVisitor visitor = new TokenCheckVisitor();
        md.accept(visitor, null);
        return visitor.hasTokenCheck();
    }

    // 自定义 Visitor：检测 Token 相关代码
    private static class TokenCheckVisitor extends VoidVisitorAdapter<Void> {
        private boolean hasTokenCheck = false;

        boolean hasTokenCheck() {
            return hasTokenCheck;
        }

        @Override
        public void visit(MethodCallExpr n, Void arg) {
            // 检测是否调用了 messageResourceUtil.getMessage("10100050")
            if (n.getNameAsString().equals("getMessage")
                    && n.getScope().isPresent()
                    && n.getScope().get().toString().equals("messageResourceUtil")
                    && n.getArgument(0).toString().contains("\"10100050\"")) {
                hasTokenCheck = true;
            }
            super.visit(n, arg);
        }

        @Override
        public void visit(NameExpr n, Void arg) {
            // 检测是否引用了 Constant.TOKEN_IS_NULL或Constant.TOKEN_INVALID
            if ("Constant.TOKEN_IS_NULL".equalsIgnoreCase(n.getParentNode().get().toString()) || "Constant.TOKEN_INVALID".equalsIgnoreCase(n.getParentNode().get().toString())) {
                hasTokenCheck = true;
            }
            super.visit(n, arg);
        }
    }
}