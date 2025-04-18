package com.valueline.client.api.profit;

import com.valueline.client.domain.common.Result;

public interface ProfitInfoGetService {

    /**
     * 获取近n年的平均ROE
     *
     * @param code
     * @param year 若year＝3，则获取近3年的平均ROE
     * @return
     */
    Result<Double> getAverageROE(String code, int year);
}
