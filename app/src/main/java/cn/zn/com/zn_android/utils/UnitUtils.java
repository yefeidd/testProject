package cn.zn.com.zn_android.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * author：ajiang
 * mail：1025065158@qq.com
 * blog：http://blog.csdn.net/qqyanjiang
 */
public class UnitUtils {
    /**
     * Prevent class instantiation.
     */
    private UnitUtils() {
    }

    public static String getVolUnit(float num) {

        int e = (int) Math.floor(Math.log10(num));

        if (e >= 8) {
            return "亿手";
        } else if (e >= 4) {
            return "万手";
        } else {
            return "手";
        }
    }

    public static String getVolUnit1(float num) {
        int e = (int) Math.floor(Math.log10(num));

        if (e >= 8) {
            return "亿";
        } else if (e >= 4) {
            return "万";
        }
        return "";
    }


    /**
     * @param num
     * @return
     */
    public static float getVol(float num) {
        int e = (int) Math.floor(Math.log10(num));
        float num1;
        if (e >= 8) {
            num1 = num / 100000000f;
        } else if (e >= 4) {
            num1 = num / 10000f;
        } else num1 = num;
        BigDecimal b = new BigDecimal(num1);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }


    /**
     * 求float数组中的最小值
     */
    public static float getMin(float... floats) {
        float minFloat = Float.MAX_VALUE;
        for (float val : floats) {
            if (minFloat > val) minFloat = val;
        }
        return minFloat;
    }

    public static float getMax(float... floats) {
        float maxFloat = Float.MIN_VALUE;
        for (float val : floats) {
            if (maxFloat < val) maxFloat = val;
        }
        return maxFloat;
    }

    /**
     * 取两位小数
     */
    public static String getTwoDecimal(float value) {
        DecimalFormat mFormat = new DecimalFormat("#0.00");
        String vol = mFormat.format(value);
        return vol;
    }

    public static String getPercentage(float value) {
        return new DecimalFormat("#0.00%").format(value);
    }


    public static String clacUnit(String str) {
        try {
            Float num = Float.valueOf(str);
            StringBuilder sb = new StringBuilder();
            String unit = UnitUtils.getVolUnit1(num);
            float vol = UnitUtils.getVol(num);
            sb.append(vol).append(unit);
            return sb.toString();
        } catch (Exception e) {
            return str;
        }
    }
}
