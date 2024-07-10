package com.hy.smartwork.jvm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Atomic变量自增运算测试
 */
public class AtomicDemo {

    public static AtomicInteger race = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {

//        System.out.println(race);
//        System.out.println(race.incrementAndGet());
//        System.out.println(race);
//        System.out.println(race.incrementAndGet());
//        System.out.println(race);

        // 初始化对象，设置初始值为0，初始版本号为1
        AtomicStampedReference<Integer> reference = new AtomicStampedReference<>(0, 1);
        // 设置原子更新操作，值由0更新为1，版本号由1更新为2，更新操作成功
        reference.compareAndSet(0, 1, 1, 2);
        // 模拟过期的更新操作，此次更新不会成功
        reference.compareAndSet(0, 2, 1, 3);
        // 结果：值为1，版本号为2
        System.out.println(reference.getReference());
        System.out.println(reference.getStamp());

        StringBuffer sb = new StringBuffer();
        sb.append("aaa");

        StringBuilder sbd = new StringBuilder();
        sbd.append("bbb");
    }
}