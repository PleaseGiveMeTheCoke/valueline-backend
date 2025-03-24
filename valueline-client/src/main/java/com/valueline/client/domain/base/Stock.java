package com.valueline.client.domain.base;

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
}
