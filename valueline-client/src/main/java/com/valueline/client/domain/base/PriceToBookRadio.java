package com.valueline.client.domain.base;

import lombok.Data;

/**
 * 股票市净率
 */
@Data
public class PriceToBookRadio extends Stock {
    /**
     * 股票市净率
     */
    private Double priceToBookRadio;
}
