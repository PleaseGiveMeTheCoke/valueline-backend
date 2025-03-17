package com.valueline.client.domain.base;

import lombok.Data;

/**
 * 股票资产负债率
 */
@Data
public class DebtAssetRatio extends Stock {
    /**
     * 股票资产负债率
     */
    private Double debtAssetRatio;
}
