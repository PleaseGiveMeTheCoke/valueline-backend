package com.valueline.client.base;

import lombok.Data;

/**
 * 股票
 */
@Data
public class Stock {
    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;
}
