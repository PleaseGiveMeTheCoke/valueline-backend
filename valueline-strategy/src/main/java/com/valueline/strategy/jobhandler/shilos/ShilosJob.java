package com.valueline.strategy.jobhandler.shilos;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.valueline.client.api.base.BaseInfoGetService;
import com.valueline.client.api.base.BaseInfoListService;
import com.valueline.client.api.price.PriceInfoGetService;
import com.valueline.client.domain.base.BalanceSheet;
import com.valueline.client.domain.base.Industry;
import com.valueline.client.domain.base.Stock;
import com.valueline.client.domain.base.StockCondition;
import com.valueline.client.domain.common.Result;
import com.valueline.client.domain.price.LowestPoint;
import com.valueline.client.dao.domain.Strategy;
import com.valueline.client.dao.domain.StrategyRunResult;
import com.valueline.strategy.jobhandler.shilos.domain.ShilosStrategyOutput;
import com.valueline.strategy.jobhandler.shilos.domain.ShilosStrategyParam;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 施洛斯选股
 */
@Component
public class ShilosJob {

    @DubboReference(version = "1.0.0.tonghuashun", timeout = 100000)
    private BaseInfoListService baseInfoListService;

    @DubboReference(version = "1.0.0.tonghuashun", timeout = 100000)
    private BaseInfoGetService baseInfoGetService;

    @DubboReference(version = "1.0.0.tonghuashun", timeout = 100000)
    private PriceInfoGetService priceInfoGetService;

    @Resource
    private Dao nutDao;

    @XxlJob("shilosJobHandler")
    public void shilosJobHandler() throws Exception {
        XxlJobContext context = XxlJobContext.getXxlJobContext();
        Strategy strategy = JSONObject.parseObject(context.getJobParam(), Strategy.class);
        ShilosStrategyParam jobParam = JSONObject.parseObject(strategy.getStrategyParam(), ShilosStrategyParam.class);
        StockCondition stockCondition = new StockCondition();
        stockCondition.setPriceToBookRadio(jobParam.getPriceToBookRadio());
        stockCondition.setDebtAssetRatio(jobParam.getDebtAssetRatio());
        stockCondition.setYear(jobParam.getYear());
        stockCondition.setSeason(jobParam.getSeason());
        Result<List<Stock>> listResult = baseInfoListService.listByCondition(stockCondition);
        if (!listResult.isSuccess()) {
            XxlJobHelper.handleFail("fail to listByCondition: " + listResult.getMessage());
            return;
        }
        List<Stock> stocks = listResult.getData();
        // 进行股票初筛
        List<Stock> filteredStocks = stocks.stream()
                // 去除ST
                .filter(s -> !s.getName().contains("ST"))
                .toList();
        XxlJobHelper.log("获取到 {} 个满足市净率和负债条件的股票", filteredStocks.size());

        List<ShilosStrategyOutput> ret = new ArrayList<>();
        for (Stock stock : filteredStocks) {
            try {
                String code = stock.getCode();
                Double marketValue = stock.getMarketValue();
                Double greamRadio = getGreamRadio(jobParam.getValueExpression(), code, marketValue);
                if (greamRadio >= jobParam.getGreamRatio() || greamRadio <= 0) {
                    XxlJobHelper.log("股票 {} 格雷厄姆比率={}，不符合要求", stock.getName(), greamRadio);
                    continue;
                }
                XxlJobHelper.log("股票 {} 格雷厄姆比率={}，满足要求", stock.getName(), greamRadio);
                ShilosStrategyOutput output = new ShilosStrategyOutput();
                output.setCode(code);
                output.setName(stock.getName());
                output.setPriceToBookRadio(stock.getPriceToBookRadio());
                output.setDebtAssetRatio(stock.getDebtAssetRatio());
                output.setGreamRatio(greamRadio);

                // 最低点涨幅
                Map<Integer, Double> maxIncrease = jobParam.getMaxIncrease();
                Result<List<LowestPoint>> lowestPointGrowthRateResult = priceInfoGetService.getLowestPointGrowthRate(code, JSON.toJSONString(maxIncrease.keySet()));
                if (!lowestPointGrowthRateResult.isSuccess()) {
                    XxlJobHelper.log("fail to get lowestPointGrowthRateResult " + stock.getName() + ", error: " + lowestPointGrowthRateResult.getMessage());
                } else {
                    List<LowestPoint> lowestPoints = lowestPointGrowthRateResult.getData();
                    List<LowestPoint> filtered = lowestPoints.stream().filter(l -> l.getIncrease() >= maxIncrease.get(l.getMonth())).toList();
                    if (!filtered.isEmpty()) {
                        // 涨幅不符合预期
                        XxlJobHelper.log("股票 {} 涨幅={}，不符合要求", stock.getName(), lowestPoints);
                        continue;
                    }
                    output.setMaxIncrease(lowestPoints);
                }

                // 行业信息
                Result<Industry> industryResult = baseInfoGetService.getIndustryByCode(code);
                if (industryResult.isSuccess()) {
                    Industry industry = industryResult.getData();
                    output.setFirstIndustry(industry.getFirstClass());
                    output.setSecondIndustry(industry.getSecondClass());
                    output.setBoard(industry.getBoard());
                }
                // 结果写入DB
                StrategyRunResult strategyRunResult = new StrategyRunResult();
                strategyRunResult.setStrategyId(strategy.getId());
                strategyRunResult.setXxlJobId(XxlJobHelper.getJobId());
                strategyRunResult.setOutput(JSON.toJSONString(output));
                strategyRunResult.setGmtCreate(new Date());
                strategyRunResult.setGmtModified(new Date());
                strategyRunResult.setCode(stock.getCode());
                nutDao.insert(strategyRunResult);
                XxlJobHelper.log("success to choose stock, name = " + stock.getName());
            } catch (Throwable e) {
                XxlJobHelper.log("fail to process stock " + stock.getName() + ", error: " + e.getMessage());
            }
        }
        XxlJobHelper.log("strategy run result: {}", ret);
    }

