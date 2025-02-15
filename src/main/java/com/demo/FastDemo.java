package com.demo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author yingh
 */
public class FastDemo {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 120; i++) {
            // 示例时间戳（毫秒）
            long timestampMs = System.currentTimeMillis();
            // 将毫秒时间戳转换为 Instant 对象
            Instant instant = Instant.ofEpochMilli(timestampMs);

            // 将 Instant 对象转换为指定时区（这里以东八区为例）的 LocalDateTime 对象
            LocalDateTime localDateTime = instant.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();

            // 提取分钟数
            int minutes = localDateTime.getMinute();

            System.out.println("时间戳 " + localDateTime.toString() + " 对应的分钟数是: " + minutes + " 直接除以的结果：" + timestampMs / 1000 / 60);

            Thread.sleep(3000);
        }

    }
}
