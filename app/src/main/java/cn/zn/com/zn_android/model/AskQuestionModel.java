package cn.zn.com.zn_android.model;

/**
 * Created by Jolly on 2016/12/12.
 */

public class AskQuestionModel {
    /**
     * 1:快速问股2:指定老师问股
     */
    private String ptype;
    /**
     * 老师id
     */
    private String tid;
    /**
     * 证牛币
     */
    private String niubi;
    /**
     * 股票代码
     */
    private String scode;
    /**
     * 股票名称
     */
    private String sname;
    /**
     * 成本价
     */
    private String scosts;
    /**
     * 问题描述
     */
    private String pdescription;
    /**1：安卓 2：IOS
     *
     */
    private String form;

    /**
     * 诊股提问题
     *
     * @param ptype     1:快速问股2:指定老师问股
     * @param niubi     (证牛币)
     * @param scode     股票代码
     * @param sname     股票名称
     * @param scosts    成本价
     * @param pdescription  问题描述
     * @param form      1：安卓 2：IOS
     */
    public AskQuestionModel(String ptype,String tid, String niubi, String scode, String sname, String scosts, String pdescription, String form) {
        this.ptype = ptype;
        this.tid = tid;
        this.niubi = niubi;
        this.scode = scode;
        this.sname = sname;
        this.scosts = scosts;
        this.pdescription = pdescription;
        this.form = form;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getNiubi() {
        return niubi;
    }

    public void setNiubi(String niubi) {
        this.niubi = niubi;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getScosts() {
        return scosts;
    }

    public void setScosts(String scosts) {
        this.scosts = scosts;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
