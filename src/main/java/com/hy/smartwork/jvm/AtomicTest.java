package com.hy.smartwork.jvm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomic变量自增运算测试
 */
public class AtomicTest {
    public static volatile int raceV1 = 0;

    public static void increaseV1() {
        raceV1++;
    }


    public static AtomicInteger race = new AtomicInteger(0);

    public static void increase() {
        race.incrementAndGet();
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        increase();
                        increaseV1();
                    }
                }
            });
            threads[i].start();
        }
        while (Thread.activeCount() > 1)
            Thread.yield();
        System.out.println(race);
        System.out.println(raceV1);
    }
}