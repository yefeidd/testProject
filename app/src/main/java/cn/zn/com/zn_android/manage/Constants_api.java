package cn.zn.com.zn_android.manage;

/**
 * Created by WJL on 2016/3/10 0010 10:20.
 */
public class Constants_api {

    //    public final static String BASE_URL = "http://192.168.1.161:98";// 内网
//    public final static String BASE_URL = "http://tt.zhengniu.net";// 证牛测试网
//    public final static String BASE_URL = "http://fz.zhengniu.net";// 证牛测试网
                public final static String BASE_URL = "http://www.zhengniu.net";// 证牛真实环境
    //    public final static String BASE_URL = "http://tt.98cfw.com";// 98财富网测试网
    //    public final static String BASE_URL = "http://www.98cfw.com";// 98财富网真实环境
    public final static String BASE_API_URL = BASE_URL + "/Api";// 线上ip最新

    public final static String LOGIN_URL = "/User/login";//登录
    public final static String SEND_REGIST_CODE = "/User/sds6mg_jazf";//注册发验证码
    public final static String SEND_SMS_FIND_PW = "/User/sdl5d_szdfg";//找回密码发验证码
    public final static String FIND_PW = "/User/zpass";//找回密码
    public final static String MODIFY_PW = "/Member/spass";//修改密码
    public final static String SEND_REGISTER_INFO = "/User/registered";//发送注册信息
    public final static String GET_MEMBER_INFO = "/Member/meminfo";//获取会员信息
    public final static String MODIFY_MEMBER_INFO = "/Member/uinfo";//修改会员信息
    public final static String UPLOAD_TOKEN = "/Upload/token";// 获取七牛token
    public final static String SEND_IM_MSG = "/Im/send";// 向服务器发送消息内容
    public final static String LOAD_BANER_INFO = "/System/getbanner";// 获取广告内容
    public final static String GET_HOT_LIVE_INFO = "/Room/index";// 获取热门视频列表
    public final static String EXIT_LOGIN = "/Member/out";// 退出登陆
    public final static String GET_PRIVATE_MSG = "/Room/getpmsg";// 获取特约讲堂悄悄话列表
    public final static String GET_USER_PRIVATE_MSG = "/Member/getmemqa";// 获取个人悄悄话列表
    public final static String GET_VIP_STATE = "/Room/isroomvip";// 获取vip状态
    public final static String CHAT_LIST = "/Index/im";// 获取聊天的html界面
    public final static String VIDEO_LIST = "/Video/video_list";// 获取视频列表
    public final static String CONTRIBUTION = "/Room/getcontribution";// 获取贡献榜
    public final static String VIP_MEMBER_INSTRU = "/Member/vippre";// VIP会员介绍
    public final static String GET_COURSE = "/Room/getkcb";// 获取课程表
    public final static String GET_NOTICE = "/Room/getplacard";// 获取特约讲堂公告

    public final static String ARTICLE = "/Room/getart";// 获取博文列表

    public final static String GET_COLLECT_LIST = "/Member/user_collect_list";// 会员收藏列表(文章，视频)
    public final static String ADD_VIDEO_COLLECT = "/Member/add_video_collect";// 增加视频收藏
    public final static String GET_COLLECT_ROOM_LIST = "/Member/user_collect_room";// 会员关注列表(房间)
    public final static String TACTICS_LIST = "/Member/user_tactics";// 会员购买策略列表

    public final static String ART_CLICK = "/Room/add_art_click";// 增加点击量
    public final static String ART_LIKES = "/Room/add_art_likes";// 增加文章点赞量
    public final static String ART_COLLECT = "/Member/add_art_collect";// 增加文章收藏量
    public final static String IS_COLLECT_ART = "/Member/is_collect_art";// 文章是否被收藏过
    public final static String SEND_PRIVATE_MSG = "/Member/sendqqh";// 发送悄悄话

    public final static String IS_COLLECT_ROOM = "/Member/is_collect_room"; // 房间是否被关注过
    public final static String ADD_ROOM_COLLECT = "/Member/add_room_collect"; // 增加房间收藏量
    public final static String BUY_TACTICS = "/Member/bug_tac"; // 购买策略

