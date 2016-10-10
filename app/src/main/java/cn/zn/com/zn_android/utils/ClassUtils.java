package cn.zn.com.zn_android.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zjs on 2016/8/29 0029.
 * email: m15267280642@163.com
 * explain:
 */
public class ClassUtils<T> {
    T model;

    public ClassUtils(T model) {
        this.model = model;
    }

    /**
     * 反射给对象赋值
     */
    public T initField(Class<T> clazz) {
        Field[] field = clazz.getDeclaredFields();
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = clazz.getMethod("get" + name);
                    String value = (String) m.invoke(model); // 调用getter方法获取属性值
                    if (null == value || "" == value) {
                        m = model.getClass().getMethod("set" + name, String.class);
                        m.invoke(model, "--");
                    }
                }
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
