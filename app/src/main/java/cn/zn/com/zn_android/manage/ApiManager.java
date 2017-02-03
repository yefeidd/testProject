package cn.zn.com.zn_android.manage;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.model.bean.AskQuestionResultBean;
import cn.zn.com.zn_android.model.bean.BannerBean;
import cn.zn.com.zn_android.model.bean.BillBean;
import cn.zn.com.zn_android.model.bean.BillDetailBean;
import cn.zn.com.zn_android.model.bean.BuySellStockBean;
import cn.zn.com.zn_android.model.bean.CSQQBean;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.model.bean.CollectVideoArtBean;
import cn.zn.com.zn_android.model.bean.CompDetailBean;
import cn.zn.com.zn_android.model.bean.ConceptBean;
import cn.zn.com.zn_android.model.bean.ContestDynamicBean;
import cn.zn.com.zn_android.model.bean.ContestHomeListBean;
import cn.zn.com.zn_android.model.bean.ContributionBean;
import cn.zn.com.zn_android.model.bean.CoursesBean;
import cn.zn.com.zn_android.model.bean.DfbBean;
import cn.zn.com.zn_android.model.bean.DiagnoseUserIndexBean;
import cn.zn.com.zn_android.model.bean.DynamicExpertBean;
import cn.zn.com.zn_android.model.bean.EntrustBean;
import cn.zn.com.zn_android.model.bean.ExcellentAnswerBean;
import cn.zn.com.zn_android.model.bean.ExchangeRecordBean;
import cn.zn.com.zn_android.model.bean.FyRankingBean;
import cn.zn.com.zn_android.model.bean.GjgbBean;
import cn.zn.com.zn_android.model.bean.HKCompDetailBean;
import cn.zn.com.zn_android.model.bean.HKListBean;
import cn.zn.com.zn_android.model.bean.HKRankListBean;
import cn.zn.com.zn_android.model.bean.HotConceptBean;
import cn.zn.com.zn_android.model.bean.HotHyBean;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.bean.HotTicBean;
import cn.zn.com.zn_android.model.bean.HsDayKLineBean;
import cn.zn.com.zn_android.model.bean.HsWeekKLineBean;
import cn.zn.com.zn_android.model.bean.HslBean;
import cn.zn.com.zn_android.model.bean.ImitateFryBean;
import cn.zn.com.zn_android.model.bean.ImitateFryItemBean;
import cn.zn.com.zn_android.model.bean.IndexBean;
import cn.zn.com.zn_android.model.bean.LoginResultBean;
import cn.zn.com.zn_android.model.bean.MarketDetailBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.MyAskAnswerBean;
import cn.zn.com.zn_android.model.bean.MyOrderBean;
import cn.zn.com.zn_android.model.bean.MyQuestionBean;
import cn.zn.com.zn_android.model.bean.OnlineTeacherBean;
import cn.zn.com.zn_android.model.bean.OperateDetailEntity;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.OrderInfoBean;
import cn.zn.com.zn_android.model.bean.OrderResultBean;
import cn.zn.com.zn_android.model.bean.OtherHomeMsgBean;
import cn.zn.com.zn_android.model.bean.PayOrderBean;
import cn.zn.com.zn_android.model.bean.PaySignBean;
import cn.zn.com.zn_android.model.bean.PrivateMsgBean;
import cn.zn.com.zn_android.model.bean.QADetailBean;
import cn.zn.com.zn_android.model.bean.QuestionBean;
import cn.zn.com.zn_android.model.bean.QuestionDetailBean;
import cn.zn.com.zn_android.model.bean.RegularStockBean;
import cn.zn.com.zn_android.model.bean.RoomBean;
import cn.zn.com.zn_android.model.bean.RoomSummaryBean;
import cn.zn.com.zn_android.model.bean.RushAnswerBean;
import cn.zn.com.zn_android.model.bean.SSListBean;
import cn.zn.com.zn_android.model.bean.SelfSelectStockBean;
import cn.zn.com.zn_android.model.bean.ShCfgBean;
import cn.zn.com.zn_android.model.bean.ShSzDetailBean;
import cn.zn.com.zn_android.model.bean.ShareTicBean;
import cn.zn.com.zn_android.model.bean.SignInfoBean;
import cn.zn.com.zn_android.model.bean.StockNewsBean;
import cn.zn.com.zn_android.model.bean.StockRecordBean;
import cn.zn.com.zn_android.model.bean.TacticsBean;
import cn.zn.com.zn_android.model.bean.TrackRankingBean;
import cn.zn.com.zn_android.model.bean.TransDetailBean;
import cn.zn.com.zn_android.model.bean.TransDetailListBean;
import cn.zn.com.zn_android.model.bean.UserInfoBean;
import cn.zn.com.zn_android.model.bean.UserPrivateTalkBean;
import cn.zn.com.zn_android.model.bean.VIPInfoBean;
import cn.zn.com.zn_android.model.bean.VersionBean;
import cn.zn.com.zn_android.model.bean.VideoBean;
import cn.zn.com.zn_android.model.bean.VipStateBean;
import cn.zn.com.zn_android.model.bean.ZfbBean;
import cn.zn.com.zn_android.model.bean.ZflBean;
import cn.zn.com.zn_android.model.chartBean.MinutesBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by WJL on 2016/3/10 0010 10:19.
 */
public class ApiManager {
    private static final String endpoint = Constants_api.BASE_URL;
    private static final String BASE_PARAMS = "/clientAction.do?method=json&classes=appinterface&common=";
    private static final String BASE_PARAMS_GSON = "/clientAction.do?method=gson&classes=appinterface&common=";
    private static final String BASE_PARAMS_PERSONAL_GSON = "/clientAction.do?method=gson&classes=personalService&common=";
    private static final String BASE_PARAMS_HOME = "/m/HomebaseServer/";
    private static OkHttpClient okHttpClient;
    private static final int DEFAULT_TIMEOUT = 20; //超时
    private int[] certificates = {R.raw.tt};
    private String hosts[] = {"https//tt.zhengniu.net"};
    private Cache cache = null;
    private File httpCacheDirectory;
    private static Retrofit retrofit;


    //    private RestAdapter restAdapter;
    private ApiService service;
    private static ApiManager mApiManager;


    private ApiManager() {
    }

    /**
     * 获取单例
     */
    public static ApiManager getInstance() {
        if (mApiManager == null) {
            synchronized (ApiManager.class) {
                if (mApiManager == null) {
                    mApiManager = new ApiManager();
                }
            }
        }
        return mApiManager;
    }


