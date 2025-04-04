package com.valueline.web.wrapper;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class XxlAdminWrapper {

    @Value("${xxl.job.admin.cookie}")
    private String cookie;

    @Value("${xxl.job.admin.address}")
    private String adminAddress;

    public String trigger(Long id, String executorParam) {
        OkHttpClient client = new OkHttpClient();

        // Create form-data body
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("executorParam", executorParam)
                .add("addressList", "")
                .build();

        // Build request with headers
        Request request = new Request.Builder()
                .url(adminAddress)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cookie", cookie)
                .build();

         try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException("Failed to trigger job", e);
        }
    }
}
