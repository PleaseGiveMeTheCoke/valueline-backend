package com.valueline.api.base.tonghuashun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.valueline.api.base.Constants;
import com.valueline.api.util.OkHttpUtil;
import com.valueline.client.api.base.BaseInfoGetService;
import com.valueline.client.domain.base.BalanceSheet;
import com.valueline.client.domain.base.Industry;
import com.valueline.client.domain.common.Result;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.remoting.http12.rest.Mapping;

/**
 * 同花顺实现类
 */
@Mapping(value = "com.valueline.client.api.base.BaseInfoGetService.tonghuashun")
@DubboService(version = "1.0.0.tonghuashun")
public class BaseInfoGetServiceTonghuashunImpl implements BaseInfoGetService {

    @Override
    public Result<Industry> getIndustryByCode(String code) {
        return Result.fail("no impl");
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

            return obj.toJavaObject(AssetsReport.class);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }    }
}
