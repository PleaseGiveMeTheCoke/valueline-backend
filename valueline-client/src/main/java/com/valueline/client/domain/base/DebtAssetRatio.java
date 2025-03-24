package com.valueline.client.domain.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 股票资产负债率
 */
@Data
public class DebtAssetRatio extends Stock implements Serializable {
    /**
     * 股票资产负债率
     */
    private Double debtAssetRatio;
}
