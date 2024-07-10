package com.hy.smartwork.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class RandomDemo {

    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(30,20);
        map.put(40,20);
        map.put(50,20);
        map.put(60,20);
        map.put(70,20);

        Integer[] scoreArray = null;
        if(null == scoreArray) {
            // 初始化权重数组
            scoreArray = new Integer[100];
            Iterator<Map.Entry<Integer, Integer>> ite = map.entrySet().iterator();
            int index = 0;
            while(ite.hasNext()) {
                Map.Entry<Integer, Integer> entry = ite.next();
                for (int i=0;i<entry.getValue(); i++) {
                    scoreArray[index] = entry.getKey();
                    index++;
                }
            }
        }
        System.out.println(scoreArray[new Random().nextInt(100)]);
        System.out.println(scoreArray[new Random().nextInt(100)]);
        System.out.println(scoreArray[new Random().nextInt(100)]);
        System.out.println(scoreArray[new Random().nextInt(100)]);
    }
}
