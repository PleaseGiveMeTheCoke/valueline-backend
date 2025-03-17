package com.valueline.client.domain.base;

import lombok.Data;

/**
 * 股票市值
 */
@Data
public class MarketValue extends Stock {
    /**
     * 股票当前市值，单位元
     */
    private Double shizhi;
}
