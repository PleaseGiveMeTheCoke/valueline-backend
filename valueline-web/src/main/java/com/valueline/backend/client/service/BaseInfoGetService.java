package com.valueline.backend.client.service;


import com.valueline.backend.client.domain.base.BalanceSheet;
import com.valueline.backend.client.domain.base.Industry;
import com.valueline.backend.client.domain.common.Result;

/**
 * 提供根据code，获取股票基础信息相关的服务
 */
public interface BaseInfoGetService {
    /**
     * 根据股票code获取行业信息
     *
     * @param code
     * @return
     */
    Result<Industry> getIndustryByCode(String code);

    /**
     * 根据股票code获取资产负债表信息
     *
     * @param code
     * @return
     */
    Result<BalanceSheet> getBalanceSheetByCode(String code);
}
