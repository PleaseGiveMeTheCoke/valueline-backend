package com.valueline.client.domain.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 股票市净率
 */
@Data
public class PriceToBookRadio extends Stock implements Serializable {
    /**
     * 股票市净率
     */
    private Double priceToBookRadio;
}
