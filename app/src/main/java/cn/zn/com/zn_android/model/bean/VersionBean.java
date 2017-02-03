package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2017/1/19.
 */

public class VersionBean extends MessageBean {

    /**
     * version : 1.1.1
     * url : http://qiniu.98cfw.com/98cfw.apk
     */

    private String version;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