    public final static String ADD_RECHARGE_ORDER = "/Member/add_recharge"; // 生成充值订单
    //    public final static String TEACHER_TACTICS = "/Room/tactics"; // 老师房间策略列表
    public final static String TEACHER_ROOM_SUMMARY = "/Room/roomsummary"; // 获取房间简介
    public final static String ROOM_TACTICS = "/Room/tactics"; // 老师房间策略列表
    public final static String ROOM_VIP_PAGE = "/Room/gettvippage"; // 获取老师VIP会员介绍页面地址
    public final static String ROOM_ARICLE = "/Room/get_mjbw"; // 获取老师博文页面
    public final static String REGISTER_MEMRA = "/index/memra"; // 注册协议
    public final static String STOCK_SCHOOL = "/Stockmc/index"; // 股市学院
    public final static String FEED_BACK = "/Member/feedback"; // 意见反馈

    public final static String ANDREWS_VER = "/Index/andrews_ver"; // 自动更新的接口
    public final static String QQ_SL = "/Room/getqqbytid"; // 获取特约讲堂QQ号

    public final static String IM_DARA = "/Index/get_im_data"; // 获取房间历史聊天记录

    //    public final static String LL_NOTIFY_URL = BASE_URL + "/webllpay/notify_url.php"; // 连连支付的notify_url
    public final static String LL_NOTIFY_URL = BASE_URL + "/thirdpay/llpay_back"; // 连连支付的notify_url
    public final static String LECTURE_URL = "http://9626.hlsplay.aodianyun.com/98cfw/stream.m3u8"; // 奥点云 视频直播URL

    public final static String ACTIVITIS_URL = BASE_URL + "/Api/Index/tuned"; // 活动
    public final static String TRIPARTITE_LOGIN = "/User/tripartite_login";//第三方登陆的接口
    public final static String TRIPARTITE_BINDING = "/User/tripartite_binding";//第三方登陆绑定的接口
    public final static String TRIPARTITE_REG = "/User/tripartite_reg";//第三方登陆注册的接口


    public final static String QQ_URL = "mqqwpa://im/chat?chat_type=wpa&uin=";//跳转qq的action

    //------------------------- 行情接口---------------------------------
    public final static String SH_INDEX = "/Quotation/shindex"; // 获取上证指数
    public final static String SZ_INDEX = "/Quotation/szindex"; // 获取深圳成指
    public final static String CY_INDEX = "/Quotation/cybindex"; // 获取创业板指
    public final static String HS_INDEX = "/Quotation/hsindex"; // 获取恒生板指
    public final static String GQ_INDEX = "/Quotation/gqindex"; // 获取国企板指
    public final static String HC_INDEX = "/Quotation/hcindex"; // 获取红绸板指

    public final static String ADD_SELF_STOCK = "/Optionalg/add"; // 用户添加自选股票
    public final static String QUERY_SELF_STOCK = "/Optionalg/index"; // 拉取用户自选股票列表
    public final static String DEL_SELF_STOCK = "/Optionalg/delug"; // 用户删除自选股票
    public final static String ADD_SELF_STOCK_JSON = "/Optionalg/utbzxg"; // 用户同步自选股到服务器

    public final static String QUERY_SS = "/Tcfw/index"; // 沪深首页
    public final static String QUERY_ZFB_LIST = "/Tcfw/zfb_list"; // 涨幅榜列表
    public final static String QUERY_DFB_LIST = "/Tcfw/dfb_list"; // 跌幅榜列表
    public final static String QUERY_HSL_LIST = "/Tcfw/hsl_list"; // 换手率列表
    public final static String QUERY_ZF_LIST = "/Tcfw/zhenfu_list"; // 振幅榜列表
    public final static String QUERY_GJB_LIST = "/Tcfw/tic_price_list"; // 高价榜列表

    public final static String QUERY_HOT_HY = "/History/get_hot_hy"; // 热门行业列表
    public final static String QUERY_HOT_HY_LIST = "/History/get_hot_hylist"; // 某个热门行业下的股票列表
    public final static String QUERY_HOT_GN = "/History/get_hot_gn"; // 热门概念列表
    public final static String QUERY_HOT_GN_LIST = "/History/get_hot_gnlist"; // 某个热门概念下的股票列表