    /***
     * 单例 全局使用一个实例
     *
     * @return
     */
    public ApiService getService() {

//        if (httpCacheDirectory == null) {
//            httpCacheDirectory = new File(context.getCacheDir(), "zjs-cache");
//        }
//        try {
//            if (cache == null) {
//                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
//            }
//        } catch (Exception e) {
//            Log.e("OKHttp", "Could not create http cache", e);
//        }
//

        if (service == null) {
//            SSLSocketFactory ss = HttpsFactory.getSSLSocketFactory(context, certificates);
//            HostnameVerifier verifier = HttpsFactory.getHostnameVerifier(hosts);
//            if (Constants_api.BASE_URL.startsWith("https:")) {
//                okHttpClient = new OkHttpClient.Builder()
//                        .socketFactory(ss)
//                        .hostnameVerifier(verifier)
//                        .cookieJar(new CookieManger(context))
//                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                        .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
////                 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
//                        .build();
//            } else {
            okHttpClient = new OkHttpClient.Builder()
//                    .cookieJar(new CookieManger(context))
                    .cookieJar(new CookieManger(RnApplication.getInstance()))
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
//                 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                    .build();
//            }
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants_api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            synchronized (ApiService.class) {
                if (service == null) {
                    service = retrofit.create(ApiService.class);
                }
            }
        }
        return service;
    }


//    /**
//     * 返回结果包转换为 List对象
//     */
//    public <T> List<T> processReturnValuePackage(ReturnValuePackage<T> returnValuePackage) {
//        ReturnValue<T> returnValue = returnValuePackage.getJsondata();
//        if (returnValue.getCode().equals(Constants.RET_CODE_SUCCESS)) {
//            List<T> list = returnValue.getList();
//            if (list == null) {
//                return Collections.EMPTY_LIST;
//            }
//            return list;
//        }
//        // 这里可以改成 返回null    异常即可捕捉
//        return null;
//    }

//    /**
//     * 返回结果包转换为 List对象
//     */
//    public <T> List<T> processReturnValue(ReturnValue<T> returnValue) {
//        if (returnValue.getCode().equals(Constants.RET_CODE_SUCCESS)) {
//            List<T> list = returnValue.getList();
//            if (list == null) {
//                return Collections.EMPTY_LIST;
//            }
//            return list;
//        }
//        // 这里可以改成 返回null    异常即可捕捉
//        return null;
//    }

