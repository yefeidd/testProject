package cn.zn.com.zn_android.model.bean;

/**
 * 新闻公告
 * Created by Jolly on 2016/8/29 0029.
 */
public class StockNewsBean {

    /**
     * id : 1045711
     * name : 2014年4月份销售及近期新增项目情况简报
     * time : 2014-05-05 19:22:46
     */

    private String id;
    private String name;
    private long time;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
