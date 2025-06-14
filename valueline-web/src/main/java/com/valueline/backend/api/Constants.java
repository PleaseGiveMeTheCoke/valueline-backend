package com.valueline.backend.api;

public class Constants {
    public static final String industryUrl = "http://n.biyingapi.com/hszg/zg/";
    public static final String token = "041134850e16b10270";
    public static final String split = "/";
    public static final String balanceSheetUrl = "https://basic.10jqka.com.cn/fuyao/f10_migrate/analysis/v1/form?id=client_stock_debt&code=%s&method=report&period=0&page=1&limit=10";
    public static final String marketValueUrl = "http://n.biyingapi.com/himk/ltszph/041134850e16b10270";
    public static final String priceToBookRadioUrl = "http://n.biyingapi.com/himk/sjl/041134850e16b10270";
    /**
     * %s%s: 年/季，如2024，3
     */
    public static final String debtAssetRatioUrl = "http://n.biyingapi.com/hicw/cznl/%s/%s/041134850e16b10270";

    /**
     * %s: 股票代码
     */
    public static final String monthKUrl = "http://api.biyingapi.com/zs/hfsjy/%s/mn/041134850e16b10270";


    public static final String industryUrlThs = "https://basic.10jqka.com.cn/basicapi/company_info/merge_info/v1/base_info/?code=%s&locale=zh_CN&type=stock";
    public static final String boardUrlThs = "https://dict.hexin.cn:9531/stocks?pattern=%s&isauto=1&associate=1&pl=i&isrealcode=1";
    public static final String priceUrlThs = "https://basic.10jqka.com.cn/basicapi/finance/valuation/v1/trend/?code=%s&id=stock_pb&locale=zh_CN&period=3&type=stock";

    /*
     * 问财股票筛选
     */
    public static final String wencaiSearchUrl = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
}
