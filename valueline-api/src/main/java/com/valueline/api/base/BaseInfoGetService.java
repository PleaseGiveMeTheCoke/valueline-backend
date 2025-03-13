package com.valueline.api.base;

import com.valueline.client.base.Hangye;

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
    Hangye getHangyeByCode(String code);
}