    /**
     * 返回结果包转换为 List对象
     */
    public <T> List<T> processReturnListValue(ReturnListValue<T> returnListValue) {
        List<T> list = returnListValue.getData();
        if (list != null) {
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public interface ApiService {

        /**
         * 登陆
         *
         * @param phone    手机号
         * @param password 密码
         * @param platform 安卓：2，IOS：3
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.LOGIN_URL)
        Observable<ReturnValue<LoginResultBean>> login(@Field("phone") String phone,
                                                 @Field("password") String password,
                                                 @Field("platform") String platform);

        /**
         * 三方登陆绑定
         *
         * @param type     三方登陆的类型
         * @param ucode    唯一识别码
         * @param phone    手机号码
         * @param password 密码
         */
        @FormUrlEncoded
        @POST(Constants_api.TRIPARTITE_BINDING)
        Observable<ReturnValue<LoginResultBean>> bindLogin(@Field("type") int type,
                                                     @Field("ucode") String ucode,
                                                     @Field("phone") String phone,
                                                     @Field("password") String password);

        /**
         * 找回密码，发送验证码
         *
         * @param phone 手机号
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SEND_SMS_FIND_PW)
        Observable<ReturnValue<MessageBean>> findPWSendSms(@Field("phone") String phone);

        /**
         * 发送注册验证码
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SEND_REGIST_CODE)
        Observable<ReturnValue<MessageBean>> sendResCode(@Field("phone") String phone);

        /**
         * 提交注册信息
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SEND_REGISTER_INFO)
        Observable<ReturnValue<MessageBean>> sendResInfo(@Field("phone") String phone,
                                                         @Field("nickname") String nickname,
                                                         @Field("password") String password,
                                                         @Field("code") String code,
                                                         @Field("platform") String platform,
                                                         @Field("channel") int channel);

        /**
         * 第三方注册绑定
         *
         * @param type     第三方登陆类型
         * @param ucode    唯一识别码
         * @param phone    手机号
         * @param password 密码
         * @param code     验证码
         * @param platform 收集类型
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.TRIPARTITE_REG)
        Observable<ReturnValue<MessageBean>> bindRegister(@Field("type") int type,
                                                          @Field("ucode") String ucode,
                                                          @Field("phone") String phone,
                                                          @Field("password") String password,
                                                          @Field("code") String code,
                                                          @Field("platform") String platform,
                                                          @Field("channel") int channel);

        /**
         * 找回密码
         *
         * @param phone  手机号
         * @param code   验证码
         * @param xpass  密码
         * @param cxpass 确认密码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.FIND_PW)
        Observable<ReturnValue<MessageBean>> findPW(@Field("phone") String phone,
                                                    @Field("code") String code,
                                                    @Field("xpass") String xpass,
                                                    @Field("cxpass") String cxpass);

        /**
         * 修改密码
         *
         * @param sessID php sessionID
         * @param ypass  原密码
         * @param xpass  密码
         * @param cxpass 确认密码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MODIFY_PW)
        Observable<ReturnValue<MessageBean>> modifyPW(@Header(Constants.COOKIE) String sessID,
                                                      @Field("ypass") String ypass,
                                                      @Field("xpass") String xpass,
                                                      @Field("cxpass") String cxpass);

        /**
         * 获取会员信息
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_MEMBER_INFO)
        Observable<ReturnValue<UserInfoBean>> queryMemberInfo(@Header(Constants.COOKIE) String cookie,
                                                              @Field("") String str);

        /**
         * 修改昵称
         *
         * @param sessID
         * @param nickname
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MODIFY_MEMBER_INFO)
        Observable<ReturnValue<MessageBean>> modifyMemberNick(@Header(Constants.COOKIE) String sessID,
                                                              @Field("nickname") String nickname);

        /**
         * 修改头像
         *
         * @param sessID
         * @param avatars
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MODIFY_MEMBER_INFO)
        Observable<ReturnValue<MessageBean>> modifyMemberAvatars(@Header(Constants.COOKIE) String sessID,
                                                                 @Field("avatars") String avatars);

        /**
         * 修改个性签名
         *
         * @param sessID
         * @param signature
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MODIFY_MEMBER_INFO)
        Observable<ReturnValue<MessageBean>> modifyMemberSignature(@Header(Constants.COOKIE) String sessID,
                                                                   @Field("signature") String signature);

        /**
         * 修改出生日期
         *
         * @param sessID
         * @param birthday
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MODIFY_MEMBER_INFO)
        Observable<ReturnValue<MessageBean>> modifyMemberBirthday(@Header(Constants.COOKIE) String sessID,
                                                                  @Field("birthday") String birthday);

        /**
         * 修改性别
         *
         * @param sessID
         * @param sex
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MODIFY_MEMBER_INFO)
        Observable<ReturnValue<MessageBean>> modifyMemberSex(@Header(Constants.COOKIE) String sessID,
                                                             @Field("sex") String sex);

        /**
         * 修改个性签名
         *
         * @param sessID
         * @param province 省ID
         * @param city     市ID
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MODIFY_MEMBER_INFO)
        Observable<ReturnValue<MessageBean>> modifyMemberLocation(@Header(Constants.COOKIE) String sessID,
                                                                  @Field("province") String province,
                                                                  @Field("city") String city);

        /**
         * 获取七牛 Token
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.UPLOAD_TOKEN)
        Observable<ReturnValue<MessageBean>> getUpLoadToken(@Field("") String str);

        /**
         * 发送消息到服务器
         * vipfd(1:VIP聊天,0:普通[默认0，特约讲堂此参数为空])
         *
         * @param sessID
         * @param type     1:发文字,2:送礼物
         * @param tid      老师id,默认9898
         * @param msg      文字内容
         * @param vipfd
         * @param giftid   礼物ID
         * @param giftnum  礼物数量
         * @param platform 安卓：2，IOS：3
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SEND_IM_MSG)
        Observable<ReturnValue<MessageBean>> sendMsg(@Header(Constants.COOKIE) String sessID,
                                                     @Field("type") String type,
                                                     @Field("tid") String tid,
                                                     @Field("msg") String msg,
                                                     @Field("vipfd") String vipfd,
                                                     @Field("giftid") String giftid,
                                                     @Field("giftnum") String giftnum,
                                                     @Field("platform") String platform);

        /**
         * 请求广告位图片信息
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.LOAD_BANER_INFO)
        Observable<ReturnListValue<BannerBean>> getHomeBanner(@Field("") String str);

        /**
         * 获取热门直播列表
         *
         * @param kwords
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_HOT_LIVE_INFO)
        Observable<ReturnListValue<HotLiveBean>> getHotLiveList(@Field("kwords") String kwords);

        /**
         * 请求退出登陆
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.EXIT_LOGIN)
        Observable<ReturnValue<MessageBean>> postExitLogin(@Header(Constants.COOKIE) String sessID,
                                                           @Field("") String str);

        /**
         * 获取特约讲堂悄悄话列表
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_PRIVATE_MSG)
        Observable<ReturnListValue<PrivateMsgBean>> getPrivateMsg(@Header(Constants.COOKIE) String sessID,
                                                                  @Field("tid") String tid);

        /**
         * 请求发送悄悄话
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SEND_PRIVATE_MSG)
        Observable<ReturnValue<MessageBean>> postPrivateMsg(@Header(Constants.COOKIE) String sessID,
                                                            @Field("msg") String msg,
                                                            @Field("tid") String tid);

        /**
         * 获取贡献榜
         */
        @FormUrlEncoded
        @POST(Constants_api.CONTRIBUTION)
        Observable<ReturnValue<ContributionBean>> getContribution(@Field("tid") String tid);

        /**
         * 获取是否vip
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_VIP_STATE)
        Observable<ReturnValue<VipStateBean>> getVipState(@Header(Constants.COOKIE) String sessID,
                                                          @Field("tid") String tid);

        /**
         * 获取视频列表
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.VIDEO_LIST)
        Observable<ReturnListValue<VideoBean>> queryVideoList(@Field("") String str);

        /**
         * VIP会员介绍
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.VIP_MEMBER_INSTRU)
        Observable<ReturnValue<VIPInfoBean>> queryVipMemberIntru(@Header(Constants.COOKIE) String sessID,
                                                                 @Field("") String str);

        /**
         * 获取课程表
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_COURSE)
        Observable<ReturnValue<CoursesBean>> queryCourse(@Field("") String str);

        /**
         * 获取特约讲堂公告
         *
         * @param tid
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_NOTICE)
        Observable<ReturnValue<CoursesBean>> queryNotice(@Field("tid") String tid);

        /**
         * 名家博文/资讯列表(总站)
         *
         * @param kwords 搜索关键字
         * @param type   1:资讯2:名家博文
         * @param order  1:热度2:时间
         * @param page   页码，默认1)
         * @param pcount 每页显示数量
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ARTICLE)
        Observable<ReturnListValue<ArticleBean>> getArticleList(@Field("kwords") String kwords,
                                                                @Field("type") String type,
                                                                @Field("order") String order,
                                                                @Field("page") String page,
                                                                @Field("pcount") String pcount);

        /**
         * 获取老师的博文列表
         *
         * @param tid    老师id
         * @param page   页码
         * @param pcount 每页的数量
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ROOM_ARICLE)
        Observable<ReturnListValue<ArticleBean>> getTeacherArticle(@Field("tid") String tid,
                                                                   @Field("page") String page,
                                                                   @Field("pcount") String pcount);

        /**
         * 会员收藏列表(文章，视频)
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_COLLECT_LIST)
        Observable<ReturnValue<CollectVideoArtBean>> queryCollectList(@Header(Constants.COOKIE) String sessID,
                                                                      @Field("") String str);

        /**
         * 会员关注列表(房间)
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_COLLECT_ROOM_LIST)
        Observable<ReturnListValue<RoomBean>> queryFocusList(@Header(Constants.COOKIE) String sessID,
                                                             @Field("") String str);

        /**
         * 增加文章点击量
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ART_CLICK)
        Observable<ReturnValue<MessageBean>> postArtClick(@Field("art_id") String art_id);

        /**
         * 增加文章点赞量
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ART_LIKES)
        Observable<ReturnValue<MessageBean>> postArtLikes(@Field("art_id") String art_id);

        /**
         * 增加文章收藏量
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ART_COLLECT)
        Observable<ReturnValue<MessageBean>> postArtCollect(@Header(Constants.COOKIE) String sessID,
                                                            @Field("art_id") String art_id);

        /**
         * 增加视频收藏量
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ADD_VIDEO_COLLECT)
        Observable<ReturnValue<MessageBean>> postVedioCollect(@Header(Constants.COOKIE) String sessID,
                                                              @Field("vid") String vid);

        /**
         * 判断文章是否被我收藏过
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.IS_COLLECT_ART)
        Observable<ReturnValue<MessageBean>> postArtISCollect(@Header(Constants.COOKIE) String sessID,
                                                              @Field("art_id") String art_id);

        /**
         * 会员购买策略列表
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.TACTICS_LIST)
        Observable<ReturnListValue<TacticsBean>> queryUserTacticsList(@Header(Constants.COOKIE) String sessID,
                                                                      @Field("") String str);

        /**
         * 会员购买策略列表
         *
         * @param tid
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ROOM_TACTICS)
        Observable<ReturnListValue<TacticsBean>> queryRoomTacticsList(@Field("tid") String tid);

        /**
         * 获取会员的悄悄话列表
         *
         * @param tid
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_USER_PRIVATE_MSG)
        Observable<ReturnListValue<UserPrivateTalkBean>> queryUserprivateTalkList(@Header(Constants.COOKIE) String sessID,
                                                                                  @Field("page") String page,
                                                                                  @Field("pcount") String pcount,
                                                                                  @Field("tid") String tid);

        /**
         * 房间是否被关注过
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.IS_COLLECT_ROOM)
        Observable<ReturnValue<MessageBean>> isCollectRoom(@Header(Constants.COOKIE) String sessID,
                                                           @Field("tid") String tid);

        /**
         * 增加房间收藏量
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ADD_ROOM_COLLECT)
        Observable<ReturnValue<MessageBean>> addRoomCollect(@Header(Constants.COOKIE) String sessID,
                                                            @Field("tid") String tid);

        /**
         * 老师房间策略列表
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ROOM_TACTICS)
        Observable<ReturnValue<MessageBean>> queryTeacherTactics(@Field("tid") String tid);

        /**
         * 获取房间简介
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.TEACHER_ROOM_SUMMARY)
        Observable<ReturnValue<RoomSummaryBean>> queryRoomSummary(@Field("tid") String tid);

        /**
         * 生成充值订单
         *
         * @param sessID
         * @param type    (1:微信2:连连)
         * @param money
         * @param orderqz IOS:i,安卓:a)
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ADD_RECHARGE_ORDER)
        Observable<ReturnValue<OrderResultBean>> addRechargeOrder(@Header(Constants.COOKIE) String sessID,
                                                                  @Field("type") String type,
                                                                  @Field("money") String money,
                                                                  @Field("orderqz") String orderqz);

        /**
         * 获取房间简介
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ROOM_VIP_PAGE)
        Observable<ReturnValue<MessageBean>> queryRoomVipPage(@Field("tid") String tid);

        /**
         * 获取房间简介
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.STOCK_SCHOOL)
        Observable<ReturnValue<CollectVideoArtBean>> queryStockSchool(@Field("page") int page,
                                                                      @Field("pcount") int pcount);

        /**
         * 购买策略
         *
         * @param tac_id   策略ID
         * @param platform 安卓：2，IOS：3
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.BUY_TACTICS)
        Observable<ReturnValue<MessageBean>> buyTactics(@Header(Constants.COOKIE) String sessID,
                                                        @Field("tac_id") String tac_id,
                                                        @Field("platform") String platform);

        /**
         * 意见反馈
         *
         * @param sessID
         * @param content 意见内容
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.FEED_BACK)
        Observable<ReturnValue<MessageBean>> feedback(@Header(Constants.COOKIE) String sessID,
                                                      @Field("content") String content);

        /**
         * 第三方登陆的接口
         */
        @FormUrlEncoded
        @POST(Constants_api.TRIPARTITE_LOGIN)
        Observable<ReturnValue<LoginResultBean>> tripartiteLogin(@Field("type") int type, @Field("ucode") String ucode);

        /**
         * 获取特约讲堂QQ号
         *
         * @param tid tid(老师id,默认9898)
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QQ_SL)
        Observable<ReturnValue<CSQQBean>> queryQQByTid(@Field("tid") String tid);

        /**
         * 获取房间历史聊天记录
         *
         * @param tid   老师id,默认9898
         * @param isvip 0:普通房间1:VIP房间,默认0
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.IM_DARA)
        Observable<ReturnListValue<ChatMsgBean>> queryImData(@Field("tid") String tid, @Field("isvip") String isvip);

        /**
         * 会员删除关注(房间)
         *
         * @param sessionId
         * @param tid
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.CANCEL_ROOM)
        Observable<ReturnValue<MessageBean>> cancelRoom(@Header(Constants.COOKIE) String sessionId,
                                                        @Field("tid") String tid);

        /**
         * 获取上证指数
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SH_INDEX)
        Observable<ReturnValue<IndexBean>> queryShIndex(@Field("") String str);

        /**
         * 获取深圳成指
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SZ_INDEX)
        Observable<ReturnValue<IndexBean>> querySzIndex(@Field("") String str);

        /**
         * 获取创业板指
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.CY_INDEX)
        Observable<ReturnValue<IndexBean>> queryCyIndex(@Field("") String str);

        /**
         * 获取恒生指数
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.HS_INDEX)
        Observable<ReturnValue<IndexBean>> queryHsIndex(@Field("") String str);

        /**
         * 获取国企指数
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GQ_INDEX)
        Observable<ReturnValue<IndexBean>> queryGqIndex(@Field("") String str);

        /**
         * 获取红筹指数
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.HC_INDEX)
        Observable<ReturnValue<IndexBean>> queryHcIndex(@Field("") String str);

        /**
         * 用户添加自选股票
         * <p>
         *
         * @param ticker 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ADD_SELF_STOCK)
        Observable<ReturnValue<MessageBean>> addSelfStock(@Header(Constants.COOKIE) String sessionId,
                                                          @Field("ticker") String ticker);

        /**
         * 拉取用户自选股票列表
         * <p>
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_SELF_STOCK)
        Observable<ReturnListValue<SelfSelectStockBean>> querySelfStock(@Header(Constants.COOKIE) String sessionId, @Field("") String str);

        /**
         * 用户删除自选股票
         * <p>
         *
         * @param sessionId
         * @param id        列表中的ID
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.DEL_SELF_STOCK)
        Observable<ReturnValue<MessageBean>> delSelfStock(@Header(Constants.COOKIE) String sessionId, @Field("id") String id);

        /**
         * 用户同步自选股到服务器
         *
         * @param sessionId json数据，包括ticker(股票代码)、orders(排序)
         * @param zdata
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ADD_SELF_STOCK_JSON)
        Observable<ReturnValue<MessageBean>> addSelfStockJson(@Header(Constants.COOKIE) String sessionId, @Field("zdata") String zdata);

        /**
         * 沪深首页
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_SS)
        Observable<ReturnValue<SSListBean>> querySS(@Field("") String str);

        /**
         * 涨幅榜列表
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_ZFB_LIST)
        Observable<ReturnListValue<ZfbBean>> queryZfbList(@Field("") String str);

        /**
         * 跌幅榜列表
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_DFB_LIST)
        Observable<ReturnListValue<DfbBean>> queryDfbList(@Field("") String str);

        /**
         * 换手率列表
         * <p>
         *
         * @param type 倒序type = 1
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HSL_LIST)
        Observable<ReturnListValue<HslBean>> queryHslList(@Field("type") String type);

        /**
         * 振幅榜列表
         * <p>
         *
         * @param type 倒序type = 1
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_ZF_LIST)
        Observable<ReturnListValue<ZflBean>> queryZfList(@Field("type") String type);

        /**
         * 高价榜列表
         * <p>
         *
         * @param type 倒序type = 1
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_GJB_LIST)
        Observable<ReturnListValue<GjgbBean>> queryGjbList(@Field("type") String type);

        /**
         * 热门行业列表
         * <p>
         *
         * @param type @POST(Constants_api.type(type = 1正序,type = 2倒序))
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HOT_HY)
        Observable<ReturnListValue<HotConceptBean>> queryHotHy(@Field("type") String type);

        /**
         * 热门概念列表
         * <p>
         *
         * @param type @POST(Constants_api.type(type = 1正序,type = 2倒序))
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HOT_GN)
        Observable<ReturnListValue<HotConceptBean>> queryHotGn(@Field("type") String type);

        /**
         * 某个热门行业下的股票列表
         * <p>
         *
         * @param type @POST(Constants_api.type(type = 1正序,type = 2倒序))
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HOT_HY_LIST)
        Observable<ReturnListValue<HotHyBean>> queryHotHyList(@Field("code_id") String code_id,
                                                              @Field("type") String type);

        /**
         * 某个热门概念下的股票列表
         * <p>
         *
         * @param type @POST(Constants_api.type(type = 1正序,type = 2倒序))
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HOT_GN_LIST)
        Observable<ReturnValue<ConceptBean>> queryHotGnList(@Field("code_id") String code_id,
                                                            @Field("type") String type);

        /**
         * 沪深股票股东，简况，财务信息
         *
         * @param tic_code 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_TIC_INFO)
        Observable<ReturnValue<CompDetailBean>> queryTicInfo(@Field("tic_code") String tic_code);

        /**
         * 沪深分时线图数据
         *
         * @param tic_code 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_MIN_QUOTA)
        Observable<ReturnListValue<MinutesBean>> queryHsMinQuota(@Field("tic_code") String tic_code);


        /**
         * 沪深日K线图数据
         *
         * @param tic_code   股票代码
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.HS_DAY_QUOTA)
        Observable<ReturnListValue<HsDayKLineBean>> queryHsDayQuota(@Field("tic_code") String tic_code,
                                                                    @Field("start_time") String start_time,
                                                                    @Field("end_time") String end_time);

        /**
         * 沪深周K线图数据
         *
         * @param tic_code   股票代码
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.HS_WEEK_QUOTA)
        Observable<ReturnListValue<HsWeekKLineBean>> queryHsWeekQuota(@Field("tic_code") String tic_code,
                                                                      @Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 沪深月K线图数据
         *
         * @param tic_code   股票代码
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.HS_MONTH_QUOTA)
        Observable<ReturnListValue<HsWeekKLineBean>> queryHsMonthQuota(@Field("tic_code") String tic_code,
                                                                       @Field("start_time") String start_time,
                                                                       @Field("end_time") String end_time);

        /**
         * 港股日K线图数据
         *
         * @param tic_code   股票代码
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GG_DAY_K)
        Observable<ReturnListValue<HsDayKLineBean>> queryGGDayQuota(@Field("code") String tic_code,
                                                                    @Field("start_time") String start_time,
                                                                    @Field("end_time") String end_time);

        /**
         * 港股周K线图数据
         *
         * @param tic_code   股票代码
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_WEEK_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryGGWeekQuota(@Field("code") String tic_code,
                                                                      @Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 港股月K线图数据
         *
         * @param tic_code   股票代码
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_YUE_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryGGMonthQuota(@Field("code") String tic_code,
                                                                       @Field("start_time") String start_time,
                                                                       @Field("end_time") String end_time);

        /**
         * 上证日K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_SZ_DAY_K)
        Observable<ReturnListValue<HsDayKLineBean>> querySZDayQuota(@Field("start_time") String start_time,
                                                                    @Field("end_time") String end_time);

        /**
         * 上证周K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GE_SZ_WEEK_K)
        Observable<ReturnListValue<HsWeekKLineBean>> querySZWeekQuota(@Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 上证月K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_SZ_YUE_K)
        Observable<ReturnListValue<HsWeekKLineBean>> querySZMonthQuota(@Field("start_time") String start_time,
                                                                       @Field("end_time") String end_time);

        /**
         * 深证日K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_CZ_DAY_K)
        Observable<ReturnListValue<HsDayKLineBean>> querySZCZDayQuota(@Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 深证周K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GE_SZCZ_WEEK_K)
        Observable<ReturnListValue<HsWeekKLineBean>> querySZCZWeekQuota(@Field("start_time") String start_time,
                                                                        @Field("end_time") String end_time);

        /**
         * 深证月K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_SZCZ_YUE_K)
        Observable<ReturnListValue<HsWeekKLineBean>> querySZCZMonthQuota(@Field("start_time") String start_time,
                                                                         @Field("end_time") String end_time);

        /**
         * 创业板日K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_CY_DAY_K)
        Observable<ReturnListValue<HsDayKLineBean>> queryCYDayQuota(@Field("start_time") String start_time,
                                                                    @Field("end_time") String end_time);

        /**
         * 创业板周K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GE_CY_WEEK_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryCYWeekQuota(@Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 创业板月K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_CY_YUE_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryCYMonthQuota(@Field("start_time") String start_time,
                                                                       @Field("end_time") String end_time);

        /**
         * 恒生日K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_HS_DAY_K)
        Observable<ReturnListValue<HsDayKLineBean>> queryHSDayQuota(@Field("start_time") String start_time,
                                                                    @Field("end_time") String end_time);

        /**
         * 恒生周K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GE_HS_WEEK_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryHSWeekQuota(@Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 恒生月K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_HS_YUE_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryHSMonthQuota(@Field("start_time") String start_time,
                                                                       @Field("end_time") String end_time);

        /**
         * 国企日K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_GQ_DAY_K)
        Observable<ReturnListValue<HsDayKLineBean>> queryGQDayQuota(@Field("start_time") String start_time,
                                                                    @Field("end_time") String end_time);

        /**
         * 国企周K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GE_GQ_WEEK_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryGQWeekQuota(@Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 国企月K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_GQ_YUE_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryGQMonthQuota(@Field("start_time") String start_time,
                                                                       @Field("end_time") String end_time);

        /**
         * 红筹日K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_HC_DAY_K)
        Observable<ReturnListValue<HsDayKLineBean>> queryHCDayQuota(@Field("start_time") String start_time,
                                                                    @Field("end_time") String end_time);

        /**
         * 红筹周K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GE_HC_WEEK_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryHCWeekQuota(@Field("start_time") String start_time,
                                                                      @Field("end_time") String end_time);

        /**
         * 红筹月K线图数据
         *
         * @param start_time 开始时间,格式：YYYYMMDD
         * @param end_time   结束时间,格式：YYYYMMDD
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_HC_YUE_K)
        Observable<ReturnListValue<HsWeekKLineBean>> queryHCMonthQuota(@Field("start_time") String start_time,
                                                                       @Field("end_time") String end_time);

        /**
         * 指数分时线图数据
         *
         * @param zs_type 指数类型
         *                1: 上证
         *                2：深证
         *                3：创业板
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.GET_MIN_ZS_QUOTA)
        Observable<ReturnListValue<MinutesBean>> queryZSMinQuota(@Field("zs_type") String zs_type);

        /**
         * 港股首页
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK)
        Observable<ReturnValue<HKListBean>> queryHK(@Field("") String str);

        /**
         * 港股创业板涨跌幅
         * <p>
         *
         * @param type (type = 1正序,type = 2倒序)
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_NEW_UPDOWN)
        Observable<ReturnListValue<HKRankListBean>> queryNewUpdown(@Field("type") String type);

        /**
         * 港股创业板成交额
         * <p>
         *
         * @param type (type = 3正序,type = 4倒序)
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_NEW_MONEY)
        Observable<ReturnListValue<HKRankListBean>> queryNewMoney(@Field("type") String type);

        /**
         * 港股主板涨跌幅
         * <p>
         *
         * @param type (type = 1正序,type = 2倒序)
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_MAIN_UPDOWN)
        Observable<ReturnListValue<HKRankListBean>> queryMainUpdown(@Field("type") String type);

        /**
         * 港股主板成交额
         * <p>
         *
         * @param type (type = 3正序,type = 4倒序)
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_MAIN_MONEY)
        Observable<ReturnListValue<HKRankListBean>> queryMainMoney(@Field("type") String type);

        /**
         * 上证指数详情数据
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_SH_DETAIL)
        Observable<ReturnValue<ShSzDetailBean>> queryShDetail(@Field("") String str);

        /**
         * 深圳成指，创业板指详情数据
         * <p>
         *
         * @param type type=1创业板指，type=0深圳成指
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_SZ_DETAIL)
        Observable<ReturnValue<ShSzDetailBean>> querySzDetail(@Field("type") String type);

        /**
         * 恒生指数成份股
         * <p>
         *
         * @param type 1为成分股涨幅2为成分股跌幅3成分股成交额
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK_UPDOWN_HS)
        Observable<ReturnListValue<HKRankListBean>> queryHkUpdownHs(@Field("type") String type);

        /**
         * 国企指数成份股
         * <p>
         *
         * @param type 1为成分股涨幅2为成分股跌幅3成分股成交额
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK_UPDOWN_GQ)
        Observable<ReturnListValue<HKRankListBean>> queryHkUpdownGq(@Field("type") String type);

        /**
         * 红筹指数成份股
         * <p>
         *
         * @param type 1为成分股涨幅2为成分股跌幅3成分股成交额
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK_UPDOWN_HC)
        Observable<ReturnListValue<HKRankListBean>> queryHkUpdownHc(@Field("type") String type);

        /**
         * 上证指数、深圳成指、创业板指成份股
         * <p>
         *
         * @param type (type = 1上证，type = 2深圳，type = 3创业板)
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_SH_CFG)
        Observable<ReturnValue<ShCfgBean>> queryShCFG(@Field("type") String type);

        /**
         * 恒生指数详情数据
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK_HS_DETAIL)
        Observable<ReturnValue<ShSzDetailBean>> queryHkHsDetail(@Field("") String str);

        /**
         * 国企指数详情数据
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK_GQ_DETAIL)
        Observable<ReturnValue<ShSzDetailBean>> queryHkGqDetail(@Field("") String str);

        /**
         * 红筹指数详情数据
         * <p>
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK_HC_DETAIL)
        Observable<ReturnValue<ShSzDetailBean>> queryHkHcDetail(@Field("") String str);

        /**
         * 查询股票行情
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.TIC_TOP_INFO)
        Observable<ReturnValue<MarketDetailBean>> queryMarketDetail(@Header(Constants.COOKIE) String sessionId,
                                                                    @Field("tic_code") String str);

        /**
         * 港股新闻公告列表
         *
         * @param type 1新闻，2公告
         * @param code 股票代码
         * @param page 分页
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.HK_NEWS_LIST)
        Observable<ReturnListValue<StockNewsBean>> queryHkNewsList(@Field("type") String type,
                                                                   @Field("code") String code,
                                                                   @Field("page") String page,
                                                                   @Field("page_size") String pageSize);

        /**
         * 沪深新闻公告列表
         *
         * @param type 1新闻，2公告
         * @param code 股票代码
         * @param page 分页
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.HS_NEWS_LIST)
        Observable<ReturnListValue<StockNewsBean>> queryHsNewsList(@Field("type") String type,
                                                                   @Field("code") String code,
                                                                   @Field("page") String page,
                                                                   @Field("page_size") String pageSize);

        /**
         * 查询股票
         *
         * @param code 股票代码，股票拼音简称
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SEARCH_STOCK)
        Observable<ReturnListValue<OptionalStockBean>> searchStock(@Field("code") String code);

        /**
         * 搜索股票
         *
         * @param code 股票代码，拼音，中文
         * @param type 值为1显示5条，值为空10条
         * @param len  值为1,只显示沪深股票，为空都显示
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SEARCH_STOCK)
        Observable<ReturnListValue<OptionalStockBean>> searchStock(@Field("code") String code,
                                                                   @Field("type") String type,
                                                                   @Field("len") String len);

        /**
         * 沪深股票股东，简况，财务信息
         *
         * @param tic_code 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HK_TIC_INFO)
        Observable<ReturnValue<HKCompDetailBean>> queryHkTicInfo(@Field("tic_code") String tic_code);

        /**
         * 买入卖出股票
         *
         * @param code_id 股票代码
         * @param type    1:买入2:卖出
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.BUY_SELL_STOCK)
        Observable<ReturnValue<BuySellStockBean>> buySellStock(@Header(Constants.COOKIE) String sessionId,
                                                               @Field("code_id") String code_id, @Field("type") String type);

        /**
         * 模拟炒股用户资产页面中持股列表
         *
         * @param page 从0开始
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_USER_POSITION)
        Observable<ReturnListValue<ImitateFryItemBean>> queryUserPosition(@Header(Constants.COOKIE) String sessionId,
                                                                          @Field("page") String page);

        /**
         * 买入卖出股票
         *
         * @param code_id    股票代码
         * @param code_num
         * @param type       1:买入2:卖出
         * @param code_name
         * @param code_price 希望买入的价格
         * @param now        1:委托买卖  2:市价买入
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.TRADE_STOCK)
        Observable<ReturnValue<MessageBean>> tradeStock(@Header(Constants.COOKIE) String sessionId,
                                                        @Field("code_id") String code_id,
                                                        @Field("code_num") String code_num,
                                                        @Field("type") String type,
                                                        @Field("code_name") String code_name,
                                                        @Field("code_price") String code_price,
                                                        @Field("now") String now);

        /**
         * 成交明细--当前持仓2
         *
         * @param code_id 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HOLD)
        Observable<ReturnValue<TransDetailBean>> queryHold(@Header(Constants.COOKIE) String sessionId,
                                                           @Field("code_id") String code_id);

        /**
         * 成交明细--当前持仓2
         *
         * @param code_id 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HOLD_LIST)
        Observable<ReturnListValue<TransDetailListBean>> queryHoldList(@Header(Constants.COOKIE) String sessionId,
                                                                       @Field("code_id") String code_id,
                                                                       @Field("page") int page,
                                                                       @Field("page_size") int page_size);

        /**
         * 模拟炒股用户首页
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_IMITATE_FRY)
        Observable<ReturnValue<ImitateFryBean>> queryImitateFry(@Header(Constants.COOKIE) String sessionId,
                                                                @Field("") String str);

        /**
         * 成交明细--当前持仓2
         *
         * @param page
         * @param page_size
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_RECORD_LIST)
        Observable<ReturnValue<StockRecordBean>> queryRecordList(@Header(Constants.COOKIE) String sessionId,
                                                                 @Field("page") int page,
                                                                 @Field("page_size") int page_size);

        /**
         * 历史交易--成交明细 头部接口
         *
         * @param code_id 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_CHANGE_LIST)
        Observable<ReturnValue<TransDetailBean>> queryChangeList(@Header(Constants.COOKIE) String sessionId,
                                                                 @Field("code_id") String code_id);

        /**
         * 成交明细--当前持仓2
         *
         * @param code_id 股票代码
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_CHANGE_CODE_LIST)
        Observable<ReturnListValue<TransDetailListBean>> queryChangeCodeList(@Header(Constants.COOKIE) String sessionId,
                                                                             @Field("code_id") String code_id,
                                                                             @Field("page") int page,
                                                                             @Field("page_size") int page_size);

        /**
         * 他人主页
         *
         * @param user_id 用户ID
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_TA_HOME_MSG)
        Observable<ReturnValue<OtherHomeMsgBean>> queryOtherMsg(@Header(Constants.COOKIE) String sessionId,
                                                                @Field("user_id") String user_id);

        /**
         * 他人主页的历史
         *
         * @param user_id 用户ID
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_TA_HISTORY)
        Observable<ReturnListValue<ExchangeRecordBean>> queryOtherHistory(@Field("user_id") String user_id,
                                                                          @Field("page") int page,
                                                                          @Field("page_size") int page_size);

        /**
         * 关注他人
         *
         * @param user_id 用户ID
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ATTENTION_OTHER)
        Observable<ReturnValue<MessageBean>> attentionOther(@Header(Constants.COOKIE) String sessionId, @Field("user_id") String user_id);

        /**
         * 模拟大赛首页
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_CONTEST_HOME_LIST)
        Observable<ReturnValue<ContestHomeListBean>> queryContestHomeList(@Field("") String id, @Header(Constants.COOKIE) String sessID);

        /**
         * 风云排行榜(同最赚钱牛人)
         *
         * @param page（页数，从0开始）， num 返回数量
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_FY_RANKING)
        Observable<ReturnListValue<FyRankingBean>> queryFyRanking(@Header(Constants.COOKIE) String sessionId,
                                                                  @Field("page") String page, @Field("num") String num);

        /**
         * 模拟跟踪收益榜
         *
         * @param page（页数，从0开始）， num 返回数量
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_TRACK_RANKING)
        Observable<ReturnListValue<TrackRankingBean>> queryTrankRanking(@Header(Constants.COOKIE) String sessionId,
                                                                        @Field("page") String page,
                                                                        @Field("num") String num);

        /**
         * 热门股票列表
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_HOT_TIC)
        Observable<ReturnListValue<HotTicBean>> queryHotTic(@Field("") String str);

        /**
         * 他的主页操作明细
         *
         * @param user_id 用户ID
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_OPERATE_LIST)
        Observable<ReturnValue<OperateDetailEntity>> queryOperateList(@Header(Constants.COOKIE) String sessionId,
                                                                      @Field("user_id") String user_id,
                                                                      @Field("page") int page);

        /**
         * 用户签到页面
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_USER_SIGN)
        Observable<ReturnValue<SignInfoBean>> queryUserSign(@Header(Constants.COOKIE) String sessionId, @Field("") String str);

        /**
         * 用户签到页面
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.USER_SIGN)
        Observable<ReturnValue<MessageBean>> userSign(@Header(Constants.COOKIE) String sessionId, @Field("") String str);

        /**
         * 模拟炒股注册接口
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.VIR_REG)
        Observable<ReturnValue<MessageBean>> virReg(@Header(Constants.COOKIE) String sessionId, @Field("") String str);

        /**
         * 报名赠送积分
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SIGN_SHARE)
        Observable<ReturnValue<MessageBean>> presentSignScore(@Header(Constants.COOKIE) String sessionId, @Field("") String str);

        /**
         * 普通赠送积分
         *
         * @param str
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.SHARE_LOOK_DETAILS)
        Observable<ReturnValue<MessageBean>> presentScore(@Header(Constants.COOKIE) String sessionId, @Field("") String str);

        /**
         * 大赛动态
         *
         * @param type
         * @param page
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.MATCH_DYNAMIC)
        Observable<ReturnListValue<ContestDynamicBean>> queryContestDynamicList(@Field("type") String type,
                                                                                @Field("page") String page);

        /**
         * 短线牛人列表
         *
         * @param str 短线牛人列表
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_WEEK_RANKING_LIST)
        Observable<ReturnListValue<DynamicExpertBean>> queryWeekRankingList(@Header(Constants.COOKIE) String sessionId,
                                                                            @Field("") String str);

        /**
         * 获取微信支付信息
         *
         * @param money
         * @param name
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.WX_PAY_SIGN)
        Observable<ReturnValue<PaySignBean>> queryWXPaySign(@Header(Constants.COOKIE) String sessionId,
                                                            @Field("money") String money,
                                                            @Field("name") String name);

        /**
         * 微信查询订单
         *
         * @param out_trade_no
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.WX_CHECK_ORDER)
        Observable<ReturnValue<MessageBean>> queryWXCheckOrder(@Field("out_trade_no") String out_trade_no);

        /**
         * 微信关闭订单
         *
         * @param out_trade_no
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.WX_CLOSE_ORDER)
        Observable<ReturnValue<MessageBean>> queryWXCloseOrder(@Field("out_trade_no") String out_trade_no);

        /**
         * 支付宝
         */
        @FormUrlEncoded
        @POST(Constants_api.APP_PAY_ALI)
        Observable<ReturnValue<MessageBean>> addDateTo(@Header(Constants.COOKIE) String sessionId,
                                                       @Field("money") String money,
                                                       @Field("nu_order_num") String number);

        /**
         * 取消关注
         */
        @FormUrlEncoded
        @POST(Constants_api.UNSET_CONCERN)
        Observable<ReturnValue<MessageBean>> unsetConcern(@Header(Constants.COOKIE) String sessionId,
                                                          @Field("user_id") String user_id);

        /**
         * 委托列表
         *
         * @param sessionId
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_ENTRUST_LIST)
        Observable<ReturnListValue<EntrustBean>> queryEntrusList(@Header(Constants.COOKIE) String sessionId,
                                                                 @Field("") String str);

        /**
         * 撤销委托
         *
         * @param sessionId
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.REMOVE_ENTRUST)
        Observable<ReturnValue<MessageBean>> removeEnstuct(@Header(Constants.COOKIE) String sessionId,
                                                           @Field("id") String id);

        /**
         * 牛人动态/最赚钱牛人
         */

        @FormUrlEncoded
        @POST(Constants_api.HOME_USER_RANK)
        Observable<ReturnListValue<DynamicExpertBean>> queryMoneyStockGenius(@Header(Constants.COOKIE) String sessionId,
                                                                             @Field("page") int page,
                                                                             @Field("num") int num);

        /**
         * 短线牛人
         */

        @FormUrlEncoded
        @POST(Constants_api.HOME_WEEK_RANK)
        Observable<ReturnListValue<DynamicExpertBean>> querySortStockGenius(@Header(Constants.COOKIE) String sessionId,
                                                                            @Field("page") int page,
                                                                            @Field("num") int num);

        /**
         * 人气牛人
         */

        @FormUrlEncoded
        @POST(Constants_api.HOME_HOT_RANK)
        Observable<ReturnListValue<DynamicExpertBean>> queryHotStockGenius(@Header(Constants.COOKIE) String sessionId,
                                                                           @Field("page") int page,
                                                                           @Field("num") int num);

        /**
         * 诊股提问题
         *
         * @param ptype        1:快速问股2:指定老师问股
         * @param niubi        (证牛币)
         * @param scode        股票代码
         * @param sname        股票名称
         * @param scosts       成本价
         * @param pdescription 问题描述
         * @param form         1：安卓 2：IOS
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ASK_QUESTION)
        Observable<ReturnValue<AskQuestionResultBean>> askQuestion(@Header(Constants.COOKIE) String sessionId,
                                                                   @Field("tid") String tid,
                                                                   @Field("ptype") String ptype,
                                                                   @Field("niubi") String niubi,
                                                                   @Field("scode") String scode,
                                                                   @Field("sname") String sname,
                                                                   @Field("scosts") String scosts,
                                                                   @Field("pdescription") String pdescription,
                                                                   @Field("form") String form);

        /**
         * 下订单后获取订单信息
         *
         * @param order_num 订单号
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_ORDER_INFO)
        Observable<ReturnValue<OrderInfoBean>> queryOrderInfo(@Header(Constants.COOKIE) String sessionId,
                                                              @Field("order_num") String order_num);

        /**
         * 订单支付
         *
         * @param sessionId
         * @param order_num 订单号
         * @param zg_tic    诊股券id
         * @param platform
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.PAY_ORDER)
        Observable<ReturnValue<PayOrderBean>> payOrder(@Header(Constants.COOKIE) String sessionId,
                                                       @Field("order_num") String order_num,
                                                       @Field("zg_tic") String zg_tic,
                                                       @Field("platform") String platform);

        /**
         * 查询问题列表
         *
         * @param sessionId
         * @param type
         * @param stype
         * @param page
         * @param num
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.TEACHER_QUESTION_LIST)
        Observable<ReturnListValue<QuestionBean>> queryTeacherQuestionList(@Header(Constants.COOKIE) String sessionId,
                                                                           @Field("type") int type,
                                                                           @Field("stype") int stype,
                                                                           @Field("page") int page,
                                                                           @Field("num") int num);

        /**
         * 抢答接口
         *
         * @param sessionId
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.RUSH_ANSWER)
        Observable<ReturnValue<RushAnswerBean>> rushAnswer(@Header(Constants.COOKIE) String sessionId,
                                                           @Field("id") String id);

        /**
         * 问题详情
         *
         * @param sessionId
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_ASK_INFO)
        Observable<ReturnListValue<QuestionDetailBean>> queryAskInfo(@Header(Constants.COOKIE) String sessionId,
                                                                     @Field("id") String id);

        @FormUrlEncoded
        @POST(Constants_api.TEACHER_ANSWER)
        Observable<ReturnValue<QuestionDetailBean>> queryTeacherAnswer(@Header(Constants.COOKIE) String sessionId,
                                                                       @Field("id") String id);

        @FormUrlEncoded
        @POST(Constants_api.POST_ANSWER)
        Observable<ReturnValue<MessageBean>> commitTeacherAnswer(@Header(Constants.COOKIE) String sessionId,
                                                                 @Field("strend") int strend,
                                                                 @Field("mtrend") int mtrend,
                                                                 @Field("tanalysis") String tanalysis,
                                                                 @Field("fanalysis") String fanalysis,
                                                                 @Field("supplement") String supplement,
                                                                 @Field("img1") String img1,
                                                                 @Field("img2") String img2,
                                                                 @Field("img3") String img3,
                                                                 @Field("id") String id,
                                                                 @Field("from") String from);

        /**
         * 首页精彩回答列表
         *
         * @param sessionId
         * @param page
         * @param num
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.EXCELLENT_ANSWER_LIST)
        Observable<ReturnListValue<ExcellentAnswerBean>> excellentAnswerList(@Header(Constants.COOKIE) String sessionId,
                                                                             @Field("page") String page,
                                                                             @Field("num") String num);

        /**
         * 老师房间精彩回答列表
         *
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.ROOM_JC_ANSWER)
        Observable<ReturnListValue<ExcellentAnswerBean>> excellentRoomAnswerList(@Header(Constants.COOKIE) String sessionId,
                                                                                 @Field("tid") String tid);

        /**
         * 诊股大厅用户首页接口
         */
        @FormUrlEncoded
        @POST(Constants_api.DIAGNOSE_INDEX)
        Observable<ReturnValue<DiagnoseUserIndexBean>> diagnoseIndex(@Field("") String str);

        /**
         * 精英投顾排行榜
         *
         * @param type 1:综合排序2:数量排序3:好评排序
         * @param sort 1为升序2位降序
         * @param page 从0开始
         * @param num  一页数量
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.DIAGNOSE_TEACHER_LIST)
        Observable<ReturnListValue<OnlineTeacherBean>> queryDiagnoseTeacherList(@Field("type") String type,
                                                                                @Field("sort") String sort,
                                                                                @Field("page") String page,
                                                                                @Field("num") String num);

        /**
         * 我的订单
         *
         * @param type 1全部 2待支付的
         * @param page 从0开始
         * @param num  一页数量
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_MY_ORDER)
        Observable<ReturnListValue<MyOrderBean>> queryMyOrderList(@Header(Constants.COOKIE) String sessionId,
                                                                  @Field("type") String type,
                                                                  @Field("page") String page,
                                                                  @Field("num") String num);

        /**
         * 问答详情页面
         *
         * @param sessionId
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QA_DETAIL)
        Observable<ReturnValue<QADetailBean>> queryQADetail(@Header(Constants.COOKIE) String sessionId,
                                                            @Field("id") String id);

        /**
         * 问股答复
         *
         * @param sessionId
         * @param page
         * @param num
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QA_ASK_ANSWER)
        Observable<ReturnListValue<MyAskAnswerBean>> queryAskAnswerList(@Header(Constants.COOKIE) String sessionId,
                                                                        @Field("page") String page,
                                                                        @Field("num") String num);

        /**
         * 提交评价
         *
         * @param sessionId
         * @param id
         * @param score     星级
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.POST_EVALUATE)
        Observable<ReturnValue<MessageBean>> postEvaluate(@Header(Constants.COOKIE) String sessionId,
                                                          @Field("id") String id,
                                                          @Field("score") String score);

        /**
         * 我的提问
         *
         * @param sessionId
         * @param page
         * @param num
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_MY_ASK)
        Observable<ReturnListValue<MyQuestionBean>> queryMyAskList(@Header(Constants.COOKIE) String sessionId,
                                                                   @Field("page") int page,
                                                                   @Field("num") int num);

        /**
         * 取消精彩
         */
        @FormUrlEncoded
        @POST(Constants_api.ANSWER_ADD_GOOD)
        Observable<ReturnValue<MessageBean>> addGoodAnswer(@Header(Constants.COOKIE) String sessionId,
                                                           @Field("id") String id);

        /**
         * 设为精彩
         */
        @FormUrlEncoded
        @POST(Constants_api.ANSWER_DEL_GOOD)
        Observable<ReturnValue<MessageBean>> delGoodAnswer(@Header(Constants.COOKIE) String sessionId,
                                                           @Field("id") String id);

        /**
         * 删除问题
         */
        @FormUrlEncoded
        @POST(Constants_api.ANSWER_DEL_ASK)
        Observable<ReturnValue<MessageBean>> delAnswer(@Header(Constants.COOKIE) String sessionId,
                                                       @Field("id") String id);

        /**
         * 查询账单列表
         *
         * @param sessionId
         * @param page
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.USER_BILL)
        Observable<ReturnListValue<BillBean>> queryUserBillList(@Header(Constants.COOKIE) String sessionId,
                                                                @Field("page") int page);

        /**
         * 查询账单详情
         */
        @FormUrlEncoded
        @POST(Constants_api.BILL_INFO)
        Observable<ReturnValue<BillDetailBean>> queryBillDeatail(@Header(Constants.COOKIE) String sessionId,
                                                                 @Field("id") String id);

        /**
         * 匹配股票名+代码格式是否正确
         */
        @FormUrlEncoded
        @POST(Constants_api.REGULAR_STOCK)
        Observable<ReturnValue<RegularStockBean>> regularStock(@Field("tic_tit") String tic_tit);

        /**
         * 个人中心诊股卷列表
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_SHARE_TIC_LIST)
        Observable<ReturnListValue<ShareTicBean>> queryShareTicList(@Header(Constants.COOKIE) String sessionId,
                                                                    @Field("") String str);

        /**
         * 老师直播间信息
         *
         * @param tid
         * @return
         */
        @FormUrlEncoded
        @POST(Constants_api.QUERY_TEACHER_INFO)
        Observable<ReturnValue<HotLiveBean>> queryTeacherInfo(@Field("tid") String tid);

        /**
         *
         * @param str
         * @return
         */
        @GET(Constants_api.ANDREWS_VER)
        Observable<ReturnValue<VersionBean>> queryAndroidVer(@Query("") String str);

    }
}
