package com.valueline.backend.domain.strategy;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;
import java.util.Map;

@Data
@Table("shilos_strategy")
public class ShilosStrategy {
    @Id
    private Long id;

    @Column(hump = true)
    private String name;

    /**
     * 市净率最大值
     */
    @Column(hump = true)
    private Double priceToBookRadio;

    /**
     * 资产负债率最大值
     */
    @Column(hump = true)
    private Double debtAssetRatio;

    /**
     * 格雷厄姆指标最小值
     */
    @Column(hump = true)
    private Double greamRatio;

    /**
     * 距最低点涨幅的最大值, Map< Integer,String >
     */
    @Column(hump = true)
    private String maxIncrease;

    /**
     * 选用的财报基准年，如2024
     */
    @Column(hump = true)
    private String year;

    /**
     * 选用的财报基准季，如1，2，3，4
     */
    @Column(hump = true)
    private String season;

    @Column(hump = true)
    private Date gmtCreate;

    @Column(hump = true)
    private Date gmtModified;
}
