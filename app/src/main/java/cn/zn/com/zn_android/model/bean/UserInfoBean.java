package cn.zn.com.zn_android.model.bean;

/**
 * 用户信息
 * <p>
 * Created by Jolly on 2016/3/17 0017.
 */
public class UserInfoBean extends MessageBean {
    /**
     * birthday : 0000-00-00
     * grade : 普通会员
     * mobile : 18255115883
     * nickname : RongNiu201603165883
     * province : 0
     * qq :
     * avatars:url
     * sex : 0
     * signature :
     */

    private String birthday; // 生日
    private String grade; // 会员等级
    private String avatars;//会员头像
    private String mobile; // 手机号
    private String nickname; // 昵称
    private String province; // 所在地
    private String city; // 城市
    private String qq; // qq
    private String sex; // 性别 0--》未知 ；1--》男 ；2--》女
    private String signature; // 个性签名
    private String wealth; // 财富
    /**
     * fen :
     */

    private String fen;
    private int ticket;

    /**
     * 该账号是否是老师账号
     * 1：是
     * 0：不是，学生账号
     */
    private int is_teacher;

    public int getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(int is_teacher) {
        this.is_teacher = is_teacher;
    }

    public int getTicket() {
        return ticket;
    }

    public UserInfoBean setTicket(int ticket) {
        this.ticket = ticket;
        return this;
    }

    public String getWealth() {
        return wealth;
    }

    public UserInfoBean setWealth(String wealth) {
        this.wealth = wealth;
        return this;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGrade() {
        return grade;
    }

    public String getMobile() {
        return mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProvince() {
        return province;
    }

    public String getQq() {
        return qq;
    }

    public String getSex() {
        return sex;
    }

    public String getSignature() {
        return signature;
    }

    public String getCity() {
        return city;
    }

    public UserInfoBean setCity(String city) {
        this.city = city;
        return this;
    }


    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }
}
