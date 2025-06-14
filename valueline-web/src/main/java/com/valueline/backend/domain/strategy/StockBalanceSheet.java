package com.valueline.backend.domain.strategy;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Data
@Table("stock_balance_sheet")
public class StockBalanceSheet {
    /**
     * 主键ID
     */
    @Id
    private Long id;

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

    @Column(hump = true)
    private String year;

    @Column(hump = true)
    private String season;

    /**
     * @see StockBalanceSheet
     */
    @Column(hump = true)
    private String balanceSheet;

    /**
     * 清算价值
     */
    @Column(hump = true)
    private Double netValue;

    /**
     * 计算过程
     */
    @Column(hump = true)
    private String calculateProcess;

    @Column(hump = true)
    private Date gmtCreate;

    @Column(hump = true)
    private Date gmtModified;
}
