package com.valueline.api.base;

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
}
