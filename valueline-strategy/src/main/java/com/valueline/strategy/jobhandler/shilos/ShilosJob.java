package com.valueline.strategy.jobhandler.shilos;

import com.alibaba.fastjson.JSONObject;
import com.valueline.client.api.base.BaseInfoListService;
import com.valueline.client.domain.base.DebtAssetRatio;
import com.valueline.client.domain.base.MarketValue;
import com.valueline.client.domain.base.PriceToBookRadio;
import com.valueline.client.domain.common.Result;
import com.valueline.strategy.jobhandler.shilos.domain.ShilosStrategyOutput;
import com.valueline.strategy.jobhandler.shilos.domain.ShilosStrategyParam;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

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

    @XxlJob("shilosJobHandler")
    public void demoJobHandler() throws Exception {
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
                // TODO 行业信息/涨幅信息填充
            } catch (Throwable e) {
                XxlJobHelper.log("fail to process stock " + pb.getName() + ", error: " + e.getMessage());
            }


        }
    }

    private Double getGreamRadio(String valueExpression, String code, Double marketValue) {
        // TODO 格雷厄姆指标获取
    }
}
