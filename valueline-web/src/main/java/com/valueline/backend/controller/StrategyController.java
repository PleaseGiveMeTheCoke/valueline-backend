package com.valueline.backend.controller;

import com.valueline.backend.jobhandler.ShilosJob;
import com.valueline.backend.client.domain.strategy.Strategy;
import com.valueline.backend.client.domain.common.Result;
import jakarta.annotation.Resource;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Resource
    private Dao nutDao;

    @Resource
    private ShilosJob shilosJob;

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
            String uuid = UUID.randomUUID().toString();
            Executors.newSingleThreadExecutor().execute(() -> {
                shilosJob.shilosJobHandler(uuid, strategy);
            });
            return Result.success(uuid);
        } catch (Throwable e) {
            return Result.fail("StrategyController run error: " + e.getMessage());
        }
    }

    @RequestMapping("/getLog")
    public Result<String> getLog(@RequestBody String instanceId) {
        try {
            String log = shilosJob.getLog(instanceId);
            return Result.success(log);
        } catch (Throwable e) {
            return Result.fail("StrategyController getLog error: " + e.getMessage());
        }
    }
}
