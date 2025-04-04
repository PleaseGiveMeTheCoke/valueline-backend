package com.valueline.web.controller;

import com.alibaba.fastjson.JSON;
import com.valueline.client.dao.domain.Strategy;
import com.valueline.client.domain.common.Result;
import com.valueline.web.wrapper.XxlAdminWrapper;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import jakarta.annotation.Resource;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Resource
    private Dao nutDao;

    @Resource
    private XxlAdminWrapper xxlAdminWrapper;

    @RequestMapping("/list")
    public Result<List<Strategy>> list() {
        try {
            Condition c = Cnd.NEW();
            List<Strategy> queryResult = nutDao.query(Strategy.class, c);
            return Result.success(queryResult);
        } catch (Throwable e) {
            return Result.fail("StrategyController list error: " + e.getMessage());
        }
    }

    @RequestMapping("/add")
    public Result<Strategy> add(@RequestBody Strategy strategy) {
        try {
            // TODO 创建xxl-job
            strategy.setGmtCreate(new Date());
            strategy.setGmtModified(new Date());
            Strategy insert = nutDao.insert(strategy);
            return Result.success(insert);
        } catch (Throwable e) {
            return Result.fail("StrategyController add error: " + e.getMessage());
        }
    }

    @RequestMapping("/update")
    public Result<Integer> update(@RequestBody Strategy strategy) {
        try {
            // TODO 编辑 xxl-job
            strategy.setGmtModified(new Date());
            int updated = nutDao.update(strategy);
            return Result.success(updated);
        } catch (Throwable e) {
            return Result.fail("StrategyController update error: " + e.getMessage());
        }
    }

    @RequestMapping("/run")
    public Result<String> run(@RequestBody Strategy strategy) {
        try {
            Long xxlJobId = strategy.getXxlJobId();
            String triggerResult = xxlAdminWrapper.trigger(xxlJobId, JSON.toJSONString(strategy));
            return Result.success(triggerResult);
        } catch (Throwable e) {
            return Result.fail("StrategyController run error: " + e.getMessage());
        }
    }

    private TriggerParam getTriggerParam(Integer jobId, String strategyParam) {
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobId);
        triggerParam.setLogId(1322);
        triggerParam.setGlueType(GlueTypeEnum.BEAN.name());
        triggerParam.setExecutorHandler("shilosJobHandler");
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setExecutorParams(strategyParam);
        triggerParam.setLogDateTime(new Date().getTime());
        return triggerParam;
    }
}
