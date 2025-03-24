package com.valueline.strategy.jobhandler.shilos;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.valueline.client.api.base.BaseInfoGetService;
import com.valueline.client.api.base.BaseInfoListService;
import com.valueline.client.api.price.PriceInfoGetService;
import com.valueline.client.domain.base.BalanceSheet;
import com.valueline.client.domain.base.DebtAssetRatio;
import com.valueline.client.domain.base.Industry;
import com.valueline.client.domain.base.MarketValue;
import com.valueline.client.domain.base.PriceToBookRadio;
import com.valueline.client.domain.common.Result;
import com.valueline.client.domain.price.LowestPoint;
import com.valueline.strategy.jobhandler.shilos.domain.ShilosStrategyOutput;
import com.valueline.strategy.jobhandler.shilos.domain.ShilosStrategyParam;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 施洛斯选股
 */
@Component
public class ShilosJob {

    @DubboReference(version = "1.0.0.biying", timeout = 10000)
    private BaseInfoListService baseInfoListService;

    @DubboReference(version = "1.0.0.tonghuashun", timeout = 10000)
    private BaseInfoGetService baseInfoGetTonghuashunService;

    @DubboReference(version = "1.0.0.biying", timeout = 10000)
    private BaseInfoGetService baseInfoGetBiyingService;

    @DubboReference(version = "1.0.0.biying", timeout = 10000)
    private PriceInfoGetService priceInfoGetService;

    @XxlJob("shilosJobHandler")
    public void shilosJobHandler() throws Exception {
        XxlJobContext context = XxlJobContext.getXxlJobContext();
        ShilosStrategyParam jobParam = JSONObject.parseObject(context.getJobParam(), ShilosStrategyParam.class);
        Result<List<PriceToBookRadio>> pbResult = baseInfoListService.listPriceToBookRadio();
        if (!pbResult.isSuccess()) {
            XxlJobHelper.handleFail("fail to listPriceToBookRadio: " + pbResult.getMessage());
            return;
        }
        List<PriceToBookRadio> pbs = pbResult.getData();
        Result<List<DebtAssetRatio>> debtResult = baseInfoListService.listDebtAssetRatio(jobParam.getYear(), jobParam.getSeason());
        if (!debtResult.isSuccess()) {
            XxlJobHelper.handleFail("fail to listDebtAssetRatio: " + debtResult.getMessage());
            return;
        }
        List<DebtAssetRatio> debts = debtResult.getData();
        Map<String, Double> debtMap = debts.stream().collect(Collectors.toMap(DebtAssetRatio::getCode, DebtAssetRatio::getDebtAssetRatio));
        // 进行股票初筛
        List<PriceToBookRadio> filteredPbs = pbs.stream()
                // 去除ST
                .filter(s -> !s.getName().contains("ST"))
                // 获取所有市净率满足条件的股票
                .filter(s -> s.getPriceToBookRadio() <= jobParam.getPriceToBookRadio() && s.getPriceToBookRadio() >= 0)
                // 对资产负债率进行过滤
                .filter(s -> debtMap.containsKey(s.getCode())
                        && debtMap.get(s.getCode()) <= jobParam.getDebtAssetRatio()
                        && debtMap.get(s.getCode()) >= 0)
                .toList();
        // 获取市值Map
        Result<List<MarketValue>> marketValueResult = baseInfoListService.listMarketValue();
        if (!marketValueResult.isSuccess()) {
            XxlJobHelper.handleFail("fail to listDebtAssetRatio: " + marketValueResult.getMessage());
            return;
        }
        Map<String, Double> marketValueMap = marketValueResult.getData().stream()
                .collect(Collectors.toMap(MarketValue::getCode, MarketValue::getMarketValue));
        List<ShilosStrategyOutput> ret = new ArrayList<>();
        for (PriceToBookRadio pb : filteredPbs) {
            try {
                String code = pb.getCode();
                Double marketValue = marketValueMap.get(code);
                Double greamRadio = getGreamRadio(jobParam.getValueExpression(), code, marketValue);
                if (greamRadio >= jobParam.getGreamRatio()) {
                    continue;
                }

                ShilosStrategyOutput output = new ShilosStrategyOutput();
                output.setCode(code);
                output.setName(pb.getName());
                output.setPriceToBookRadio(pb.getPriceToBookRadio());
                output.setDebtAssetRatio(debtMap.get(code));
                output.setGreamRatio(greamRadio);

                // 行业信息
                Result<Industry> industryResult = baseInfoGetBiyingService.getIndustryByCode(code);
                if (industryResult.isSuccess()) {
                    Industry industry = industryResult.getData();
                    output.setFirstIndustry(industry.getFirstClass());
                    output.setSecondIndustry(industry.getSecondClass());
                }

                // 最低点涨幅
                Map<Integer, Double> maxIncrease = jobParam.getMaxIncrease();

                Result<List<LowestPoint>> lowestPointGrowthRateResult = priceInfoGetService.getLowestPointGrowthRate(code, JSON.toJSONString(maxIncrease.keySet()));
                if (!lowestPointGrowthRateResult.isSuccess()) {
                    XxlJobHelper.log("fail to get lowestPointGrowthRateResult " + pb.getName() + ", error: " + lowestPointGrowthRateResult.getMessage());
                } else {
                    List<LowestPoint> lowestPoints = lowestPointGrowthRateResult.getData();
                    List<LowestPoint> filtered = lowestPoints.stream().filter(l -> l.getIncrease() >= maxIncrease.get(l.getMonth())).toList();
                    if (!filtered.isEmpty()) {
                        // 涨幅不符合预期
                        continue;
                    }
                    output.setMaxIncrease(lowestPoints);
                }
                XxlJobHelper.log("success to choose stock, name = " + pb.getName());
                ret.add(output);
            } catch (Throwable e) {
                XxlJobHelper.log("fail to process stock " + pb.getName() + ", error: " + e.getMessage());
            }

            XxlJobHelper.log("strategy run result: {}", ret);
        }
    }

    private Double getGreamRadio(String valueExpression, String code, Double marketValue) {
        Result<BalanceSheet> balanceSheetResult = baseInfoGetTonghuashunService.getBalanceSheetByCode(code);
        if (!balanceSheetResult.isSuccess()) {
            throw new RuntimeException("fail to balanceSheet: " + balanceSheetResult.getMessage());
        }

        BalanceSheet balanceSheet = balanceSheetResult.getData();
        Binding binding = new Binding();
        binding.setVariable("balanceSheet", balanceSheet);
        GroovyShell shell = new GroovyShell(binding);
        Object result = shell.evaluate(valueExpression);
        return Double.parseDouble(String.valueOf(result)) / marketValue;
    }
}
