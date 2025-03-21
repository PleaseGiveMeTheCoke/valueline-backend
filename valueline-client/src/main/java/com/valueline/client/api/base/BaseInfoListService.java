package com.valueline.client.api.base;

import com.valueline.client.domain.base.DebtAssetRatio;
import com.valueline.client.domain.base.PriceToBookRadio;
import com.valueline.client.domain.base.MarketValue;
import com.valueline.client.domain.common.Result;

import java.util.List;

/**
 * 提供股票基础信息列表相关的服务
 */
public interface BaseInfoListService {
    /**
     * 获取股票市值列表
     *
     * @return
     */
    Result<List<MarketValue>> listMarketValue();

    /**
     * 获取股票市净率列表
     *
     * @return
     */
    Result<List<PriceToBookRadio>> listPriceToBookRadio();

    /**
     * 获取股票资产负债率
     *
     * @return
     */
    Result<List<DebtAssetRatio>> listDebtAssetRatio(String year, String season);
}
