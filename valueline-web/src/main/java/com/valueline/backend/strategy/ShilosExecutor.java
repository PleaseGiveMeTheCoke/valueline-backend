package com.valueline.backend.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.valueline.backend.common.ExecutorLog;
import com.valueline.backend.domain.base.Industry;
import com.valueline.backend.domain.base.Stock;
import com.valueline.backend.domain.base.StockCondition;
import com.valueline.backend.domain.common.Result;
import com.valueline.backend.domain.price.LowestPoint;
import com.valueline.backend.domain.strategy.ShilosStrategy;
import com.valueline.backend.domain.strategy.ShilosStrategyInstance;
import com.valueline.backend.domain.strategy.ShilosStrategyOutput;
import com.valueline.backend.domain.strategy.StrategyStatus;
import com.valueline.backend.service.BaseInfoGetService;
import com.valueline.backend.service.BaseInfoListService;
import com.valueline.backend.service.PriceInfoGetService;
import jakarta.annotation.Resource;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@Component
public class ShilosExecutor {
    @Resource
    private Dao nutDao;

    @Resource
    private BaseInfoListService baseInfoListService;

    @Resource
    private BaseInfoGetService baseInfoGetServiceTonghuashunImpl;

    @Resource
    private PriceInfoGetService priceInfoGetService;

    @Resource
    private GreamRadioGetter greamRadioGetter;

    public void execute(ShilosStrategyInstance instance) {
        ExecutorLog logger = new ExecutorLog();
        Long strategyId = instance.getStrategyId();
        Condition c = Cnd.where("id", "=", strategyId);
        List<ShilosStrategy> queryResult = nutDao.query(ShilosStrategy.class, c);
        ShilosStrategy shilosStrategy = queryResult.get(0);
        instance.setStatus(StrategyStatus.RUNNING.name());
        nutDao.update(instance);
        logger.log("开始运行");
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                execute0(shilosStrategy, instance, logger);
                instance.setStatus(StrategyStatus.SUCCESS.name());
            } catch (Throwable e) {
                instance.setStatus(StrategyStatus.FAIL.name());
                logger.log("运行失败，错误: {0}", e.getMessage());
            } finally {
                instance.setRunResult(logger.collect());
                instance.setGmtModified(new Date());
                nutDao.update(instance);
            }
        });
    }

    private void execute0(ShilosStrategy shilosStrategy, ShilosStrategyInstance instance, ExecutorLog logger) {
        StockCondition stockCondition = new StockCondition();
        stockCondition.setPriceToBookRadio(shilosStrategy.getPriceToBookRadio());
        stockCondition.setDebtAssetRatio(shilosStrategy.getDebtAssetRatio());
        stockCondition.setYear(shilosStrategy.getYear());
        stockCondition.setSeason(shilosStrategy.getSeason());
        Result<List<Stock>> listResult = baseInfoListService.listByCondition(stockCondition);
        if (!listResult.isSuccess()) {
            throw new RuntimeException("获取股票列表失败: " + listResult.getMessage());
        }
        List<Stock> stocks = listResult.getData();
        // 进行股票初筛
        List<Stock> filteredStocks = stocks.stream()
                // 去除ST
                .filter(s -> !s.getName().contains("ST"))
                .toList();
        logger.log("获取到 {0} 个满足市净率和负债条件的股票", filteredStocks.size());
        for (Stock stock : filteredStocks) {
            try {
                String code = stock.getCode();
                Double greamRadio = greamRadioGetter.getGreamRadio(stock, shilosStrategy.getYear(), shilosStrategy.getSeason());
                if (greamRadio <= shilosStrategy.getGreamRatio()) {
                    logger.log("股票 {0} 格雷厄姆比率= {1}，不符合要求", stock.getName(), greamRadio);
                    continue;
                }
                logger.log("股票 {0} 格雷厄姆比率= {1}，满足要求", stock.getName(), greamRadio);
                ShilosStrategyOutput output = new ShilosStrategyOutput();
                output.setCode(code);
                output.setName(stock.getName());
                output.setPriceToBookRadio(stock.getPriceToBookRadio());
                output.setDebtAssetRatio(stock.getDebtAssetRatio());
                output.setGreamRatio(greamRadio);

                // 最低点涨幅
                if (shilosStrategy.getMaxIncrease() != null && !shilosStrategy.getMaxIncrease().isEmpty()) {
                    Map<Integer, String> maxIncrease = JSONObject.parseObject(shilosStrategy.getMaxIncrease(), Map.class);
                    Result<List<LowestPoint>> lowestPointGrowthRateResult = priceInfoGetService.getLowestPointGrowthRate(code, JSON.toJSONString(maxIncrease.keySet()));
                    if (!lowestPointGrowthRateResult.isSuccess()) {
                        logger.log("股票 {0} 获取最低点涨幅失败, error: {1}", stock.getName(), lowestPointGrowthRateResult.getMessage());
                    } else {
                        List<LowestPoint> lowestPoints = lowestPointGrowthRateResult.getData();
                        List<LowestPoint> filtered = lowestPoints.stream().filter(l -> l.getIncrease() >= Double.parseDouble(maxIncrease.getOrDefault(l.getMonth(), "100.0"))).toList();
                        if (!filtered.isEmpty()) {
                            // 涨幅不符合预期
                            logger.log("股票 {0} 涨幅= {1}，不符合要求", stock.getName(), lowestPoints);
                            continue;
                        }
                        output.setMaxIncrease(JSON.toJSONString(lowestPoints));
                    }
                }

                // 行业信息
                Result<Industry> industryResult = baseInfoGetServiceTonghuashunImpl.getIndustryByCode(code);
                if (industryResult.isSuccess()) {
                    Industry industry = industryResult.getData();
                    output.setFirstIndustry(industry.getFirstClass());
                    output.setSecondIndustry(industry.getSecondClass());
                    output.setBoard(industry.getBoard());
                }
                // 结果写入DB
                output.setStrategyInstanceId(instance.getId());
                output.setGmtCreate(new Date());
                output.setGmtModified(new Date());
                nutDao.insert(output);
                logger.log("股票 {0} 筛选成功", stock.getName());
            } catch (Throwable e) {
                logger.log("股票 {0} 筛选失败, error: {1}", stock.getName(), e.getMessage());
            }
        }
    }

}
