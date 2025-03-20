package com.valueline.client.domain.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 资产负债表
 */
@Data
public class BalanceSheet {
    /**
     * 流动资产
     */
    @JSONField(name = "流动资产")
    private Double currentAssets;

    /**
     * 货币资金
     */
    @JSONField(name = "cash")
    private Double cash;

    /**
     * 交易性金融资产
     */
    @JSONField(name = "trade_financial_assets")
    private Double tradingFinancialAssets;

    /**
     * 应收票据及应收账款
     */
    @JSONField(name = "receivable_notes_and_accounts")
    private Double receivablesNet;

    /**
     * 应收票据
     */
    @JSONField(name = "notes_receivable")
    private Double notesReceivable;

    /**
     * 应收账款
     */
    @JSONField(name = "accounts_receivable")
    private Double accountsReceivable;

    /**
     * 应收款项融资
     */
    @JSONField(name = "receivable_financing")
    private Double financingReceivables;

    /**
     * 预付款项
     */
    @JSONField(name = "payment_money")
    private Double prepaidExpenses;

    /**
     * 其他应收款合计
     */
    @JSONField(name = "other_receivable_total")
    private Double otherReceivablesTotal;

    /**
     * 其他应收款
     */
    @JSONField(name = "other_receivable")
    private Double otherReceivables;

    /**
     * 存货
     */
    @JSONField(name = "inventory")
    private Double inventory;

    /**
     * 持有待售资产
     */
    @JSONField(name = "hold_sale_assets")
    private Double assetsHeldForSale;

    /**
     * 其他流动资产
     */
    @JSONField(name = "other_current_assets")
    private Double otherCurrentAssets;

    /**
     * 总现金
     */
    @JSONField(name = "total_cash")
    private Double totalCash;

    /**
     * 流动资产合计
     */
    @JSONField(name = "total_current_assets")
    private Double totalCurrentAssets;

    /**
     * 非流动资产
     */
    @JSONField(name = "非流动资产")
    private Double nonCurrentAssets;

    /**
     * 债权投资
     */
    @JSONField(name = "debt_investment")
    private Double debtInvestment;

    /**
     * 长期股权投资
     */
    @JSONField(name = "long_term_equity_investment")
    private Double longTermEquityInvestment;

    /**
     * 其他权益工具投资
     */
    @JSONField(name = "other_equity_tools_investment")
    private Double otherEquityInstrumentsInvestment;

    /**
     * 投资性房地产
     */
    @JSONField(name = "investment_property")
    private Double investmentProperty;

    /**
     * 固定资产合计
     */
    @JSONField(name = "fixed_assets_total")
    private Double fixedAssetsTotal;

    /**
     * 固定资产
     */
    @JSONField(name = "fixed_assets")
    private Double fixedAssets;

    /**
     * 固定资产清理
     */
    @JSONField(name = "fixed_assets_clear")
    private Double fixedAssetsClearing;

    /**
     * 在建工程合计
     */
    @JSONField(name = "construction_process_total")
    private Double constructionInProgressTotal;

    /**
     * 在建工程
     */
    @JSONField(name = "construction_in_process")
    private Double constructionInProgress;

    /**
     * 工程物资
     */
    @JSONField(name = "engineering_materials")
    private Double engineeringMaterials;

    /**
     * 使用权资产
     */
    @JSONField(name = "right_use_assets")
    private Double rightOfUseAssets;

    /**
     * 无形资产
     */
    @JSONField(name = "intangible_assets")
    private Double intangibleAssets;

    /**
     * 商誉
     */
    @JSONField(name = "goodwill")
    private Double goodwill;

    /**
     * 长期待摊费用
     */
    @JSONField(name = "long_term_deferred_expenses")
    private Double longTermDeferredExpenses;

    /**
     * 递延所得税资产
     */
    @JSONField(name = "deferred_tax_assets")
    private Double deferredTaxAssets;

    /**
     * 其他非流动资产
     */
    @JSONField(name = "other_non_current_nets")
    private Double otherNonCurrentAssets;

    /**
     * 非流动资产合计
     */
    @JSONField(name = "non_current_nets_total")
    private Double nonCurrentAssetsTotal;

    /**
     * 资产总计
     */
    @JSONField(name = "assets_total")
    private Double totalAssets;

    /**
     * 流动负债
     */
    @JSONField(name = "流动负债")
    private Double currentLiabilities;

