package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/4/21 0021.
 * explain:
 */
public class UpdataVersionBean {


    /**
     * code : 2000
     * msg : success
     * data : {"version":"2.0","url":"http://192.168.1.213:8080/test/downLoad/2.0.apk"}
     */

    private String code;
    private String msg;
    /**
     * version : 2.0
     * url : http://192.168.1.213:8080/test/downLoad/2.0.apk
     */

    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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
}
