package com.valueline.api.base.tonghuashun;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.valueline.api.base.Constants;
import com.valueline.api.util.OkHttpUtil;
import com.valueline.client.api.price.PriceInfoGetService;
import com.valueline.client.domain.common.Result;
import com.valueline.client.domain.price.LowestPoint;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.remoting.http12.rest.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapping(value = "com.valueline.client.api.price.PriceInfoGetService.biying")
@DubboService(version = "1.0.0.biying")
public class PriceInfoGetServiceBiyingImpl implements PriceInfoGetService {
    @Override
    public Result<Double> getPbPercentile(String code, int year) {
        return Result.fail("no impl");
    }

    @Override
    public Result<List<LowestPoint>> getLowestPointGrowthRate(String code, String months) {
        try {
            String host = String.format(Constants.monthKUrl, code);
            String responseString = OkHttpUtil.getRequest(host, 3);
            JSONArray kArray = JSONArray.parseArray(responseString);
            int size = kArray.size();
            List<LowestPoint> ret = new ArrayList<>();
            List<Integer> monthList = JSON.parseArray(months, Integer.class);
            for (int month : monthList) {
                Double min = Double.MAX_VALUE;
                for (int i = 1; i <= month; i++) {
                    JSONObject mk = kArray.getJSONObject(size - i);
                    min = Math.min(min, mk.getDouble("l"));
                }

                Double cur = kArray.getJSONObject(size - 1).getDouble("c");
                Double increase = ((cur - min) / min);
                LowestPoint lowestPoint = new LowestPoint();
                lowestPoint.setLowestPrice(min);
                lowestPoint.setCurrentPrice(cur);
                lowestPoint.setIncrease(increase);
                lowestPoint.setMonth(month);
                ret.add(lowestPoint);
            }
            return Result.success(ret);
        } catch (Throwable e) {
            return Result.fail("system error: " + e.getMessage());
        }
    }
}
