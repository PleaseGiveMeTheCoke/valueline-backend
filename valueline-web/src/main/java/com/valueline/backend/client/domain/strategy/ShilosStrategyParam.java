package com.valueline.backend.client.domain.strategy;

import lombok.Data;

import java.util.Map;

@Data
public class ShilosStrategyParam {
    /**
     * 市净率最大值
     */
    private Double priceToBookRadio;

    /**
     * 资产负债率最大值
     */
    private Double debtAssetRatio;

    /**
     * 净资产计算公式
     */
    private String valueExpression;

    /**
     * 格雷厄姆指标最大值
     */
    private Double greamRatio;

    /**
     * 距最低点涨幅的最大值
     */
    private Map<Integer, Double> maxIncrease;

    /**
     * 选用的财报基准年，如2024
     */
    private String year;

    /**
     * 选用的财报基准季，如1，2，3，4
     */
    private String season;
}