    private Double getGreamRadio(String valueExpression, String code, Double marketValue) {
        Result<BalanceSheet> balanceSheetResult = baseInfoGetService.getBalanceSheetByCode(code);
        if (!balanceSheetResult.isSuccess()) {
            throw new RuntimeException("fail to balanceSheet: " + balanceSheetResult.getMessage());
        }

        BalanceSheet balanceSheet = balanceSheetResult.getData();
        Double result = evaluateNet(balanceSheet);
        return marketValue / result;
    }

    private Double evaluateNet(BalanceSheet balanceSheet) {
        // 计算母公司股东权益占比
        double parentCompanyEquityRadio = (balanceSheet.getParentCompanyEquityTotal()) / (balanceSheet.getTotalEquity());

        // 资产合计
        double totalAssets = balanceSheet.getTotalAssets();
        // 负债合计
        double totalLiabilities = balanceSheet.getTotalLiabilities();

        // 非流动资产中要被剔除的（每个字段都可能为 null）
        double sub1 = balanceSheet.getFixedAssetsTotal()
                + balanceSheet.getConstructionInProgressTotal()
                + balanceSheet.getRightOfUseAssets()
                + balanceSheet.getIntangibleAssets()
                + balanceSheet.getGoodwill()
                + balanceSheet.getLongTermDeferredExpenses()
                + balanceSheet.getDeferredTaxAssets();

        // 流动资产中要被剔除的
        double sub2 = balanceSheet.getPrepaidExpenses();

        // 负债中要被剔除的
        double add = balanceSheet.getLeaseLiabilities()
                + balanceSheet.getAdvancePaymentsReceived()
                + balanceSheet.getDeferredTaxLiabilities();

        // 最终计算
        return (totalAssets - (sub1 + sub2) - totalLiabilities + add) * parentCompanyEquityRadio;
    }
}
