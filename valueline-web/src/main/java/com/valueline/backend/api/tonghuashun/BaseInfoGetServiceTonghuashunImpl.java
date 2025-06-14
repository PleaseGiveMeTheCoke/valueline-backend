package com.valueline.backend.api.tonghuashun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.valueline.backend.api.Constants;
import com.valueline.backend.service.BaseInfoGetService;
import com.valueline.backend.api.util.OkHttpUtil;
import com.valueline.backend.domain.base.BalanceSheet;
import com.valueline.backend.domain.base.Industry;
import com.valueline.backend.domain.common.Result;
import org.springframework.stereotype.Component;

/**
 * 同花顺实现类
 */
@Component
public class BaseInfoGetServiceTonghuashunImpl implements BaseInfoGetService {

    @Override
    public Result<Industry> getIndustryByCode(String code) {
        try {
            String url = String.format(Constants.industryUrlThs, code);
            String responseString = OkHttpUtil.getRequest(url, 5);
            JSONObject responseObj = JSONArray.parseObject(responseString);
            Industry ret = new Industry();
            ret.setFirstClass(responseObj.getJSONObject("data").getJSONObject("industry").getString("hy2"));
            ret.setSecondClass(responseObj.getJSONObject("data").getJSONObject("industry").getString("hy3"));
            url = String.format(Constants.boardUrlThs, code);
            responseString = OkHttpUtil.getRequest(url, 5);
            if (responseString.contains("科创")) {
                ret.setBoard("kcb");
            } else if (responseString.contains("创")) {
                ret.setBoard("cyb");
            } else {
                // 主板
                ret.setBoard("zb");
            }
            return Result.success(ret);
        } catch (Throwable e) {
            // TODO log
            return Result.fail("system error: " + e.getMessage());
        }
    }

    @Override
    public Result<BalanceSheet> getBalanceSheetByCode(String code) {
        try {
            // period = 0，所有报告
            // period = 1，所有一季报
            // period = 2，所有中报
            // period = 3，所有三季报
            // period = 4，所有年报
            String host = String.format(Constants.balanceSheetUrl, code);
            String responseString = OkHttpUtil.getRequest(host, 3);
            JSONObject jsonObject = JSONObject.parseObject(responseString);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray category = data.getJSONArray("category");
            JSONArray list = ((JSONObject) data.getJSONArray("finance").get(0)).getJSONArray("list");
            JSONObject obj = new JSONObject();
            for (int i = 0; i < category.size(); i++) {
                JSONObject categoryObj = (JSONObject) category.get(i);
                JSONObject listObj = (JSONObject) list.get(i);
                obj.put(categoryObj.getString("id"), listObj.getString("value") + listObj.getString("unit"));
            }

            for (String key : obj.keySet()) {
                String moneyString = obj.getString(key);
                obj.put(key, convertToDouble(moneyString));
            }

            return Result.success(obj.toJavaObject(BalanceSheet.class));
        } catch (Throwable e) {
            return Result.fail("system error: " + e.getMessage());
        }
    }

    private double convertToDouble(String input) {
        if (input == null || input.isEmpty()) {
            return 0.0;
        }

        // 去除空格
        input = input.trim();

        // 定义单位和对应的乘数
        double multiplier = 1.0;
        if (input.endsWith("万亿")) {
            multiplier = 1000000000000.0;
            input = input.substring(0, input.length() - 2).trim();
        } else if (input.endsWith("亿")) {
            multiplier = 100000000.0;
            input = input.substring(0, input.length() - 1).trim();
        } else if (input.endsWith("万")) {
            multiplier = 10000.0;
            input = input.substring(0, input.length() - 1).trim();
        } else if (input.endsWith("元")) {
            input = input.substring(0, input.length() - 1).trim();
        }

        // 将数字部分转换为double
        double number = Double.parseDouble(input);

        // 返回最终结果
        return number * multiplier;
    }
}
