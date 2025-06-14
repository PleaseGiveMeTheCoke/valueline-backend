package com.valueline.backend.domain.strategy;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Data
@Table("shilos_strategy_instance")
public class ShilosStrategyInstance {
    @Id
    private Long id;

    @Column(hump = true)
    private Long strategyId;

    /**
     * @see StrategyStatus#name()
     */
    @Column(hump = true)
    private String status;

    @Column(hump = true)
    private Date gmtCreate;

    @Column(hump = true)
    private Date gmtModified;

    @Column(hump = true)
    private String runResult;
}
