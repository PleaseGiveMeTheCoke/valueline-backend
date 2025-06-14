package com.valueline.backend.service;

import com.valueline.backend.domain.common.Result;
import com.valueline.backend.domain.price.LowestPoint;

import java.util.List;

/**
 * 获取股票价格相关信息
 */
public interface PriceInfoGetService {

    /**
     * 获取市净率分位
     *
     * @param code 股票代码
     * @param year 1，3，5年的市净率分为
     * @return
     */
    Result<Double> getPbPercentile(String code, int year);

    /**
     * 获取最低点涨幅
     *
     * @param code  股票代码
     * @param months 月，如3个月内，最低点的涨幅。批量, jsonArray
     * @return
     */
    Result<List<LowestPoint>> getLowestPointGrowthRate(String code, String months);
}
