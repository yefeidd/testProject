package cn.zn.com.zn_android.presenter.requestType;

/**
 * Created by Jolly on 2016/8/1 0001.
 */
public enum StockRequestType {
    ADD_SELF_SELECT, // 添加自选股
    QUERY_SELF_SELECT, // 查询自选股列表
    DEL_SELF_SELECT, // 查询自选股列表
    QUERY_ZFB_LIST, // 查询涨幅榜
    QUERY_DFB_LIST, // 查询跌幅榜
    QUERY_HSL_LIST, // 查询换手率榜
    QUERY_ZF_LIST, // 查询震幅榜
    QUERY_GJGB_LIST, // 查询高价股榜
    QUERY_DJGB_LIST, // 查询高教股榜
    QUERY_GGT_LIST, // 查询港股通
    QUERY_HY_LIST, // 查询某个行业下的股票列表
    QUERY_GN_LIST, // 查询某个概念下的股票列表

    QUERY_HK_MAIN_UP, // 主板涨幅榜
    QUERY_HK_MAIN_DOWN, // 主板跌幅榜
    QUERY_HK_MAIN_MONEY, // 主板成交额榜
    QUERY_HK_NEW_UP, // 创业板涨幅榜
    QUERY_HK_NEW_DOWN, // 创业板跌幅榜
    QUERY_HK_NEW_MONEY, // 创业板成交额榜

    QUERY_SH_DETAIL, // 上证指数详情数据
    QUERY_SZ_DETAIL, // 上证指数详情数据

    QUERY_HK_UPDOWN_HS_UP, // 恒生指数成份股涨幅
    QUERY_HK_UPDOWN_HS_DOWN, // 恒生指数成份股跌幅
    QUERY_HK_UPDOWN_HS_CHANGE, // 恒生指数成份股换手率
    QUERY_HK_UPDOWN_GQ_UP, // 国企指数成份股涨幅
    QUERY_HK_UPDOWN_GQ_DOWN, // 国企指数成份股跌幅
    QUERY_HK_UPDOWN_GQ_CHANGE, // 国企指数成份股换手率
    QUERY_HK_UPDOWN_HC_UP, // 红绸指数成份股涨幅
    QUERY_HK_UPDOWN_HC_DOWN, // 红绸指数成份股跌幅
    QUERY_HK_UPDOWN_HC_CHANGE, // 红绸指数成份股换手率

    QUERY_SH_UPDOWN_SSE, // 上证指数成份股
    QUERY_SH_UPDOWN_SZ, // 深证成指成份股
    QUERY_SH_UPDOWN_REM, // 创业板指成份股

    QUERY_HK_DETAIL_HS, // 恒生指数详情数据
    QUERY_HK_DETAIL_GQ, // 国企指数详情数据
    QUERY_HK_DETAIL_HC, // 红筹指数详情数据

    SEARCH_STOCK, // 查询股票
}
