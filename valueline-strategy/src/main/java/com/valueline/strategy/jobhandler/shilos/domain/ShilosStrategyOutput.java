package com.valueline.strategy.jobhandler.shilos.domain;

import com.valueline.client.domain.price.LowestPoint;
import lombok.Data;

import java.util.List;

@Data
public class ShilosStrategyOutput {
    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 格雷厄姆指标
     */
    private Double greamRatio;

    /**
     * 市净率
     */
    private Double priceToBookRadio;

    /**
     * 资产负债率
     */
    private Double debtAssetRatio;

    /**
     * 距最低点涨幅
     */
    private List<LowestPoint> maxIncrease;

    /**
     * 申万一级行业
     */
    private String firstIndustry;

    /**
     * 申万二级行业
     */
    private String secondIndustry;

    /**
     * 板块， 如上证，深证，创业板，科创板
     */
    private String board;
}
