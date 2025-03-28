package com.valueline.client.domain.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产负债表
 */
@Data
public class BalanceSheet implements Serializable {
    /**
     * 流动资产
     */
    @JSONField(name = "流动资产")
    private double currentAssets;

    /**
     * 货币资金
     */
    @JSONField(name = "cash")
    private double cash;

    /**
     * 交易性金融资产
     */
    @JSONField(name = "trade_financial_assets")
    private double tradingFinancialAssets;

    /**
     * 应收票据及应收账款
     */
    @JSONField(name = "receivable_notes_and_accounts")
    private double receivablesNet;

    /**
     * 应收票据
     */
    @JSONField(name = "notes_receivable")
    private double notesReceivable;

    /**
     * 应收账款
     */
    @JSONField(name = "accounts_receivable")
    private double accountsReceivable;

    /**
     * 应收款项融资
     */
    @JSONField(name = "receivable_financing")
    private double financingReceivables;

    /**
     * 预付款项
     */
    @JSONField(name = "payment_money")
    private double prepaidExpenses;

    /**
     * 其他应收款合计
     */
    @JSONField(name = "other_receivable_total")
    private double otherReceivablesTotal;

    /**
     * 其他应收款
     */
    @JSONField(name = "other_receivable")
    private double otherReceivables;

    /**
     * 存货
     */
    @JSONField(name = "inventory")
    private double inventory;

    /**
     * 持有待售资产
     */
    @JSONField(name = "hold_sale_assets")
    private double assetsHeldForSale;

    /**
     * 其他流动资产
     */
    @JSONField(name = "other_current_assets")
    private double otherCurrentAssets;

    /**
     * 总现金
     */
    @JSONField(name = "total_cash")
    private double totalCash;

    /**
     * 流动资产合计
     */
    @JSONField(name = "total_current_assets")
    private double totalCurrentAssets;

    /**
     * 非流动资产
     */
    @JSONField(name = "非流动资产")
    private double nonCurrentAssets;

    /**
     * 债权投资
     */
    @JSONField(name = "debt_investment")
    private double debtInvestment;

    /**
     * 长期股权投资
     */
    @JSONField(name = "long_term_equity_investment")
    private double longTermEquityInvestment;

    /**
     * 其他权益工具投资
     */
    @JSONField(name = "other_equity_tools_investment")
    private double otherEquityInstrumentsInvestment;

    /**
     * 投资性房地产
     */
    @JSONField(name = "investment_property")
    private double investmentProperty;

    /**
     * 固定资产合计
     */
    @JSONField(name = "fixed_assets_total")
    private double fixedAssetsTotal;

    /**
     * 固定资产
     */
    @JSONField(name = "fixed_assets")
    private double fixedAssets;

    /**
     * 固定资产清理
     */
    @JSONField(name = "fixed_assets_clear")
    private double fixedAssetsClearing;

    /**
     * 在建工程合计
     */
    @JSONField(name = "construction_process_total")
    private double constructionInProgressTotal;

    /**
     * 在建工程
     */
    @JSONField(name = "construction_in_process")
    private double constructionInProgress;

    /**
     * 工程物资
     */
    @JSONField(name = "engineering_materials")
    private double engineeringMaterials;

    /**
     * 使用权资产
     */
    @JSONField(name = "right_use_assets")
    private double rightOfUseAssets;

    /**
     * 无形资产
     */
    @JSONField(name = "intangible_assets")
    private double intangibleAssets;

    /**
     * 商誉
     */
    @JSONField(name = "goodwill")
    private double goodwill;

    /**
     * 长期待摊费用
     */
    @JSONField(name = "long_term_deferred_expenses")
    private double longTermDeferredExpenses;

    /**
     * 递延所得税资产
     */
    @JSONField(name = "deferred_tax_assets")
    private double deferredTaxAssets;

    /**
     * 其他非流动资产
     */
    @JSONField(name = "other_non_current_nets")
    private double otherNonCurrentAssets;

    /**
     * 非流动资产合计
     */
    @JSONField(name = "non_current_nets_total")
    private double nonCurrentAssetsTotal;

    /**
     * 资产总计
     */
    @JSONField(name = "assets_total")
    private double totalAssets;

    /**
     * 流动负债
     */
    @JSONField(name = "流动负债")
    private double currentLiabilities;

