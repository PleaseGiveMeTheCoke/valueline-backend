package com.valueline.strategy.jobhandler.shilos;

public class GreamExample {
    public static final String expression = """
                // 计算母公司股东权益占比
                double parentCompanyEquityRadio = (balanceSheet.parentCompanyEquityTotal) / (balanceSheet.totalEquity);
            
                // 资产合计
                double totalAssets = balanceSheet.totalAssets;
                // 负债合计
                double totalLiabilities = balanceSheet.totalLiabilities;
            
                // 非流动资产中要被剔除的（每个字段都可能为 null）
                double sub1 = (balanceSheet.fixedAssetsTotal ?: 0)
                            + (balanceSheet.constructionInProgressTotal ?: 0)
                            + (balanceSheet.rightOfUseAssets ?: 0)
                            + (balanceSheet.intangibleAssets ?: 0)
                            + (balanceSheet.goodwill ?: 0)
                            + (balanceSheet.longTermDeferredExpenses ?: 0)
                            + (balanceSheet.deferredTaxAssets ?: 0);
            
                // 流动资产中要被剔除的
                double sub2 = balanceSheet.prepaidExpenses ?: 0;
            
                // 负债中要被剔除的
                double add = (balanceSheet.leaseLiabilities ?: 0)
                           + (balanceSheet.advancePaymentsReceived ?: 0)
                           + (balanceSheet.deferredTaxLiabilities ?: 0);
            
                // 最终计算
                return (totalAssets - (sub1 + sub2) - totalLiabilities + add) * parentCompanyEquityRadio;
            """;
}