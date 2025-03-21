package com.valueline.api.base.biying.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MarketValueDTO {
    @JSONField(name = "dm")
    private String code;

    @JSONField(name = "mc")
    private String name;

    /**
     * 总市值，单位万
     */
    @JSONField(name = "zsz")
    private Double marketCapitalization;
}
