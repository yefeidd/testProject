package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/4/6 0006.
 */
public class CollectVideoArtBean {

    /**
     * id : 1
     * source : 98财富网
     * times : 2016-04-05 09:15:59
     * title : 标题111111
     * url : http://192.168.1.3:98/Api/Index/art?id=1
     */

    private List<ArticleBean> art;
    /**
     * id : 1
     * image : https://www.baidu.com/img/bd_logo1.png
     * times : 2016-04-05 09:15:59
     * title : 视频11111
     * url : http://192.168.1.3:98/Api/Index/video?id=1
     */

    private List<VideoBean> video;

    public List<ArticleBean> getArt() {
        return art;
    }

    public void setArt(List<ArticleBean> art) {
        this.art = art;
    }

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

}
