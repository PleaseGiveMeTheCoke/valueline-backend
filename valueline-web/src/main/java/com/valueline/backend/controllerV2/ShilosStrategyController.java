package com.valueline.backend.controllerV2;

import com.valueline.backend.domain.common.Result;
import com.valueline.backend.domain.strategy.ShilosStrategy;
import com.valueline.backend.domain.strategy.ShilosStrategyInstance;
import com.valueline.backend.domain.strategy.StrategyStatus;
import com.valueline.backend.strategy.ShilosExecutor;
import jakarta.annotation.Resource;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shilos/strategy/")
public class ShilosStrategyController {

    @Resource
    private Dao nutDao;

    @Resource
    private ShilosExecutor shilosExecutor;

    @RequestMapping("/list")
    public Result<List<ShilosStrategy>> list() {
        try {
            Condition c = Cnd.NEW();
            List<ShilosStrategy> queryResult = nutDao.query(ShilosStrategy.class, c);
            return Result.success(queryResult);
        } catch (Throwable e) {
            return Result.fail("策略列表获取失败: " + e.getMessage());
        }
    }

    @RequestMapping("/create")
    public Result<Boolean> create(@RequestBody ShilosStrategy shilosStrategy) {
        try {
            shilosStrategy.setId(null);
            shilosStrategy.setGmtCreate(new Date());
            shilosStrategy.setGmtModified(new Date());
            nutDao.insert(shilosStrategy);
            return Result.success(true);
        } catch (Throwable e) {
            return Result.fail("策略创建失败：" + e.getMessage());
        }
    }

    @RequestMapping("/run")
    public Result<Boolean> run(@RequestBody Map<String, Object> payload) {
        try {
            Long strategyId = Long.valueOf(payload.get("strategyId").toString());
            ShilosStrategyInstance instance = new ShilosStrategyInstance();
            instance.setStrategyId(strategyId);
            instance.setGmtCreate(new Date());
            instance.setGmtModified(new Date());
            instance.setStatus(StrategyStatus.INIT.name());
            ShilosStrategyInstance insert = nutDao.insert(instance);
            shilosExecutor.execute(insert);
            return Result.success(true);
        } catch (Throwable e) {
            return Result.fail("策略创建失败：" + e.getMessage());
        }
    }

    //  TODO 运行结果展示


}