    /**
     * 短期借款
     */
    @JSONField(name = "short_term_loans")
    private double shortTermLoans;

    /**
     * 应付票据及应付账款
     */
    @JSONField(name = "payable_notes_and_accounts")
    private double payablesNet;

    /**
     * 应付票据
     */
    @JSONField(name = "notes_payable")
    private double notesPayable;

    /**
     * 应付账款
     */
    @JSONField(name = "accounts_payable")
    private double accountsPayable;

    /**
     * 预收款项
     */
    @JSONField(name = "collect_money")
    private double advancePaymentsReceived;

    /**
     * 合同负债
     */
    @JSONField(name = "contract_debt")
    private double contractLiabilities;

    /**
     * 应付职工薪酬
     */
    @JSONField(name = "accrued_wages")
    private double accruedCompensation;

    /**
     * 应交税费
     */
    @JSONField(name = "taxes_dues")
    private double taxesPayable;

    /**
     * 其他应付款合计
     */
    @JSONField(name = "other_payable_total")
    private double otherPayablesTotal;

    /**
     * 应付股利
     */
    @JSONField(name = "dividends_payable")
    private double dividendsPayable;

    /**
     * 其他应付款
     */
    @JSONField(name = "other_accounts_payable")
    private double otherAccountsPayable;

    /**
     * 一年内到期的非流动负债
     */
    @JSONField(name = "year_non_current_debt")
    private double currentPortionOfNonCurrentDebt;

    /**
     * 其他流动负债
     */
    @JSONField(name = "other_current_debt")
    private double otherCurrentLiabilities;

    /**
     * 流动负债合计
     */
    @JSONField(name = "current_total_debt")
    private double totalCurrentLiabilities;

    /**
     * 非流动负债
     */
    @JSONField(name = "非流动负债")
    private double nonCurrentLiabilities;

    /**
     * 长期借款
     */
    @JSONField(name = "long_term_loan")
    private double longTermLoans;

    /**
     * 租赁负债
     */
    @JSONField(name = "lease_debt")
    private double leaseLiabilities;

    /**
     * 长期应付款合计
     */
    @JSONField(name = "long_term_payable_total")
    private double longTermPayablesTotal;

    /**
     * 长期应付款
     */
    @JSONField(name = "long_term_accounts_payable")
    private double longTermAccountsPayable;

    /**
     * 递延所得税负债
     */
    @JSONField(name = "deferred_tax_debt")
    private double deferredTaxLiabilities;

    /**
     * 非流动负债递延收益
     */
    @JSONField(name = "non_current_debt_deferred_income")
    private double nonCurrentLiabilitiesDeferredIncome;

    /**
     * 非流动负债合计
     */
    @JSONField(name = "non_current_debt_total")
    private double totalNonCurrentLiabilities;

    /**
     * 负债合计
     */
    @JSONField(name = "total_debt")
    private double totalLiabilities;

    /**
     * 所有者权益
     */
    @JSONField(name = "所有者权益")
    private double ownerEquity;

    /**
     * 股本
     */
    @JSONField(name = "equity")
    private double shareCapital;

    /**
     * 资本公积
     */
    @JSONField(name = "capital_reserve")
    private double capitalSurplus;

    /**
     * 减：库存股
     */
    @JSONField(name = "treasury_stock")
    private double treasuryStock;

    /**
     * 其他综合收益
     */
    @JSONField(name = "other_comprehensive_income")
    private double otherComprehensiveIncome;

    /**
     * 盈余公积
     */
    @JSONField(name = "surplus_reserve")
    private double surplusReserve;

    /**
     * 未分配利润
     */
    @JSONField(name = "undistributed_profits")
    private double retainedEarnings;

    /**
     * 归属于母公司所有者权益合计
     */
    @JSONField(name = "parent_holder_equity_total")
    private double parentCompanyEquityTotal;

    /**
     * 少数股东权益
     */
    @JSONField(name = "minority_equity")
    private double minorityInterest;

    /**
     * 股东权益合计
     */
    @JSONField(name = "holder_equity_total")
    private double totalEquity;

    /**
     * 负债和股东权益总计
     */
    @JSONField(name = "debt_and_equity_total")
    private double totalLiabilitiesAndEquity;
}
