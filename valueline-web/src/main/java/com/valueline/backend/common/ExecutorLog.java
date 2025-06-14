package com.valueline.backend.common;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExecutorLog {
    // 使用线程安全的集合（适用于多线程场景）
    private final List<String> logs = new CopyOnWriteArrayList<>();

    /**
     * 记录一行日志，支持 {} 占位符
     */
    public void log(String format, Object... args) {
        String formatted = MessageFormat.format(format, args);
        System.out.println(format);
        logs.add(formatted);
    }

    /**
     * 收集所有日志，按行拼接成一个字符串返回
     */
    public String collect() {
        StringBuilder sb = new StringBuilder();
        for (String line : logs) {
            sb.append(line).append("\n");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : ""; // 去掉最后一个换行符
    }

    /**
     * 清空已记录的日志
     */
    public void clear() {
        logs.clear();
    }
}