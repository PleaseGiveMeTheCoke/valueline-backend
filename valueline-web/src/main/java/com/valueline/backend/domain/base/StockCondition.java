package com.valueline.backend.domain.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockCondition implements Serializable {

    /**
     * 股票市净率
     */
    private Double priceToBookRadio;

    /**
     * 资产负债率
     */
    private Double debtAssetRatio;

    /**
     * 资产负债率所属年
     */
    private String year;

    /**
     * 资产负债率所属季度
     */
    private String season;

    /**
     * 市净率日期。格式20250403
     */
    private String day;
}
