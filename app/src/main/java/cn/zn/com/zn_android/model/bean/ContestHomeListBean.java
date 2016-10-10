package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by zjs on 2016/9/24 0024.
 * email: m15267280642@163.com
 * explain:
 */

public class ContestHomeListBean {


    private String total_money;
    /**
     * id : 784
     * title : 1111
     */

    private List<ArticleBean> art_list;
    /**
     * link : http://www.zhengniu.net
     * url : http://qiniu.98cfw.com/2016-04-21_5718983b7f9f1.png
     */

    private List<BannerBean> banner;
    /**
     * nickname :
     * profit : 24.89
     * ranking : 1
     * totalmoney : 1248884.98
     * user_id : 10
     */

    private List<FyRankingBean> fy_ranking;
    /**
     * change_rage : 44.071
     * code_id : 600908
     * code_name : N锡银
     * price : 6.44
     */

    private List<HotTicBean> hot_tic;
    /**
     * attentionType : 0
     * avatars :
     * first_time : 1473823380
     * mon_income : 0.66
     * nickname :
     * profit : 24.89
     * user_id : 10
     * week_income : 24.89
     * win_rate : 3
     */

    private List<DynamicExpertBean> hot_warren;
    /**
     * attentionType : 0
     * nickname :
     * profit : 24.89
     * ranking : 1
     * totalmoney : 1248884.98
     * user_id : 10
     */

    private List<TrackRankingBean> track_ranking;
    /**
     * click_num : 13225
     * id : 1
     * image : http://qiniu.98cfw.com/2016-04-18_57149b65b2807.jpg
     * title : 视频11111...
     * video_link : http://tt.zhengniu.net/Api/Index/video?id=1
     */

    private List<VideoBean> video;

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public List<ArticleBean> getArt_list() {
        return art_list;
    }

    public void setArt_list(List<ArticleBean> art_list) {
        this.art_list = art_list;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<FyRankingBean> getFy_ranking() {
        return fy_ranking;
    }

    public void setFy_ranking(List<FyRankingBean> fy_ranking) {
        this.fy_ranking = fy_ranking;
    }

    public List<HotTicBean> getHot_tic() {
        return hot_tic;
    }

    public void setHot_tic(List<HotTicBean> hot_tic) {
        this.hot_tic = hot_tic;
    }

    public List<DynamicExpertBean> getHot_warren() {
        return hot_warren;
    }

    public void setHot_warren(List<DynamicExpertBean> hot_warren) {
        this.hot_warren = hot_warren;
    }

    public List<TrackRankingBean> getTrack_ranking() {
        return track_ranking;
    }

    public void setTrack_ranking(List<TrackRankingBean> track_ranking) {
        this.track_ranking = track_ranking;
    }

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }
}
