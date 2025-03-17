package com.valueline.client.domain.base;

import lombok.Data;

/**
 * 资产负债表
 */
@Data
public class BalanceSheet {
    /**
     * 流动资产
     */
    private Double currentAssets;

    /**
     * 货币资金
     */
    private Double cash;

    /**
     * 交易性金融资产
     */
    private Double tradingFinancialAssets;

    /**
     * 应收票据及应收账款
     */
    private Double receivablesNet;

    /**
     * 应收票据
     */
    private Double notesReceivable;

    /**
     * 应收账款
     */
    private Double accountsReceivable;

    /**
     * 应收款项融资
     */
    private Double financingReceivables;

    /**
     * 预付款项
     */
    private Double prepaidExpenses;

    /**
     * 其他应收款合计
     */
    private Double otherReceivablesTotal;

    /**
     * 其他应收款
     */
    private Double otherReceivables;

    /**
     * 存货
     */
    private Double inventory;

    /**
     * 持有待售资产
     */
    private Double assetsHeldForSale;

    /**
     * 其他流动资产
     */
    private Double otherCurrentAssets;

    /**
     * 总现金
     */
    private Double totalCash;

    /**
     * 流动资产合计
     */
    private Double totalCurrentAssets;

    /**
     * 非流动资产
     */
    private Double nonCurrentAssets;

    /**
     * 长期股权投资
     */
    private Double longTermEquityInvestment;

    /**
     * 其他权益工具投资
     */
    private Double otherEquityInstrumentsInvestment;

    /**
     * 投资性房地产
     */
    private Double investmentProperty;

    /**
     * 固定资产合计
     */
    private Double fixedAssetsTotal;

    /**
     * 固定资产
     */
    private Double fixedAssets;

    /**
     * 固定资产清理
     */
    private Double fixedAssetsClearing;

    /**
     * 在建工程合计
     */
    private Double constructionInProgressTotal;

    /**
     * 在建工程
     */
    private Double constructionInProgress;

    /**
     * 工程物资
     */
    private Double engineeringMaterials;

    /**
     * 使用权资产
     */
    private Double rightOfUseAssets;

    /**
     * 无形资产
     */
    private Double intangibleAssets;

    /**
     * 商誉
     */
    private Double goodwill;

    /**
     * 长期待摊费用
     */
    private Double longTermDeferredExpenses;

    /**
     * 递延所得税资产
     */
    private Double deferredTaxAssets;

    /**
     * 其他非流动资产
     */
    private Double otherNonCurrentAssets;

    /**
     * 非流动资产合计
     */
    private Double nonCurrentAssetsTotal;

    /**
     * 资产总计
     */
    private Double totalAssets;

    /**
     * 流动负债
     */
    private Double currentLiabilities;

    /**
     * 短期借款
     */
    private Double shortTermLoans;

    /**
     * 应付票据及应付账款
     */
    private Double payablesNet;

    /**
     * 应付票据
     */
    private Double notesPayable;

    /**
     * 应付账款
     */
    private Double accountsPayable;

    /**
     * 预收款项
     */
    private Double advancePaymentsReceived;

    /**
     * 合同负债
     */
    private Double contractLiabilities;

    /**
     * 应付职工薪酬
     */
    private Double accruedCompensation;

    /**
     * 应交税费
     */
    private Double taxesPayable;

    /**
     * 其他应付款合计
     */
    private Double otherPayablesTotal;

    /**
     * 应付股利
     */
    private Double dividendsPayable;

    /**
     * 其他应付款
     */
    private Double otherAccountsPayable;

    /**
     * 一年内到期的非流动负债
     */
    private Double currentPortionOfNonCurrentDebt;

    /**
     * 其他流动负债
     */
    private Double otherCurrentLiabilities;

    /**
     * 流动负债合计
     */
    private Double totalCurrentLiabilities;

    /**
     * 非流动负债
     */
    private Double nonCurrentLiabilities;

    /**
     * 长期借款
     */
    private Double longTermLoans;

    /**
     * 租赁负债
     */
    private Double leaseLiabilities;

    /**
     * 长期应付款合计
     */
    private Double longTermPayablesTotal;

    /**
     * 长期应付款
     */
    private Double longTermAccountsPayable;

    /**
     * 递延所得税负债
     */
    private Double deferredTaxLiabilities;

    /**
     * 非流动负债递延收益
     */
    private Double nonCurrentLiabilitiesDeferredIncome;

    /**
     * 非流动负债合计
     */
    private Double totalNonCurrentLiabilities;

    /**
     * 负债合计
     */
    private Double totalLiabilities;

    /**
     * 所有者权益
     */
    private Double equity;

    /**
     * 股本
     */
    private Double shareCapital;

    /**
     * 资本公积
     */
    private Double capitalSurplus;

    /**
     * 减：库存股
     */
    private Double treasuryStock;

    /**
     * 其他综合收益
     */
    private Double otherComprehensiveIncome;

    /**
     * 盈余公积
     */
    private Double surplusReserve;

    /**
     * 未分配利润
     */
    private Double retainedEarnings;

    /**
     * 归属于母公司所有者权益合计
     */
    private Double parentCompanyEquityTotal;

    /**
     * 少数股东权益
     */
    private Double minorityInterest;

    /**
     * 股东权益合计
     */
    private Double totalEquity;

    /**
     * 负债和股东权益总计
     */
    private Double totalLiabilitiesAndEquity;
}
