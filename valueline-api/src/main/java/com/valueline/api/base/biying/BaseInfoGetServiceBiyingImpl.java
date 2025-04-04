package com.valueline.api.base.biying;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.valueline.api.base.Constants;
import com.valueline.api.util.OkHttpUtil;
import com.valueline.client.api.base.BaseInfoGetService;
import com.valueline.client.domain.base.BalanceSheet;
import com.valueline.client.domain.base.Industry;
import com.valueline.client.domain.common.Result;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.remoting.http12.rest.Mapping;

/**
 * 必盈实现类
 */
@Mapping(value = "com.valueline.client.api.base.BaseInfoGetService.biying")
@DubboService(version = "1.0.0.biying")
public class BaseInfoGetServiceBiyingImpl implements BaseInfoGetService {

    /**
     * http://localhost:20880/com.valueline.client.api.base.BaseInfoGetService.biying/getIndustryByCode?code=000419
     *
     * @param code
     * @return
     */
    @Override
    public Result<Industry> getIndustryByCode(String code) {
        try {
            String url = Constants.industryUrl + code + Constants.split + Constants.token;
            String responseString = OkHttpUtil.getRequest(url, 5);
            JSONArray responseArray = JSONArray.parseArray(responseString);
            Industry ret = new Industry();
            ret.setFirstClass(responseArray.getJSONObject(0).getString("name"));
            ret.setSecondClass(responseArray.getJSONObject(1).getString("name"));
            String jsonString = JSON.toJSONString(responseArray);
            if (jsonString.contains("创业板")) {
                ret.setBoard("cyb");
            } else if (jsonString.contains("科创板")) {
                ret.setBoard("kcb");
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
        return Result.fail("no impl");
    }
}
