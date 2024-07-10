package com.hy.smartwork.jvm;

public class KlassInfoDemo {

    public int ini() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
            return x;
        }
    }
}
