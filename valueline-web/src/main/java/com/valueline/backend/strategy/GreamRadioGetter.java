package com.valueline.backend.strategy;

import com.alibaba.fastjson.JSONObject;
import com.valueline.backend.domain.base.BalanceSheet;
import com.valueline.backend.domain.base.Stock;
import com.valueline.backend.domain.common.Result;
import com.valueline.backend.domain.strategy.StockBalanceSheet;
import com.valueline.backend.service.BaseInfoGetService;
import jakarta.annotation.Resource;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class GreamRadioGetter {

    @Resource
    private BaseInfoGetService baseInfoGetServiceTonghuashunImpl;

    @Resource
    private Dao nutDao;

    public Double getGreamRadio(Stock stock, String year, String season) {
        String code = stock.getCode();
        Condition c = Cnd.where("code", "=", code).and("year", "=", year).and("season", "=", season);
        List<StockBalanceSheet> queryResult = nutDao.query(StockBalanceSheet.class, c);
        StockBalanceSheet stockBalanceSheet;
        if (!queryResult.isEmpty()) {
            stockBalanceSheet = queryResult.get(0);
        } else {
            stockBalanceSheet = getStockBalanceSheet(stock, year, season);
            nutDao.insert(stockBalanceSheet);
        }
        return stockBalanceSheet.getNetValue() / stock.getMarketValue();
    }

    @NotNull
    private StockBalanceSheet getStockBalanceSheet(Stock stock, String year, String season) {
        Result<BalanceSheet> balanceSheetResult = baseInfoGetServiceTonghuashunImpl.getBalanceSheetByCode(stock.getCode());
        if (!balanceSheetResult.isSuccess()) {
            throw new RuntimeException("fail to balanceSheet: " + balanceSheetResult.getMessage());
        }

        BalanceSheet balanceSheet = balanceSheetResult.getData();
        StockBalanceSheet stockBalanceSheet = new StockBalanceSheet();
        stockBalanceSheet.setCode(stock.getCode());
        stockBalanceSheet.setName(stock.getName());
        stockBalanceSheet.setYear(year);
        stockBalanceSheet.setSeason(season);
        stockBalanceSheet.setBalanceSheet(JSONObject.toJSONString(balanceSheet));
        stockBalanceSheet.setGmtModified(new Date());
        stockBalanceSheet.setGmtCreate(new Date());
        Pair<Double, String> res = evaluateNet(balanceSheet);
        stockBalanceSheet.setNetValue(res.getFirst());
        stockBalanceSheet.setCalculateProcess(res.getSecond());
        return stockBalanceSheet;
    }

    private Pair<Double, String> evaluateNet(BalanceSheet balanceSheet) {
        StringBuilder calculateProcess = new StringBuilder();

        // 流动资产
        calculateProcess.append("流动资产: \n");
        calculateProcess.append(MessageFormat.format("货币资金: {0}\n", balanceSheet.getCash()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("交易性金融资产: {0}\n", balanceSheet.getTradingFinancialAssets()));
        calculateProcess.append("+\n");
        // calculateProcess.append(MessageFormat.format("衍生金融资产: {}\n", ));
        calculateProcess.append(MessageFormat.format("应收票据: {0}\n", balanceSheet.getNotesReceivable()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("应收账款: {0}\n", balanceSheet.getAccountsReceivable()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("合同资产: {0}\n", balanceSheet.getContractAssets()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("应收款项融资: {0}\n", balanceSheet.getFinancingReceivables()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("预付款项: {0}\n", balanceSheet.getPrepaidExpenses()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("其他应收款: {0}\n", balanceSheet.getOtherReceivablesTotal()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("存货: {0}\n", balanceSheet.getInventory()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("持有待售资产: {0}\n", balanceSheet.getAssetsHeldForSale()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("一年内到期的非流动资产: {0}\n", balanceSheet.getNonCurrentAssetDueYear())); // 假设没有单独字段，用 0 占位
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("其他流动资产: {0}\n", balanceSheet.getOtherCurrentAssets()));
        // TODO 衍生金融资产
        double currentAssetsTotal = balanceSheet.getCash() + balanceSheet.getTradingFinancialAssets() + balanceSheet.getNotesReceivable()
                + balanceSheet.getAccountsReceivable() + balanceSheet.getContractAssets()
                + balanceSheet.getFinancingReceivables() + balanceSheet.getPrepaidExpenses() + balanceSheet.getOtherReceivables()
                + balanceSheet.getInventory() + balanceSheet.getAssetsHeldForSale() + balanceSheet.getNonCurrentAssetDueYear()
                + balanceSheet.getOtherCurrentAssets();
        calculateProcess.append(MessageFormat.format("= {0}\n\n", currentAssetsTotal));

        // 非流动资产
        calculateProcess.append("非流动资产: \n");
        calculateProcess.append(MessageFormat.format("债权投资: {0}\n", balanceSheet.getDebtInvestment()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("其他债权投资: {0}\n", balanceSheet.getOtherDebtInvestment()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("长期应收款: {0}\n", balanceSheet.getLongTermReceivables()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("长期股权投资: {0}\n", balanceSheet.getLongTermEquityInvestment()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("其他权益工具投资: {0}\n", balanceSheet.getOtherEquityInstrumentsInvestment()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("其他非流动金融资产: {0}\n", balanceSheet.getOtherNonCurrentAssets()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("投资性房地产: {0}\n", balanceSheet.getInvestmentProperty()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("其他非流动资产: {0}\n", balanceSheet.getOtherNonCurrentNets()));

        double nonCurrentAssetsTotal = balanceSheet.getDebtInvestment()
                + balanceSheet.getOtherDebtInvestment()
                + balanceSheet.getLongTermReceivables()
                + balanceSheet.getLongTermEquityInvestment()
                + balanceSheet.getOtherEquityInstrumentsInvestment()
                + balanceSheet.getOtherNonCurrentAssets()
                + balanceSheet.getInvestmentProperty()
                + balanceSheet.getOtherNonCurrentNets();

        calculateProcess.append(MessageFormat.format("= {0}\n\n", nonCurrentAssetsTotal));

        calculateProcess.append("负债合计: \n");
        calculateProcess.append(MessageFormat.format("流动负债合计: {0}\n", balanceSheet.getTotalCurrentLiabilities()));
        calculateProcess.append("+\n");
        calculateProcess.append(MessageFormat.format("非流动负债合计: {0}\n", balanceSheet.getTotalNonCurrentLiabilities()));
        calculateProcess.append("-\n");
        calculateProcess.append(MessageFormat.format("租赁负债: {0}\n", balanceSheet.getLeaseLiabilities()));

        double liabilities = balanceSheet.getTotalCurrentLiabilities() + balanceSheet.getTotalNonCurrentLiabilities() - balanceSheet.getLeaseLiabilities();
        calculateProcess.append(MessageFormat.format("= {0}\n\n", liabilities));


        // 计算母公司股东权益占比
        double parentCompanyEquityRadio = (balanceSheet.getParentCompanyEquityTotal()) / (balanceSheet.getTotalEquity());
        calculateProcess.append(MessageFormat.format("母公司股东权益占比: {0} / {1} = {2}\n", balanceSheet.getParentCompanyEquityTotal(), balanceSheet.getTotalEquity(), parentCompanyEquityRadio));
        double ret = (currentAssetsTotal + nonCurrentAssetsTotal - liabilities) * parentCompanyEquityRadio;
        calculateProcess.append(MessageFormat.format("最终结果: ({0} + {1} - {2}) * {3}  = {4}\n", currentAssetsTotal, nonCurrentAssetsTotal, liabilities, parentCompanyEquityRadio, ret));

        // 最终计算
        return new Pair<>(ret, calculateProcess.toString());
    }

}
