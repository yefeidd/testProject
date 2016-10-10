package cn.zn.com.zn_android.model.bean;

/**
 * Created by yefeidd on 2016/2/26.
 */
public class AnyEventType {
    Object obj;
    Boolean state;
    Integer type;
    String tid;

    public String getTid() {
        return tid;
    }

    public AnyEventType setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public AnyEventType() {
        super();
    }

    public AnyEventType(Object obj) {
        this.obj = obj;
    }

    public Boolean getState() {
        return state;
    }

    public AnyEventType setState(Boolean state) {
        this.state = state;
        return this;
    }

    public AnyEventType setType(int type) {
        this.type = type;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public Object getObject() {
        return obj;
    }

    public AnyEventType setObject(Object obj) {
        this.obj = obj;
        return this;
    }

}