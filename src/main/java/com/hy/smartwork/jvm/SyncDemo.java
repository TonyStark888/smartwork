package com.hy.smartwork.jvm;

public class SyncDemo {

    public synchronized void testSync() {
        System.out.println("this is a synchronized method");
    }

    public void nonSync() {
        System.out.println("this is a method");
    }

    public void syncCode(Object obj) {
        synchronized (obj) {
            System.out.println("this is a synchronized code");
        }
    }

    public static void main(String[] args) {
        SyncDemo demo = new SyncDemo();
        demo.testSync();
        demo.nonSync();
    }
}
