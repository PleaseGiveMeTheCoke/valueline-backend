package com.valueline.client.domain.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 股票市值
 */
@Data
public class MarketValue extends Stock implements Serializable {
    /**
     * 股票当前市值，单位元
     */
    private Double marketValue;
}
