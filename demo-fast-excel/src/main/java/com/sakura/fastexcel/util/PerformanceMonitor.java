package com.sakura.fastexcel.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能监控工具类
 * 用于记录Excel导入过程中的性能指标
 */
@Slf4j
public class PerformanceMonitor {

    private static final ConcurrentHashMap<String, Long> startTimes = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();

    /**
     * 开始计时
     *
     * @param taskName 任务名称
     */
    public static void start(String taskName) {
        startTimes.put(taskName, System.currentTimeMillis());
        counters.putIfAbsent(taskName, new AtomicLong(0));
        log.info("任务[{}]开始执行", taskName);
    }

    /**
     * 增加计数
     *
     * @param taskName 任务名称
     * @param count    增加的数量
     */
    public static void incrementCount(String taskName, long count) {
        counters.computeIfAbsent(taskName, k -> new AtomicLong(0)).addAndGet(count);
    }

    /**
     * 结束计时并输出性能指标
     *
     * @param taskName 任务名称
     */
    public static void end(String taskName) {
        Long startTime = startTimes.get(taskName);
        if (startTime == null) {
            log.warn("任务[{}]未找到开始时间", taskName);
            return;
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long count = counters.getOrDefault(taskName, new AtomicLong(0)).get();

        double recordsPerSecond = count > 0 ? (count * 1000.0 / duration) : 0;

        log.info("任务[{}]执行完成, 处理记录数: {}, 耗时: {}ms, 平均处理速度: {}/秒",
                taskName, count, duration, String.format("%.2f", recordsPerSecond));

        // 清理资源
        startTimes.remove(taskName);
    }
}