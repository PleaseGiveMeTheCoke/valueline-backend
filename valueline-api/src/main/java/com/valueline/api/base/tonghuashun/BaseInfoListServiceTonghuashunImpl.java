package com.valueline.api.base.tonghuashun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.valueline.api.base.Constants;
import com.valueline.api.util.OkHttpUtil;
import com.valueline.client.api.base.BaseInfoListService;
import com.valueline.client.domain.base.Stock;
import com.valueline.client.domain.base.StockCondition;
import com.valueline.client.domain.common.Result;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.remoting.http12.rest.Mapping;
import org.apache.dubbo.remoting.http12.rest.Param;
import org.apache.dubbo.remoting.http12.rest.ParamType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapping(value = "com.valueline.client.api.base.BaseInfoListService.tonghuashun")
@DubboService(version = "1.0.0.tonghuashun")
public class BaseInfoListServiceTonghuashunImpl implements BaseInfoListService {
    public Result<List<Stock>> listByCondition(@Param(type = ParamType.Body, value = "stockCondition") StockCondition stockCondition) {
        List<Stock> stockList = new ArrayList<>();
        try {
            int curPage = 1;
            boolean shouldContinue = true;
            Double targetPbRatio = stockCondition.getPriceToBookRadio();
            Double targetDebtRatio = stockCondition.getDebtAssetRatio(); // 获取资产负债率筛选条件
            if (stockCondition.getDay() == null) {
                stockCondition.setDay(getLatestDateFromApi());
            }
            while (shouldContinue) {
                Map<String, String> formBody = createRequestForm(stockCondition, curPage);
                String responseString = OkHttpUtil.postFormRequest(Constants.wencaiSearchUrl, formBody);

                // Parse and validate response
                JSONObject responseJson = JSON.parseObject(responseString);
                if (!"0".equals(responseJson.getString("status_code"))) {
                    return Result.fail(responseJson.getString("status_msg"));
                }

                // Process response data
                Optional<JSONObject> tableComponent = findTableComponent(responseJson);
                if (!tableComponent.isPresent()) {
                    break;
                }

                JSONArray datas = tableComponent.get().getJSONObject("data").getJSONArray("datas");
                if (datas == null || datas.isEmpty()) {
                    break;
                }

                // Process stocks in current page
                boolean foundValidStockInPage = false;

                for (int i = 0; i < datas.size(); i++) {
                    JSONObject stockData = datas.getJSONObject(i);
                    String pbKey = String.format("市净率(pb)[%s]", stockCondition.getDay());
                    Double pbValue = stockData.getDouble(pbKey);

                    // Skip negative PB ratios
                    if (pbValue == null || pbValue < 0) {
                        continue;
                    }

                    // Stop if PB exceeds target (since results are in ascending order)
                    if (targetPbRatio != null && pbValue > targetPbRatio) {
                        shouldContinue = false;
                        break;
                    }

                    Double debtRatio = Double.valueOf(getDebtRatioValue(stockData));
                    // 筛选资产负债率
                    if (targetDebtRatio != null && debtRatio != null) {
                        // 注意：入参0.4代表40%，所以比较时需要乘以100
                        if (debtRatio > targetDebtRatio * 100) {
                            continue; // 跳过不符合条件的记录
                        }
                    }

                    Stock stock = createStockFromData(stockData, stockCondition, pbValue);
                    stockList.add(stock);
                    foundValidStockInPage = true;
                }

                // Stop if no valid stocks found in current page
                if (!foundValidStockInPage) {
                    shouldContinue = false;
                } else {
                    curPage++;
                }
            }

            return Result.success(stockList);
        } catch (Throwable e) {
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    private Map<String, String> createRequestForm(StockCondition condition, int page) {
        Map<String, String> formBody = new HashMap<>();
        formBody.put("query", String.format("市净率和市值和%s季报资产负债率", condition.getSeason()));
        formBody.put("urp_sort_way", "asc");
        formBody.put("urp_sort_index", String.format("市净率(pb)[%s]", condition.getDay()));
        formBody.put("comp_id", "6836372");
        formBody.put("query_type", "stock");
        formBody.put("ret", "json_all");
        formBody.put("date_range[0]", condition.getDay());
        formBody.put("date_range[1]", condition.getDay());
        formBody.put("urp_use_sort", "1");
        formBody.put("uuid", "24087");
        formBody.put("perpage", "100");
        formBody.put("page", String.valueOf(page));
        return formBody;
    }

    private Optional<JSONObject> findTableComponent(JSONObject responseJson) {
        JSONArray components = responseJson.getJSONObject("answer").getJSONArray("components");
        if (components == null) {
            return Optional.empty();
        }

        for (int i = 0; i < components.size(); i++) {
            JSONObject component = components.getJSONObject(i);
            if ("xuangu_tableV1".equals(component.getString("show_type"))) {
                return Optional.of(component);
            }
        }
        return Optional.empty();
    }

    private Stock createStockFromData(JSONObject stockData, StockCondition condition, Double pbValue) {
        Stock stock = new Stock();
        stock.setCode(stockData.getString("股票代码").substring(0, 6));
        stock.setName(stockData.getString("股票简称"));
        stock.setPriceToBookRadio(pbValue);

        String mvKey = String.format("总市值[%s]", condition.getDay());
        stock.setMarketValue(stockData.getDouble(mvKey));

        // 统一处理资产负债率的key，去掉日期部分
        String debtRatioValue = getDebtRatioValue(stockData);
        stock.setDebtAssetRatio(Double.parseDouble(debtRatioValue) / 100.0);

        return stock;
    }

    private String getDebtRatioValue(JSONObject stockData) {
        // 遍历所有key，找出资产负债率相关的key
        for (String key : stockData.keySet()) {
            if (key.startsWith("资产负债率")) {
                String value = stockData.getString(key);
                return value != null ? value : "0"; // 默认返回0如果值为null
            }
        }
        return "0"; // 默认返回0如果找不到key
    }

    // 新增方法：从API获取最新日期
    private String getLatestDateFromApi() throws Exception {
        // 第一次调用获取元数据
        Map<String, String> formBody = new HashMap<>();
        formBody.put("query", "市净率");
        formBody.put("comp_id", "6836372");
        formBody.put("query_type", "stock");
        formBody.put("ret", "json_all");
        formBody.put("urp_use_sort", "1");
        formBody.put("uuid", "24087");
        formBody.put("perpage", "100");
        formBody.put("page", "1");

        String metaResponse = OkHttpUtil.postFormRequest(Constants.wencaiSearchUrl, formBody);
        JSONObject metaJson = JSON.parseObject(metaResponse);

        // 从columns中解析timestamp
        JSONArray components = metaJson.getJSONObject("answer").getJSONArray("components");
        for (int i = 0; i < components.size(); i++) {
            JSONObject component = components.getJSONObject(i);
            if ("xuangu_tableV1".equals(component.getString("show_type"))) {
                JSONArray columns = component.getJSONObject("data").getJSONArray("columns");
                for (int j = 0; j < columns.size(); j++) {
                    JSONObject column = columns.getJSONObject(j);
                    if ("市净率(pb)".equals(column.getString("index_name"))) {
                        return column.getString("timestamp"); // 返回如"20250403"
                    }
                }
            }
        }
        return null;
    }

}