    public final static String QUERY_TIC_INFO = "/Tcfw/get_tic_info"; // 沪深股票股东，简况，财务信息
    public final static String QUERY_HK_TIC_INFO = "/Tcfw/get_hk_tic_info"; // 港股股东，简况信息

    public final static String GET_MIN_QUOTA = "/Tcfw/get_min_quota"; // 沪深分时图数据
    public final static String HS_DAY_QUOTA = "/Tcfw/hs_day_quota"; // 沪深日K线图数据
    public final static String HS_WEEK_QUOTA = "/Tcfw/hs_week_quota"; // 沪深周K线图数据
    public final static String HS_MONTH_QUOTA = "/Tcfw/hs_month_quota"; // 沪深月K线图数据

    public final static String GG_DAY_K = "/Hk/gg_day_k";//港股日K
    public final static String GET_WEEK_K = "/Hk/get_week_k";//港股周K
    public final static String GET_YUE_K = "/Hk/get_yue_k";//港股月K


    public final static String GET_MIN_ZS_QUOTA = "/Tcfw/get_min_zs_quota";//指数分时
    public final static String GET_SZ_DAY_K = "/Tcfw/get_sz_day_k";//上证日K
    public final static String GE_SZ_WEEK_K = "/Tcfw/ge_sz_week_k";//上证周K
    public final static String GET_SZ_YUE_K = "/Tcfw/get_sz_yue_k";//上证月K
    public final static String GET_CZ_DAY_K = "/Tcfw/get_cz_day_k";//深圳成指日K
    public final static String GE_SZCZ_WEEK_K = "/Tcfw/ge_szcz_week_k";//深圳成指周K
    public final static String GET_SZCZ_YUE_K = "/Tcfw/get_szcz_yue_k";//深圳成指月K
    public final static String GET_CY_DAY_K = "/Tcfw/get_cy_day_k";//创业板指日K
    public final static String GE_CY_WEEK_K = "/Tcfw/ge_cy_week_k";//创业板指周K
    public final static String GET_CY_YUE_K = "/Tcfw/get_cy_yue_k";//创业板指月K
    public final static String GET_HS_DAY_K = "/Hk/get_hs_day_k";//恒生指数日K
    public final static String GE_HS_WEEK_K = "/Hk/ge_hs_week_k";//恒生指数周K
    public final static String GET_HS_YUE_K = "/Hk/get_hs_yue_k";//恒生指数月K
    public final static String GET_GQ_DAY_K = "/Hk/get_gq_day_k";//国企指数日K
    public final static String GE_GQ_WEEK_K = "/Hk/ge_gq_week_k";//国企指数周K
    public final static String GET_GQ_YUE_K = "/Hk/get_gq_yue_k";//国企指数月K
    public final static String GET_HC_DAY_K = "/Hk/get_hc_day_k";//红筹指数日K
    public final static String GE_HC_WEEK_K = "/Hk/ge_hc_week_k";//红筹指数周K
    public final static String GET_HC_YUE_K = "/Hk/get_hc_yue_k";//红筹指数月K


    public final static String QUERY_HK = "/Hk/index"; // 港股首页
    public final static String QUERY_MAIN_UPDOWN = "/Hk/main_updown_list"; // 港股主板涨跌幅|
    public final static String QUERY_MAIN_MONEY = "/Hk/main_money_list"; // 港股主板成交额
    public final static String QUERY_NEW_UPDOWN = "/Hk/new_updown_list"; // 港股创业板涨跌幅
    public final static String QUERY_NEW_MONEY = "/Hk/new_money_list"; // 港股创业板成交额

    public final static String QUERY_SH_DETAIL = "/Quotation/shdetail"; // 上证指数详情数据
    public final static String QUERY_SZ_DETAIL = "/Quotation/szdetail"; // 深圳成指，创业板指详情数据

    public final static String QUERY_HK_UPDOWN_HS = "/Hk/hs_updown_list"; // 恒生指数成份股
    public final static String QUERY_HK_UPDOWN_GQ = "/Hk/gq_updown_list"; // 国企指数成份股
    public final static String QUERY_HK_UPDOWN_HC = "/Hk/hc_updown_list"; // 红筹指数成份股

