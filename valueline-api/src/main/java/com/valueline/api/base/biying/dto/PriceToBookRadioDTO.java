package com.valueline.api.base.biying.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PriceToBookRadioDTO {
    /**
     * 股票代码.
     */
    @JSONField(name = "dm")
    private String code;

    /**
     * 股票名称.
     */
    @JSONField(name = "mc")
    private String name;

    /**
     * 市净率.
     */
    @JSONField(name = "sjl")
    private Double shijing;
}
