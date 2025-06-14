package com.valueline.backend.domain.strategy;

import lombok.Data;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Data
// @Table("strategy")
@Deprecated
public class Strategy {
    @Id
    private Long id;

    @Column(hump = true)
    private String strategyType;

    @Column(hump = true)
    private Long xxlJobId;

    @Column(hump = true)
    private String name;

    @Column(hump = true)
    @ColDefine(type = ColType.TEXT)
    private String strategyParam;

    @Column(hump = true)
    private String cron;

    @Column(hump = true)
    private String status;

    @Column(hump = true)
    private Date gmtCreate;

    @Column(hump = true)
    private Date gmtModified;
}
