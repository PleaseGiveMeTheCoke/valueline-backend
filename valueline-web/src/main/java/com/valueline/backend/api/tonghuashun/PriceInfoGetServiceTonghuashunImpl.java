package com.valueline.backend.api.tonghuashun;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.valueline.backend.api.Constants;
import com.valueline.backend.service.PriceInfoGetService;
import com.valueline.backend.api.util.OkHttpUtil;
import com.valueline.backend.domain.common.Result;
import com.valueline.backend.domain.price.LowestPoint;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class PriceInfoGetServiceTonghuashunImpl implements PriceInfoGetService {
    @Override
    public Result<Double> getPbPercentile(String code, int year) {
        return Result.fail("no impl");
    }

    @Override
    public Result<List<LowestPoint>> getLowestPointGrowthRate(String code, String months) {
        try {
            JSONArray monthArray = JSONArray.parseArray(months);

            // 3. 获取市净率数据
            String url = String.format(Constants.priceUrlThs, code);
            String response = OkHttpUtil.getRequest(url, 5);
            JSONObject responseObj = JSON.parseObject(response);

            // 5. 解析数据
            JSONObject data = responseObj.getJSONObject("data");
            JSONObject latest = data.getJSONObject("latest");
            JSONArray trendList = data.getJSONArray("trend_list");

            double currentValue = Double.parseDouble(latest.getString("value"));
            List<LowestPoint> result = new ArrayList<>();

            // 6. 计算每个月份区间的最低点
            for (int i = 0; i < monthArray.size(); i++) {
                int month = monthArray.getInteger(i);
                if (month <= 0) {
                    continue; // 跳过无效月份
                }

                // 计算该月份区间内的最低点
                LowestValueInfo lowestInfo = calculateLowestValueAndDate(trendList, month);
                double increase = (currentValue - lowestInfo.value) / lowestInfo.value;

                LowestPoint point = new LowestPoint();
                point.setLowestPrice(lowestInfo.value);
                point.setCurrentPrice(currentValue);
                point.setIncrease(increase);
                point.setMonth(month);
                point.setLowestDate(lowestInfo.date);
                result.add(point);
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.fail("获取最低点涨幅失败: " + e.getMessage());
        }
    }

    private LowestValueInfo calculateLowestValueAndDate(JSONArray trendList, int month) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(month);

        double lowestValue = Double.MAX_VALUE;
        String lowestDate = "";

        for (int i = 0; i < trendList.size(); i++) {
            JSONObject item = trendList.getJSONObject(i);
            String dateStr = item.getString("date");

            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);

            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                double value = Double.parseDouble(item.getString("value"));
                if (value < lowestValue) {
                    lowestValue = value;
                    lowestDate = dateStr;
                }
            }
        }

        // 如果没有找到数据，返回默认值
        if (lowestValue == Double.MAX_VALUE) {
            return new LowestValueInfo(0, "");
        }
        return new LowestValueInfo(lowestValue, lowestDate);
    }

    // 用于保存最低点值和日期的内部类
    private static class LowestValueInfo {
        double value;
        String date;

        LowestValueInfo(double value, String date) {
            this.value = value;
            this.date = date;
        }
    }
}
