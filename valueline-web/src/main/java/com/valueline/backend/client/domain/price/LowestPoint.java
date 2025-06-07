package com.valueline.backend.client.domain.price;

import lombok.Data;

import java.io.Serializable;

@Data
public class LowestPoint implements Serializable {
    /**
     * 最低点价格
     */
    private Double lowestPrice;

    /**
     * 当前价格
     */
    private Double currentPrice;

    /**
     * 距最低点的涨幅，如0.5,代表 50%
     */
    private Double increase;

    /**
     * 代表month月内的涨幅
     */
    private int month;

    /**
     * 最低点日期 (格式: yyyyMMdd)
     */
    private String lowestDate;
}