    /**
     * 短期借款
     */
    @JSONField(name = "short_term_loans")
    private Double shortTermLoans;

    /**
     * 应付票据及应付账款
     */
    @JSONField(name = "payable_notes_and_accounts")
    private Double payablesNet;

    /**
     * 应付票据
     */
    @JSONField(name = "notes_payable")
    private Double notesPayable;

    /**
     * 应付账款
     */
    @JSONField(name = "accounts_payable")
    private Double accountsPayable;

    /**
     * 预收款项
     */
    @JSONField(name = "collect_money")
    private Double advancePaymentsReceived;

    /**
     * 合同负债
     */
    @JSONField(name = "contract_debt")
    private Double contractLiabilities;

    /**
     * 应付职工薪酬
     */
    @JSONField(name = "accrued_wages")
    private Double accruedCompensation;

    /**
     * 应交税费
     */
    @JSONField(name = "taxes_dues")
    private Double taxesPayable;

    /**
     * 其他应付款合计
     */
    @JSONField(name = "other_payable_total")
    private Double otherPayablesTotal;

    /**
     * 应付股利
     */
    @JSONField(name = "dividends_payable")
    private Double dividendsPayable;

    /**
     * 其他应付款
     */
    @JSONField(name = "other_accounts_payable")
    private Double otherAccountsPayable;

    /**
     * 一年内到期的非流动负债
     */
    @JSONField(name = "year_non_current_debt")
    private Double currentPortionOfNonCurrentDebt;

    /**
     * 其他流动负债
     */
    @JSONField(name = "other_current_debt")
    private Double otherCurrentLiabilities;

    /**
     * 流动负债合计
     */
    @JSONField(name = "current_total_debt")
    private Double totalCurrentLiabilities;

    /**
     * 非流动负债
     */
    @JSONField(name = "非流动负债")
    private Double nonCurrentLiabilities;

    /**
     * 长期借款
     */
    @JSONField(name = "long_term_loan")
    private Double longTermLoans;

    /**
     * 租赁负债
     */
    @JSONField(name = "lease_debt")
    private Double leaseLiabilities;

    /**
     * 长期应付款合计
     */
    @JSONField(name = "long_term_payable_total")
    private Double longTermPayablesTotal;

    /**
     * 长期应付款
     */
    @JSONField(name = "long_term_accounts_payable")
    private Double longTermAccountsPayable;

    /**
     * 递延所得税负债
     */
    @JSONField(name = "deferred_tax_debt")
    private Double deferredTaxLiabilities;

    /**
     * 非流动负债递延收益
     */
    @JSONField(name = "non_current_debt_deferred_income")
    private Double nonCurrentLiabilitiesDeferredIncome;

    /**
     * 非流动负债合计
     */
    @JSONField(name = "non_current_debt_total")
    private Double totalNonCurrentLiabilities;

    /**
     * 负债合计
     */
    @JSONField(name = "total_debt")
    private Double totalLiabilities;

    /**
     * 所有者权益
     */
    @JSONField(name = "所有者权益")
    private Double ownerEquity;

    /**
     * 股本
     */
    @JSONField(name = "equity")
    private Double shareCapital;

    /**
     * 资本公积
     */
    @JSONField(name = "capital_reserve")
    private Double capitalSurplus;

    /**
     * 减：库存股
     */
    @JSONField(name = "treasury_stock")
    private Double treasuryStock;

    /**
     * 其他综合收益
     */
    @JSONField(name = "other_comprehensive_income")
    private Double otherComprehensiveIncome;

    /**
     * 盈余公积
     */
    @JSONField(name = "surplus_reserve")
    private Double surplusReserve;

    /**
     * 未分配利润
     */
    @JSONField(name = "undistributed_profits")
    private Double retainedEarnings;

    /**
     * 归属于母公司所有者权益合计
     */
    @JSONField(name = "parent_holder_equity_total")
    private Double parentCompanyEquityTotal;

    /**
     * 少数股东权益
     */
    @JSONField(name = "minority_equity")
    private Double minorityInterest;

    /**
     * 股东权益合计
     */
    @JSONField(name = "holder_equity_total")
    private Double totalEquity;

    /**
     * 负债和股东权益总计
     */
    @JSONField(name = "debt_and_equity_total")
    private Double totalLiabilitiesAndEquity;
}
