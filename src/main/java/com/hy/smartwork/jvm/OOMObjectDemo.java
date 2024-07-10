package com.hy.smartwork.jvm;

import java.util.ArrayList;
import java.util.List;

public class OOMObjectDemo {

    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i=0; i<num; i++) {
            Thread.sleep(100);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws InterruptedException {
        // 如果直接打断点，JConsole会连不进来，先休眠10秒钟，有足够的时候完成JConsole的连接
        Thread.sleep(10000);
        fillHeap(1000);
    }
}
