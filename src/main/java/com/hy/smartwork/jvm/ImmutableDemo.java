package com.hy.smartwork.jvm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImmutableDemo {
    public static void main(String[] args) {
        // 不可变设计
        Integer i = Integer.valueOf(12);
        Long l = Long.valueOf(12);
        // BigInteger的不可变
        BigInteger bi = new BigInteger("1314520998998688574384553");
        System.out.println(bi.intValue());

        // BigDecimal的不可变
        BigDecimal bd = new BigDecimal("13143.99");
        System.out.println(bd.intValue());
        System.out.println(bd.doubleValue());

        // 可变设计
        AtomicInteger ai = new AtomicInteger(13);
        AtomicLong al = new AtomicLong(18);

        ai.compareAndSet(1,2);
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.get(0);
        vector.size();

//        System.setIn();
//        System.setOut();
//        System.runFinalizersOnExit(false);
    }
}
