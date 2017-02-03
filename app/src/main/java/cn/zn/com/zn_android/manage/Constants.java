package cn.zn.com.zn_android.manage;

import cn.zn.com.zn_android.R;

/**
 * 常量类
 * Created by WJL on 2016/3/10 0010 09:57.
 */
public class Constants {
    /**
     * 原app spf文件名
     */
    public static final String SPF_NAME_APP = "ocheckinfo";         // 账户相关
    public static final String SPF_NAME_APP_CONFIG = "app_config";  // app设置相关

    /**
     * 登录相关
     */
    public static final String SPF_KEY_IS_LOGIN = "isLogin"; // 是否登录 1：登录

    public static final String SPF_KEY_FIRST = "isfirst";   // 第一次登陆
    public static final String SPF_KEY_PHONE = "phone"; // 登录手机号
    public static final String SPF_KEY_LOGINNAME = "loginname";
    public static final String SPF_KEY_PWD = "passWord"; // 登录密码
    public static final String SPF_KEY_NAME = "name";
    public static final String SPF_KEY_IMG_HEAD = "img";

    public static final String SPF_KEY_REFRESH_DT = "dtRefreshRate"; // 流量数据下行情刷新频率 = tDRefreshRate * 5
    public static final String SPF_KEY_REFRESH_WIFI = "wifiRefreshRate"; // WiFi下行情刷新频率 = wifiRefreshRate * 5

    /**
     * 及时通讯相关
     */
    public static final String PUB_KEY = "pub_1b7d4cd545f644cc19632d747c474c30"; // 登录密码
    public static final String SUB_KEY = "sub_17ff0d4026df07b908af61608e5fead6";
    public static final String TOPIC_HEAD = "t";

    // 更新app文件的存放文件名
    public static final String FILE_CACHE_IMG = "rn_img";
    // SD卡目录
    public static final String FILE_APP = "/rn";
    // SD卡目录
    public static final String FILE_APP_IMG = "/rn";
    // SD卡临时文件
    public static final String FILE_APP_TEMP = "/temp";

    /**
     * 请求成功返回
     */
    public static final String RET_CODE_SUCCESS = "0000";

    /**
     * 调用接口，返回数据成功标志
     */
    public static final String SUCCESS = "success";
    /**
     * 调用接口，返回数据失败标志
     */
    public static final String ERROR = "error";

    /**
     * 从哪个页面来
     */
    public static final String FROM = "from";

    /**
     * 找回密码
     */
    public static final String FIND = "findPW";

    /**
     * 注册
     */
    public static final String REGISTER = "register";

    /**
     * SET_COOKIE
     */
    public static final String SET_COOKIE = "Set-Cookie";

    /**
     * COOKIE
     */
    public static final String COOKIE = "Cookie";

    /**
     * 所在地 数据库 id字段名
     */
    public static final String ID = "id";
    /**
     * 所在地数据库 area字段名
     */
    public static final String AREA = "area";

    /**
     * 所在地 数据库 文件名
     */
    public static final String DB_FILE = "address.db";

    /**
     * 是否登陆状态
     */
    public static final int IS_LOGIN = 1;

    public static final int NOT_LOGIN = 0;

    /**
     * 是否是博文或者资讯
     */
    public static final String VIDEO = "3";
    public static final String ARTICLE = "2";
    public static final String NEWS = "1";
    /**
     * 设备信息
     */
    public static final String ANDROID = "2";
    public static final String IMG = "img";
    public static final String TITLE = "title";
    public static final String INFO = "info";
    public static final String TIME = "time";
    public static final String URL = "url";
    public static final String SELECT = "select";


    /**
     * 是否是vip
     */
    public static final String IS_VIP = "1";
    public static final String NOT_VIP = "0";
    public static final String NOT_LOGIN_VIP = "2";

    public static final String DEFAULT_TID = "9898";

    /**
     * 第三方登陆类型
     */
    public static final int QQ = 1;
    public static final int WEI_XING = 2;
    public static final int WEI_BO = 3;

    public static final String ROLE_TEACHER = "老师";

    /**
     * 连连  常量
     */
    /*********************************************************************************/
    public static final int BASE_ID = 0;
    public static final int RQF_PAY = BASE_ID + 1;
    public static final int RQF_INSTALL_CHECK = RQF_PAY + 1;
    public static final String SERVER_URL = "http://yintong.com.cn/secure_server/x.htm";
    public static final String PAY_PACKAGE = "com.yintong.secure";
    // 银通支付安全支付服务apk的名称，必须与assets目录下的apk名称一致
    public static final String YT_PLUGIN_NAME = "SecurePay.apk";


    public static final String RET_CODE_PROCESS = "2008";// 2008 支付处理中
    public static final String RESULT_PAY_SUCCESS = "SUCCESS";
    public static final String RESULT_PAY_PROCESSING = "PROCESSING";
    public static final String RESULT_PAY_FAILURE = "FAILURE";
    public static final String RESULT_PAY_REFUND = "REFUND";

    public static final String LL_RECHARGE_NAME = "证牛财富币充值";
    public static final String LL_BUSI_PARTNER = "101001";
    /*********************************************************************************/

    public static final String ROLE_DEFAULT = "小白";


