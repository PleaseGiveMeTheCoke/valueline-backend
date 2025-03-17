package com.valueline.api.base;

import com.valueline.client.api.base.BaseInfoGetService;
import com.valueline.client.domain.base.BalanceSheet;
import com.valueline.client.domain.base.Industry;
import com.valueline.client.domain.common.Result;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class BaseInfoGetServiceImpl implements BaseInfoGetService {
    @Override
    public Result<Industry> getIndustryByCode(String code) {
        return Result.fail("no impl");
    }

    @Override
    public Result<BalanceSheet> getBalanceSheetByCode(String code) {
        return Result.fail("no impl");
    }
}
