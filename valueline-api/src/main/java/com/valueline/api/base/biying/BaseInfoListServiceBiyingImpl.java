package com.valueline.api.base.biying;

import com.alibaba.fastjson.JSON;
import com.valueline.api.base.Constants;
import com.valueline.api.base.biying.dto.DebtAssetRatioDTO;
import com.valueline.api.base.biying.dto.MarketValueDTO;
import com.valueline.api.base.biying.dto.PriceToBookRadioDTO;
import com.valueline.api.util.OkHttpUtil;
import com.valueline.client.api.base.BaseInfoListService;
import com.valueline.client.domain.base.DebtAssetRatio;
import com.valueline.client.domain.base.MarketValue;
import com.valueline.client.domain.base.PriceToBookRadio;
import com.valueline.client.domain.common.Result;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.remoting.http12.rest.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapping(value = "com.valueline.client.api.base.BaseInfoListService.biying")
@DubboService(version = "1.0.0.biying")
public class BaseInfoListServiceBiyingImpl implements BaseInfoListService {
    @Override
    public Result<List<MarketValue>> listMarketValue() {
        try {
            String host = Constants.marketValueUrl;
            String responseString = OkHttpUtil.getRequest(host, 3);
            List<MarketValueDTO> marketValueList = JSON.parseArray(responseString, MarketValueDTO.class);
            List<MarketValue> ret = new ArrayList<>();
            for (MarketValueDTO marketValueDTO : marketValueList) {
                MarketValue marketValue = new MarketValue();
                // 去除前缀
                marketValue.setCode(marketValueDTO.getCode().substring(2));
                marketValue.setName(marketValueDTO.getName());
                marketValue.setMarketValue(marketValueDTO.getMarketCapitalization() * 10000);
                ret.add(marketValue);
            }

            return Result.success(ret);
        } catch (Throwable e) {
            return Result.fail("system error: " + e.getMessage());
        }
    }

    @Override
    public Result<List<PriceToBookRadio>> listPriceToBookRadio() {
        try {
            String host = Constants.priceToBookRadioUrl;
            String responseString = OkHttpUtil.getRequest(host, 3);
            List<PriceToBookRadioDTO> priceToBookRadioDTOList = JSON.parseArray(responseString, PriceToBookRadioDTO.class);
            List<PriceToBookRadio> ret = new ArrayList<>();
            for (PriceToBookRadioDTO priceToBookRadioDTO : priceToBookRadioDTOList) {
                PriceToBookRadio priceToBookRadio = new PriceToBookRadio();
                priceToBookRadio.setCode(priceToBookRadioDTO.getCode().substring(2));
                priceToBookRadio.setName(priceToBookRadioDTO.getName());
                priceToBookRadio.setPriceToBookRadio(priceToBookRadioDTO.getShijing());
                ret.add(priceToBookRadio);
            }

            return Result.success(ret);
        } catch (Throwable e) {
            return Result.fail("system error: " + e.getMessage());
        }
    }

    @Override
    public Result<List<DebtAssetRatio>> listDebtAssetRatio(String year, String season) {
        try {
            String host = String.format(Constants.debtAssetRatioUrl, year, season);
            String responseString = OkHttpUtil.getRequest(host, 3);
            List<DebtAssetRatioDTO> debtAssetRatioDTOList = JSON.parseArray(responseString, DebtAssetRatioDTO.class);
            List<DebtAssetRatio> ret = new ArrayList<>();
            for (DebtAssetRatioDTO debtAssetRatioDTO : debtAssetRatioDTOList) {
                DebtAssetRatio debtAssetRatio = new DebtAssetRatio();
                debtAssetRatio.setCode(debtAssetRatioDTO.getCode());
                debtAssetRatio.setName(debtAssetRatioDTO.getName());
                debtAssetRatio.setDebtAssetRatio(debtAssetRatioDTO.getZichanFuzhaiLv());
                ret.add(debtAssetRatio);
            }

            return Result.success(ret);
        } catch (Throwable e) {
            return Result.fail("system error: " + e.getMessage());
        }
    }
}
