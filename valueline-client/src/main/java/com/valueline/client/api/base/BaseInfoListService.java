package com.valueline.client.api.base;

import com.valueline.client.domain.base.Stock;
import com.valueline.client.domain.base.StockCondition;
import com.valueline.client.domain.common.Result;

import java.util.List;

/**
 * 提供股票基础信息列表相关的服务
 */
public interface BaseInfoListService {
    /**
     * 筛选股票列表
     *
     * @return
     */
    Result<List<Stock>> listByCondition(StockCondition stockCondition);
}