    /**
     * 股票交易所类型
     */
    public static final int HK = 1;
    public static final int SH = 0;

    /**
     * 通联数据  token
     */
    public static final String STOCK_TOKEN = "Bearer 9ccfd8d179f9b65650f3311488c0637147965c2d7c8e9b52e0f2911f3c0afb1d";


    /**
     * K线图类型
     */
    public enum kLineType {
        DAY, WEEK, MONTH
    }

    /**
     * 分时图类型
     */
    public enum MinutesType {
        HS, INDEX
    }

    /**
     * 指数类型
     */
    public static final String[] indexType = {"SZ", "SZCZ", "CY", "HS", "GQ", "HC"};

    /**
     * 指数类型
     * 1: 上证
     * 2：深证
     * 3：创业板
     */
    public static final int[] zs_type = {1, 2, 3};

    /**
     * 分享内容
     */
    public static final int iconResourece = R.drawable.share_icon;

    //个股分享
    public static final String marketShareTitle = "这儿的个股很妖啊";
    public static final String marketShareContent = "当一只股票成为牛股的时候，其实已经不牛了！牛股的最大价值在于成为牛股之前，你能够逮捕它，然后关起来，养熟！重要的不是找，而是找到！来这里，证牛APP,让一切牛股无所遁形!";
    public static final String marketShareUrl = Constants_api.BASE_URL + "/index/appdown.html";

    //老师分享
    public static final String teacherShareTitle = "每个人都有绝活儿，慧眼识股，妙手擒牛！";
    public static final String teacherShareContent = "这里拒绝任何专家，这里清一色的全是实战大伽。我们没时间玩理论，我们“心黑手辣”，我们“眼毒刀狠”，我们“老谋深算”，变的是涨来跌去的大盘，不变的是红或更红的算盘。";
    public static final String teacherShareUrl = Constants_api.BASE_URL + "/index/appdown.html";

    //他人主页分享
    public static final String taShareTitle = "运筹一臂之力，谋划百桶之金，谋在我，决在您！";
    public static final String taShareContent = "在这个震荡无休，调整无止的时间，请不要抱以侥幸，并相信运气。肯定的是他们存在，不肯定的是他们为你存在。我在这里等你，为您谋划，为您布局，为您在大盘里杀出一席之地。";
    public static final String taShareUrl = Constants_api.BASE_URL + "/index/appdown.html";
    //特约讲堂分享
    public static final String specificShareTitle = "对于大盘，我们不是解读，而是解剖！";
    public static final String specificShareContent = "这里的要求更高，不止同步直播，不仅讲解思索！结合所有消息面，掰碎了、揉成末、然后一点一滴地推理并还原。这里是证牛，别开生面的讲堂，别具一格的依据，别出心裁的解析。";
    public static final String specificShareUrl = Constants_api.BASE_URL + "/index/appdown.html";
    //视频页面分享
    public static final String vedioShareTitle = "实战高手的30分钟/这视频的破坏力太大......";
    public static final String vedioShareContent = "不仅分享，更要收藏。满满的都是宝贵经验，真真的都是肺腑之谈。基本常识、炒股技巧、选股策略、风险规避，行情参照，滴水观海，落叶知秋，每一遍都有触动，每一遍都有收获。";
    public static final String vedioShareUrl = Constants_api.BASE_URL + "/index/appdown.html";
    //文章页面分享
    public static final String articleShareTitle = "每篇文章里都有1千万，不铺陈，只精选！";
    public static final String articleShareContent = "含金量，是我们刊发或转载资讯的唯一标准。我们坚信，值钱的永远都不是股票，而是股市里第一手的信息！每一个政策或信息的背后，都有可能藏有3只或者5只你所迫切需要的牛股。";
    public static final String articleShareUrl = Constants_api.BASE_URL + "/index/appdown.html";
    //模拟炒股页面分享
    public static final String simulateShareTitle = "百万现金馈高手，宝马豪车赠股神";
    public static final String simulateShareContent = "机构高手？民间高手？百战老将？新晋小散？英雄不问出处！";
    public static final String simulateShareUrl = Constants_api.BASE_URL + "/index/shareregister.html";
    //行情指数页面分享
    public static final String indexShareTitle = "一步到位，一目了然";
    public static final String indexShareContent = "个股望闻问切，大盘剥茧抽丝,行情解剖评测，策略技巧布局，尽在证牛APP";
    public static final String indexShareUrl = Constants_api.BASE_URL + "/index/appdown.html";
    //报名页面分享
    public static final String signUpShareTitle = "决战股坛，畅赢大奖！草根股民开启翻身之日！";
    public static final String signUpShareContent = "证牛炒股大奖赛火爆开启！大赛云集海量高手，更有百万现金奖励、千万实盘资金、宝马豪车座驾等着你！参赛还是观战任你选！";
    public static final String signUpShareUrl = Constants_api.BASE_URL + "/index/shareregister.html";


    public static final int SDK_PAY_FLAG = BASE_ID + 2;

    public static final String[] channels = {"zhengniu","yingyongbao","baidu","_360","oppo","vivo","huawei","wandoujia","xiaomi","meizu","yingyongbaovip","baiduvip1","_360vip","oppovip","baiduvip2"};

    public static final int IS_BACK = 1;
    public static final int COMMENT_UN = 2;

}
