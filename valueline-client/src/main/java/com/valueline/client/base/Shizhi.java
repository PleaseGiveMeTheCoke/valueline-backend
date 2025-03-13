package com.valueline.client.base;

import lombok.Data;

/**
 * 股票市值
 */
@Data
public class Shizhi extends Stock {
    /**
     * 股票当前市值，单位元
     */
    private Double shizhi;
}
