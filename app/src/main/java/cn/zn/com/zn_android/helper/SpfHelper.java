package cn.zn.com.zn_android.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WJL on 2016/3/10 0010 09:45.
 */
public class SpfHelper {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    
    public SpfHelper(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 删除全部数据
     */
    public void deleteAll() {
        editor.clear();
        editor.commit();
    }

    /**
     * 删除某一条
     * @param key
     */
    public void delete(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 保存
     */
    public void saveData(String key, String data) {
        editor.putString(key, data);
        editor.commit();
    }

    /**
     * 保存
     */
    public void saveIntData(String key, int data) {
        editor.putInt(key, data);
        editor.commit();
    }

    /**
     * 保存 不提交
     */
    public void saveDataNoCommit(String key, String data) {
        editor.putString(key, data);
    }

    /**
     * 提交
     */
    public void commit() {
        editor.commit();
    }

    /**
     * 获取
     */
    public String getData(String key) {
        return preferences.getString(key, "");
    }

    /**
     * 获取
     */
    public int getIntData(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * 获取 带默认
     */
    public String getData(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    /**
     * 包含
     */
    public boolean contains(String key){
        return preferences.contains(key);
    }

    /**
     * Editor
     */
    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
