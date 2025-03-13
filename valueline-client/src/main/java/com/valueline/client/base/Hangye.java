package com.valueline.client.base;

import lombok.Data;

@Data
public class Hangye extends Stock {
    /**
     * 一级行业
     */
    private String firstClass;

    /**
     * 二级行业
     */
    private String secondClass;
}
