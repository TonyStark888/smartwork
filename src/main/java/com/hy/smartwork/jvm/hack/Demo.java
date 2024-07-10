package com.hy.smartwork.jvm.hack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Demo {

    public static void main(String[] args) {
        try {
            InputStream is = new FileInputStream("d://TestClass.class");
            byte[] b = new byte[is.available()];
            is.read(b);
            is.close();

            System.out.println(JavaclassExecuter.execute(b));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
