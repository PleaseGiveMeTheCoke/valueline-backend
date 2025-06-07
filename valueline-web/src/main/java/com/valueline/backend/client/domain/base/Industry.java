package com.valueline.backend.client.domain.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class Industry extends Stock implements Serializable {
    /**
     * 一级行业
     */
    private String firstClass;

    /**
     * 二级行业
     */
    private String secondClass;

    /**
     * 所属板块，zb，cyb，kcb
     */
    private String board;
}
