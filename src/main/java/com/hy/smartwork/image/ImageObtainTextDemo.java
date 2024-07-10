package com.hy.smartwork.image;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.File;
import java.io.FileFilter;

/**
 * @author hy
 */
public class ImageObtainTextDemo {

    public static void main(String[] args) {
        // 初始化Tesseract OCR引擎
        Tesseract tesseract = new Tesseract();
        try {
            tesseract.setLanguage("chi_sim");
            String dirPath = "D://image";
            File dir = new File(dirPath);
            FileFilter fileFilter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return false;
                    }
                    if (pathname.getName().toLowerCase().endsWith("png")) {
                        return true;
                    }
                    return false;
                }
            };
            File[] files = dir.listFiles(fileFilter);
            if(null != files && files.length > 0) {
                for (File file: files) {
                    System.out.println(extraText(tesseract, file));
                }
            }

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String extraText(Tesseract tesseract, File imageFile) throws TesseractException {
        // 读取图片并进行文字识别
        // 设置读取的边框
        // 创建一个Rectangle对象，定义感兴趣的区域（以像素为单位）
        // 截图上的文字宽度示例，仅有一例截图，不代表所有
//            Rectangle rect = new Rectangle(25, 120, 720, 1200);
        Rectangle rect = new Rectangle(25, 200, 720, 1100);
        String text = tesseract.doOCR(imageFile, rect);

        // 提取到的文字内容进行清洗，去掉多余的空格
        text = text.replaceAll(" ", "");
        return text;
    }
}
