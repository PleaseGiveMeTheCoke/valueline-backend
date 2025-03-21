package com.valueline.api.base.biying.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DebtAssetRatioDTO {
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
     * 资产负债率 百分数.
     */
    @JSONField(name = "zcfzl")
    private Double zichanFuzhaiLv;
}