    public final static String QUERY_SH_CFG = "/Tcfw/get_index_list"; // 上证指数、深圳成指、创业板指成份股

    public final static String QUERY_HK_HS_DETAIL = "/Hk/hensheng"; // 恒生指数详情数据
    public final static String QUERY_HK_GQ_DETAIL = "/Hk/guoqi"; // 国企指数详情数据
    public final static String QUERY_HK_HC_DETAIL = "/Hk/hongchou"; // 红筹指数详情数据
    public final static String TIC_TOP_INFO = "/Tcfw/tic_top_info";//股票详情数据

    public final static String HK_NEWS_LIST = "/New/gg_new_list";//港股新闻公告列表
    public final static String HS_NEWS_LIST = "/New/hs_new_list";//沪深新闻公告列表

    public final static String SEARCH_STOCK = "/Search/index";//查询股票

    public final static String BUY_SELL_STOCK = "/analog/trade_stock";// 买入卖出股票
    public final static String QUERY_USER_POSITION = "/Virtual/get_user_position";// 模拟炒股用户资产页面中持股列表
    public final static String TRADE_STOCK = "/analog/do_trade_stock";// 提交买卖股票
    public final static String QUERY_HOLD = "/analog/hold_code"; // 成交明细--当前持仓
    public final static String QUERY_HOLD_LIST = "/analog/hold_code_list"; // 持仓列表页
    public final static String QUERY_IMITATE_FRY = "/Virtual/index"; // 模拟炒股用户首页
    public final static String QUERY_RECORD_LIST = "/analog/code_trade_list"; // 历史交易--历史成交
    public final static String QUERY_CHANGE_LIST = "/analog/change_list"; // 历史交易--成交明细 头部接口
    public final static String QUERY_CHANGE_CODE_LIST = "/analog/change_code_list"; // 历史交易--成交明细 列表接口
    public final static String QUERY_TA_HOME_MSG = "/Virtualmatch/user_head_info"; // 他人的主页
    public final static String QUERY_TA_HISTORY = "/Virtualmatch/trade_list"; // 他人主页交易记录
    public final static String ATTENTION_OTHER = "/analog/user_add_concern"; // 关注他人
    public final static String QUERY_CONTEST_HOME_LIST = "/Virtualmatch"; //模拟大赛首页
    public final static String QUERY_FY_RANKING = "/Virtualmatch/fy_ranking"; //风雨排行榜详情页
    public final static String QUERY_TRACK_RANKING = "/Virtualmatch/track_ranking"; //风雨排行榜详情页
    public final static String QUERY_HOT_TIC = "/Virtualmatch/hot_tic"; //风雨排行榜详情页

    public final static String QUERY_OPERATE_LIST = "/analog/opera_list"; // 他的主页操作明细
    public final static String QUERY_USER_SIGN = "/analog/user_sign"; // 用户签到页面
    public final static String USER_SIGN = "/analog/do_sign"; // 用户签到页面
    public final static String QUERY_HOT_WARREN_LIST = "/Virtualmatch/hot_warren"; // 首页 热门大股神

    public final static String VIR_REG = "/Virtualmatch/vir_reg"; // 注册

    public final static String MATCH_DYNAMIC = "/Virtualmatch/match_dynamic";//大赛动态页面
    public final static String QUERY_WEEK_RANKING_LIST = "/Virtualmatch/get_week_ranking_list";// 短线牛人列表

    public final static String SHARE_LOOK_DETAILS = "/Virtualmatch/share_look_details";//赠送积分

    public final static String SIGN_SHARE = "/Virtualmatch/sign_share";//赠送积分

    public final static String WX_PAY_SIGN = "/pay/sign"; // 微信统一下单
    public final static String WX_CHECK_ORDER = "/pay/check_order"; // 微信查询订单
    public final static String WX_CLOSE_ORDER = "/pay/close_order"; // 微信关闭订单

    public final static String APP_PAY_ALI = "/Apppay/pay";//ali pay

    public final static String UNSET_CONCERN = "/Analog/user_unset_concern"; // 取消关注


}
