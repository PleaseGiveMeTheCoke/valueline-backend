package com.valueline.client.domain.base;

import lombok.Data;

@Data
public class Industry extends Stock {
    /**
     * 一级行业
     */
    private String firstClass;

    /**
     * 二级行业
     */
    private String secondClass;
}
