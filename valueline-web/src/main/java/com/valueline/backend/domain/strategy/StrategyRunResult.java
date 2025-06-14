package com.valueline.backend.domain.strategy;

import lombok.Data;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Data
@Table("strategy_run_result")
public class StrategyRunResult {
    @Id
    private Long id;

    @Column(hump = true)
    @Index(unique = false, fields = {})
    private Long strategyId;

    @Column(hump = true)
    private Long xxlJobId;

    @Column(hump = true)
    @Index(unique = false, fields = {})
    private String code;

    @Column(hump = true)
    @ColDefine(type = ColType.TEXT)
    private String output;

    @Column(hump = true)
    private Date gmtCreate;

    @Column(hump = true)
    private Date gmtModified;
}
