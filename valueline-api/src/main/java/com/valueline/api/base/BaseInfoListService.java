package com.valueline.api.base;

import com.valueline.client.base.Fuzhai;
import com.valueline.client.base.Shijing;
import com.valueline.client.base.Shizhi;

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
    List<Shizhi> listShizhi();

    /**
     * 获取股票市净率列表
     *
     * @return
     */
    List<Shijing> listShijing();

    /**
     * 获取股票资产负债率
     *
     * @return
     */
    List<Fuzhai> listFuzhai();
}
