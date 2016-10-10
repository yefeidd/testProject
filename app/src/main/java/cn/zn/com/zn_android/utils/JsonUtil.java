package cn.zn.com.zn_android.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * json工具类
 * </p>
 */
@SuppressWarnings("rawtypes")
public class JsonUtil {

    /**
     * <p>
     * 把对象转为Json格式的字符串
     * </P>
     * <p>
     * 集合数据
     *
     * @return
     */
    public static String writeEntity2JSON(Object obj) {
        if (obj == null)
            return "{}";
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String writeEntity2KeyValueJSON(String key, Object obj,
                                                  long time) {
        Gson gson = new Gson();
        String result = "";
        if (time > 0) {
            result = "{\"" + key + "\":" + gson.toJson(obj)
                    + ",\"current_time\":" + time + "}";
        } else {
            result = "{\"" + key + "\":" + gson.toJson(obj) + "}";
        }
        return result;
    }

    public static String writeEntityList2JSONWithSuccess(Object obj) {
        Gson gson = new Gson();
        return "{\"array\":" + gson.toJson(obj) + ",\"success\":true}";
    }

    public static String writeEntityList2JSON(Object obj) {
        Gson gson = new Gson();
        return "{\"array\":" + gson.toJson(obj) + "}";
    }

    public static String writeEntityList2JSON(Object obj, long time) {
        Gson gson = new Gson();
        return "{\"array\":" + gson.toJson(obj) + ",\"current_time\":" + time
                + "}";
    }

    /**
     * 将对象中注解为@Expose的属性转成JSON串
     *
     * @param obj
     * @return
     */
    public static String writeEntityOrListExcludeFields2JSON(Object obj) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(obj);
    }

    /**
     * 将json转换成对应的实体类对象
     *
     * @param json
     * @param objEntity
     * @return
     */
    public static <T extends Object> T fromJSON2Entity(String json,
                                                       Class<T> objEntity) {
        Gson gson = new Gson();
        return gson.fromJson(json, objEntity);
    }

    /**
     * 将json转换成对应的实体类对象数组列表
     *
     * @param json   JSON格式的数据
     * @param tClass 实体类字节码文件对象
     * @return
     */
    public static <T extends Object> List<T> fromJSON2EntityList(String json, Class<T> tClass) {
        Gson gson = new Gson();
        List list = gson.fromJson(json, List.class);
        if (list != null && list.size() > 0) {
            List<T> result = new ArrayList<T>();
            for (Object object : list) { // "object instanceof LinkedHashMap" is true
                String entity = gson.toJson(object);
                T t = gson.fromJson(entity, tClass);
                result.add(t);
            }
            return result;
        }
        return null;
    }

}
