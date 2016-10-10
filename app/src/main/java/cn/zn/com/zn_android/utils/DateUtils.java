package cn.zn.com.zn_android.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间日期工具类
 * Created by Jolly on 2016/4/6 0006.
 */
public class DateUtils {
    /**
     * 按格式 日期转字符串
     *
     * @param style
     * @param date
     * @return
     */
    public static String dateToStr(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

    /**
     * 按格式 时间字符串转日期
     *
     * @param style
     * @param date
     * @return
     */
    public static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 改变时间的显示格式
     *
     * @param beforStyle
     * @param afterStyle
     * @param date
     * @return
     */
    public static String dateStrToDateStr(String beforStyle, String afterStyle, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(beforStyle);
        SimpleDateFormat afterFormatter = new SimpleDateFormat(afterStyle);

        try {
            return afterFormatter.format(formatter.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return new Date().toString();
        }
    }

    public static String getToday() {
        Date currentDate = strToDate("yyyy.MM.dd", System.currentTimeMillis() + "");
        return dateToStr("yyyy.MM.dd", currentDate);
    }

    public static String getToday(String style) {
        Date currentDate = strToDate(style, System.currentTimeMillis() + "");
        return dateToStr(style, currentDate);
    }

    /**
     * 按格式获取现在时间，日期，年份等
     *
     * @param style
     * @return
     */
    public static String getCurrentYearMonth(String style) {
        Date currentDate = strToDate(style, System.currentTimeMillis() + "");
        return dateToStr(style, currentDate);
    }

    /**
     * 获取当月总共天数
     *
     * @return
     */
    public static int getCurrentMonthDay() {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当天日期  day
     *
     * @return
     */
    public static int getCurrentDay() {
//        Calendar a = Calendar.getInstance();
//        a.set(Calendar.DATE, 1);
//        a.roll(Calendar.DATE, -1);
//        int day = a.get(Calendar.DAY_OF_MONTH) + 1;

        String currentDate = dateToStr("dd", new Date(System.currentTimeMillis()));
        return Integer.valueOf(currentDate);
    }

    public static int getCurrentHour() {
        String currentDate = dateToStr("HH", new Date(System.currentTimeMillis()));
        return Integer.valueOf(currentDate);
    }

    public static int getCurrentMinutes() {
        String currentDate = dateToStr("mm", new Date(System.currentTimeMillis()));
        return Integer.valueOf(currentDate);
    }

    /**
     * @return
     */
    public static String stockStatus() {
        String status = "待开盘";
        if (getCurrentHour() < 9 || (getCurrentHour() == 9 && getCurrentMinutes() < 30)) {
            status = "待开盘";
        } else if (getCurrentHour() == 9 && getCurrentMinutes() >= 30) {
            status = "交易中";
        } else if (getCurrentHour() > 9 && getCurrentHour() < 15) {
            status = "交易中";
        } else if (getCurrentHour() == 15 && getCurrentMinutes() <= 30) {
            status = "交易中";
        } else {
            status = "已收盘";
        }
        return status;
    }

    public static List<String> getLastWeekDate() {
        List<String> datas = new ArrayList<>();
        for (int i = 7; i > -1; i--) {
            Calendar a = Calendar.getInstance();
            a.add(Calendar.DAY_OF_MONTH, -i);
            datas.add(dateToStr("yyyy.MM.dd", a.getTime()));
        }
        return datas;
    }


    public static List<String> getLastWeekDateNoYear() {
        List<String> datas = new ArrayList<>();
        for (int i = 7; i > -1; i--) {
            Calendar a = Calendar.getInstance();
            a.add(Calendar.DAY_OF_MONTH, -i);
            datas.add(dateToStr("MM.dd", a.getTime()));
        }
        return datas;
    }

    public static List<String> getLastSevenMonthDate() {
        List<String> datas = new ArrayList<>();

        for (int i = 7; i > -1; i--) {
            Calendar a = Calendar.getInstance();
            a.add(Calendar.MONTH, -i);
            datas.add(dateToStr("yyyy-MM", a.getTime()));
        }
        return datas;
    }

    public static int getCurrentDayOfWeek() {
        Calendar a = Calendar.getInstance();
        a.setTime(new Date(System.currentTimeMillis()));
        int inweek = a.get(Calendar.DAY_OF_WEEK);
        return inweek;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 将长时间格式字符串 根据要求的格式 转换为时间
     */
    public static String getStringDate(Long date, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 将当前时间根据要求的格式 转换为时间
     */
    public static String getCurDate(String style) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        String dateString = formatter.format(date);
        return dateString;
    }
}
