package com.valueline.backend.controller;

import com.valueline.backend.client.domain.strategy.StrategyRunResult;
import com.valueline.backend.client.domain.common.Result;
import jakarta.annotation.Resource;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/strategy/result")  // ✅ 正确写法（去掉末尾的斜杠）
public class StrategyRunResultController {

    @Resource
    private Dao nutDao;

    @RequestMapping("/list")
    public Result<List<StrategyRunResult>> list(@RequestParam String strategyId,
                                                @RequestParam Long beginDay,
                                                @RequestParam Long endDay) {
        try {
            Date beginDate = new Date(beginDay);
            Date endDate = new Date(endDay);
            Condition c = Cnd.where("strategy_id", "=", strategyId).and("gmt_create", "<=", endDate).and("gmt_create", ">=", beginDate).desc("id");
            List<StrategyRunResult> queryResult = nutDao.query(StrategyRunResult.class, c);
            return Result.success(queryResult);
        } catch (Throwable e) {
            return Result.fail("StrategyRunResultController list error: " + e.getMessage());
        }
    }
}
