package com.valueline.api.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 发送GET请求并返回JSONObject
     *
     * @param url        请求的URL
     * @param retryTimes 重试次数
     * @return JSONObject
     * @throws IOException 如果请求失败
     */
    public static String getRequest(String url, int retryTimes) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        IOException exception = null;
        for (int i = 0; i < retryTimes; i++) {
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return Objects.requireNonNull(response.body()).string();
                }
            } catch (IOException e) {
                exception = e;
                // 重试之前可以等待一段时间
                try {
                    Thread.sleep(5000); // 等待5秒后重试
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (exception != null) {
            throw exception;
        } else {
            throw new IOException("Request failed after " + retryTimes + " retries");
        }
    }
}
