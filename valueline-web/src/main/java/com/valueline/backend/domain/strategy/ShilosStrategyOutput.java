package com.valueline.backend.domain.strategy;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Table("shilos_strategy_output")
@Data
public class ShilosStrategyOutput {
    /**
     * 主键ID
     */
    @Id
    private Long id;

    /**
     * 策略实例ID
     */
    @Column(hump = true)
    private Long strategyInstanceId;

    /**
     * 股票代码
     */
    @Column(hump = true)
    private String code;

    /**
     * 股票名称
     */
    @Column(hump = true)
    private String name;

    /**
     * 格雷厄姆指标
     */
    @Column(hump = true)
    private Double greamRatio;

    /**
     * 市净率
     */
    @Column(hump = true)
    private Double priceToBookRadio;

    /**
     * 资产负债率
     */
    @Column(hump = true)
    private Double debtAssetRatio;

    /**
     * 距最低点涨幅 List<LowestPoint>
     */
    @Column(hump = true)
    private String maxIncrease;

    /**
     * 申万一级行业
     */
    @Column(hump = true)
    private String firstIndustry;

    /**
     * 申万二级行业
     */
    @Column(hump = true)
    private String secondIndustry;

    /**
     * 板块， 如上证，深证，创业板，科创板
     */
    @Column(hump = true)
    private String board;

    @Column(hump = true)
    private Date gmtCreate;

    @Column(hump = true)
    private Date gmtModified;
}
