package cn.zn.com.zn_android.manage;

import android.content.Context;
import android.util.Log;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zjs on 2017/1/16 0016.
 * email: m15267280642@163.com
 * explain: 自定义的Cookie的管理器
 */

public class CookieManger implements CookieJar {
    public static String APP_PLATFORM = "app-platform";
    private static final String TAG = "CookieManger";
    private static Context mContext;

    private static PersistentCookieStore cookieStore;

    public CookieManger(Context context) {
        mContext = context;
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(mContext);
        }

    }

    /**
     * 返回cookieStore
     *
     * @return
     */
    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    /**
     * 把从cookie转化为String类型
     *
     * @param cookies
     * @return
     */
    public static String formatCookie(List<Cookie> cookies) {
        StringBuilder s = new StringBuilder();
        Log.i(TAG, "formatCookie: " + cookies.toString());
        if (null == cookies || cookies.size() == 0) {
            return "";
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.toString().contains("PHPSESSID")) {
                    s.append(cookie.toString());
                }
            }
        }
        String[] strs = s.toString().split(";");
        StringBuilder sb = new StringBuilder();
        if (strs.length > 0) {
            sb.append(strs[0]).append(";").append("path=/;");
        }
        Log.i(TAG, "formatCookie: " + sb.toString());
        return sb.toString();
    }


    static class Customer {

        private String userID;
        private String token;

        public Customer(String userID, String token) {
            this.userID = userID;
            this.token = token;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


    }
}
