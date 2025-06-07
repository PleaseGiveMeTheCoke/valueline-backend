package com.valueline.backend.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.valueline.backend.client.domain.strategy.Strategy;
import com.valueline.backend.client.domain.strategy.StrategyRunResult;
import com.valueline.backend.client.domain.base.BalanceSheet;
import com.valueline.backend.client.domain.base.Industry;
import com.valueline.backend.client.domain.base.Stock;
import com.valueline.backend.client.domain.base.StockCondition;
import com.valueline.backend.client.domain.common.Result;
import com.valueline.backend.client.domain.price.LowestPoint;
import com.valueline.backend.client.domain.strategy.ShilosStrategyOutput;
import com.valueline.backend.client.domain.strategy.ShilosStrategyParam;
import com.valueline.backend.client.service.BaseInfoGetService;
import com.valueline.backend.client.service.BaseInfoListService;
import com.valueline.backend.client.service.PriceInfoGetService;
import jakarta.annotation.Resource;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 施洛斯选股
 */
@Component
public class ShilosJob {

    @Resource
    private BaseInfoListService baseInfoListService;

    @Resource
    private BaseInfoGetService baseInfoGetService;

    @Resource
    private PriceInfoGetService priceInfoGetService;

    @Resource
    private Dao nutDao;

    private Map<String, StringBuffer> logs = new ConcurrentHashMap<>();

    public void shilosJobHandler(String instanceId, Strategy strategy) {
        ShilosStrategyParam jobParam = JSONObject.parseObject(strategy.getStrategyParam(), ShilosStrategyParam.class);
        StockCondition stockCondition = new StockCondition();
        stockCondition.setPriceToBookRadio(jobParam.getPriceToBookRadio());
        stockCondition.setDebtAssetRatio(jobParam.getDebtAssetRatio());
        stockCondition.setYear(jobParam.getYear());
        stockCondition.setSeason(jobParam.getSeason());
        StringBuffer log = new StringBuffer();
        logs.put(instanceId, log);
        Result<List<Stock>> listResult = baseInfoListService.listByCondition(stockCondition);
        if (!listResult.isSuccess()) {
            log.append("获取股票列表失败: " + listResult.getMessage());
            return;
        }
        List<Stock> stocks = listResult.getData();
        // 进行股票初筛
        List<Stock> filteredStocks = stocks.stream()
                // 去除ST
                .filter(s -> !s.getName().contains("ST"))
                .toList();
        log.append("获取到 " + filteredStocks.size() + " 个满足市净率和负债条件的股票\n");
        for (Stock stock : filteredStocks) {
            try {
                String code = stock.getCode();
                Double marketValue = stock.getMarketValue();
                Double greamRadio = getGreamRadio(jobParam.getValueExpression(), code, marketValue);
                if (greamRadio >= jobParam.getGreamRatio() || greamRadio <= 0) {
                    log.append("股票 " + stock.getName() + " 格雷厄姆比率=" + greamRadio + "，不符合要求\n");
                    continue;
                }
                log.append("股票 " + stock.getName() + " 格雷厄姆比率=" + greamRadio + "，满足要求\n");
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
                    log.append("股票 " + stock.getName() + "获取最低点涨幅失败, error: " + lowestPointGrowthRateResult.getMessage() + "\n");
                } else {
                    List<LowestPoint> lowestPoints = lowestPointGrowthRateResult.getData();
                    List<LowestPoint> filtered = lowestPoints.stream().filter(l -> l.getIncrease() >= maxIncrease.get(l.getMonth())).toList();
                    if (!filtered.isEmpty()) {
                        // 涨幅不符合预期
                        log.append("股票 " + stock.getName() + " 涨幅=" + lowestPoints + "，不符合要求\n");
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
                strategyRunResult.setOutput(JSON.toJSONString(output));
                strategyRunResult.setGmtCreate(new Date());
                strategyRunResult.setGmtModified(new Date());
                strategyRunResult.setCode(stock.getCode());
                nutDao.insert(strategyRunResult);
                log.append("股票：" + stock.getName() + "筛选成功\n");
            } catch (Throwable e) {
                log.append("股票：" + stock.getName() + "筛选失败, error: " + e.getMessage() + "\n");
            }
        }
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

    public String getLog(String instanceId) {
        return logs.get(instanceId).toString();
    }
}
