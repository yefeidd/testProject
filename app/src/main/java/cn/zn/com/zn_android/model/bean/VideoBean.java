package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/4/5 0005.
 */
public class VideoBean {
    /**
     * id : 2
     * tid : 9898
     * title : 视频2
     * image : https://www.baidu.com/img/bd_logo1.png
     * video_link :
     * sort : 99
     * remark : 视频简介视频简介视频简介视频简介视频简介视频简介视频简介
     * create_time : 1459489716
     * click_num : 1099
     * status : 1
     * operation : 0
     * stock : 0
     * url : http://192.168.1.3:98/Api/Index/video?id=2
     * likes : 10052
     */

    private String id; // 视频id
    private String tid; //
    private String title; // 视频标题
    private String image; // 视频图片URL
    private String video_link; // 视频link
    private String sort; //
    private String remark;
    private String create_time;
    private String click_num; // 点击次数
    private String status;
    private String operation;
    private String stock;
    private String url;
    private String times;
    private String likes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getClick_num() {
        return click_num;
    }

    public void setClick_num(String click_num) {
        this.click_num = click_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
//    {
//        id: "2",
//                tid: "9898",
//            title: "视频2",
//            image: "https://www.baidu.com/img/bd_logo1.png",
//            video_link: "",
//            sort: "99",
//            remark: "视频简介视频简介视频简介视频简介视频简介视频简介视频简介",
//            create_time: "1459489716",
//            click_num: "1099",
//            status: "1",
//            operation: "0",
//            stock: "0",
//            url: "http://192.168.1.3:98/Api/Index/video?id=2"
//    }

}
