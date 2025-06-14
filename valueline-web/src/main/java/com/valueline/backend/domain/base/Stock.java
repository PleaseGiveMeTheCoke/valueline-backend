package com.valueline.backend.domain.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 股票
 */
@Data
public class Stock implements Serializable {
    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 股票市净率
     */
    private Double priceToBookRadio;

    /**
     * 市值
     */
    private Double marketValue;

    /**
     * 资产负债率
     */
    private Double debtAssetRatio;
}
