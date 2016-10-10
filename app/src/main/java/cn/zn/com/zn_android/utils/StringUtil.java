package cn.zn.com.zn_android.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import cn.zn.com.zn_android.uiclass.gif.AnimatedGifDrawable;
import cn.zn.com.zn_android.uiclass.gif.AnimatedImageSpan;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil implements Serializable {

    private static final long serialVersionUID = 6023511038584610786L;

    private static final String EMPTY = "";

    private static final String EMPTY_FOR_HTML = "&nbsp;";

    private static final String TIP_BRACKET_LEFT = "<";

    private static final String TIP_BRACKET_RIGHT = ">";

    private static final String TIP_BRACKET_LEFT_HTML = "&lt;";

    private static final String TIP_BRACKET_RIGHT_HTML = "&gt;";

    public static String null2Empty(String str) {
        if (str == null) {
            return EMPTY;
        }
        return str;
    }

    public static String null2HtmlEmpty(String str) {
        if (str == null) {
            return EMPTY_FOR_HTML;
        }
        return str;
    }

    public static String convertString2Html(String str) {
        if (str == null)
            return EMPTY;

        str = str.replaceAll(TIP_BRACKET_LEFT_HTML, TIP_BRACKET_LEFT);
        str = str.replaceAll(TIP_BRACKET_RIGHT_HTML, TIP_BRACKET_RIGHT);

        return str;
    }

    /**
     * 字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean hasValue(String value) {
        return (value != null) && (value.trim().length() > 0);
    }

    /**
     * 支持数字
     *
     * @param value
     * @return
     */
    public static boolean isNumber(String value) {
        return value.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 支持E-mail格式
     *
     * @param value
     * @return
     */
    public static boolean isEmail(String value) {
        return value
                .matches("^([a-z0-9A-Z]+[_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    /**
     * 用户名匹配:支持数字、字母、下划线
     *
     * @param value
     * @return
     */
    public static boolean isLegal(String value) {
        return value.matches("^([a-z0-9A-Z_]+)?$");
    }

    /**
     * 型号匹配:支持数字、字母、下划线/连接符
     *
     * @param value
     * @return
     */
    public static boolean isModel(String value) {
        return value.matches("^([a-z0-9A-Z_-]+)?$");
    }

    /**
     * 统计给定字符串中单字节字符的个数
     *
     * @param str
     * @return
     */
    public static int singleByteCount(String str) {
        int count = 0;
        String[] strArr = str.split("");
        int l = strArr.length;
        for (int i = 0; i < l; i++) {
            if (strArr[i].matches("[x00-xff]")) {
                count++;
            }
        }
        return count;
    }

    /**
     * 统计给定字符串中双字节字符的个数
     *
     * @param str
     * @return
     */
    public static int doubleByteCount(String str) {
        int strLen = str.length();
        return strLen - singleByteCount(str);
    }

    /**
     * 将字符串数组转换成字符串,即字符串数组的字符串化, 就是将每一个字符串数组对象拼接成一个字符串, 此过程是
     * <code>str.split("")</code>的反向转化
     *
     * @param strArr
     * @return
     */
    public static String arrToString(String[] strArr) {
        int len = strArr.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (hasValue(strArr[i]))
                sb.append(strArr[i]);
        }
        return sb.toString();
    }

    /**
     * 从给定字符串中按字节截取指定长度的字符,返回结果字符串.<br/>
     * 如果结果字符串的最后一个字符恰好是双字节字符,而且该字符串所包含的字节总数会因此而增加一个,<br/>
     * 那么,将结果字符串的最后一个字符(双字节字符)舍去,最终得到的结果字符串的字节总长度为: length - 1;<br/>
     * 参数都不能为空,否则将抛出异常
     *
     * @param str
     * @param length
     * @return
     */
    public static String interruptBytesByLength(String str, Integer length) {
        if (!hasValue(str))
            new Exception("获取源字符串出错.给定字符串不能为空!");
        if (length < 0) {
            new Exception("截取长度不能为负数!");
        }
        int allBytesLen = doubleByteCount(str) * 2 + singleByteCount(str);
        if (length > allBytesLen) {
            new Exception("截取字节长度超出给定字符串所包含字节总数:" + allBytesLen);
        }

        String[] a = new String[length];
        String[] src = str.substring(0).split("");
        src = Arrays.copyOfRange(src, 1, src.length);
        int count = 0;
        for (int i = 0; i < src.length; i++) {
            if (src[i].matches("[x00-xff]")) {
                count++;
            } else {
                if ((count + 1) == length)
                    break;
                count = count + 2;
            }
            a[i] = src[i];
            if (count >= length) {
                break;
            }
        }
        return arrToString(a);
    }

    /**
     * <p>
     * 判断字符串是否为null或""
     * </p>
     * 为空返回true<br>
     *
     * @param args
     * @return
     */
    public static boolean isEmpty(String args) {
        if (args != null && args.trim().length() > 0)
            return false;
        return true;
    }

    /**
     * <p>
     * 实际为整型数组，升序排序。
     * </p>
     * <br>
     *
     * @param arr
     * @return
     * @author leo_soul 2014-6-20
     */
    public static String[] intSort(String[] arr) {
        int[] num = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            num[i] = Integer.parseInt(arr[i]);
        }
        Arrays.sort(num);
        String[] r = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = String.valueOf(num[i]);
        }
        return r;
    }

    /**
     * <p>
     * 字符串数组排序
     * </p>
     * 冒泡法，按照字符串字典顺序排序。<br>
     *
     * @param arr
     * @return
     */
    public static String[] stringBubble(String[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    swap(arr, j, j + 1);
                }
            }
        }
        return arr;
    }

    public static void swap(String[] arr, int a, int b) {
        String temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     * <p>
     * 按字节截取字符串。
     * </p>
     * 按照指定的有效编码格式，指定的字节长度，以及截断方向(右截断/左截断)。截取后不产生乱码。<br>
     * 返回的字符串的字节长度将小于等于指定长度。可能为空字符串。<br>
     *
     * @param original          原字符串
     * @param charsetName       编码格式名
     * @param byteLen           字节长度
     * @param isRightTruncation 是否右截断。
     * @return String
     * @throws UnsupportedEncodingException
     * @author leo_soul
     */
    public static String subStrByByteLen(String original, String charsetName,
                                         int byteLen, boolean isRightTruncation)
            throws UnsupportedEncodingException {
        if (original == null || "".equals(original.trim()))
            return "";
        if (charsetName == null || "".equals(charsetName))
            throw new UnsupportedEncodingException("subStrByByteLen方法，必须指定编码格式");
        byte[] bytes = original.getBytes(charsetName);
        if (byteLen <= 0)
            return "";
        if (byteLen >= bytes.length)
            return original;

        int tempLen = 0;
        String result = "";
        if (isRightTruncation) {
            // 右截断
            // 按照指定字节长度截断，再转成临时String，计算长度。
            tempLen = new String(bytes, 0, byteLen, charsetName).length();
            // 根据该长度右截取原字符串。
            result = original.substring(0, tempLen);
            // 超过预订字节长度，则去掉一个字符。
            if (result != null && !"".equals(result.trim())
                    && result.getBytes(charsetName).length > byteLen)
                result = original.substring(0, tempLen - 1);
        } else {
            // 左截断
            // 全字符长-左截预订点(注意必须是预定点，bytes.length-byteLen+1)所右截断的串的字符长+1，计算长度。(为了给左截串多留一个字符。)
            // tempLen = original.length()-new
            // String(bytes,0,bytes.length-byteLen+1,charsetName).length()+1;
            // 根据该长度左截取原字符串。注意起始下标计算方法。
            // result = original.substring(original.length()-tempLen);
            // 由于以上公式可以展开，由此得到简化版。
            tempLen = new String(bytes, 0, bytes.length - byteLen + 1,
                    charsetName).length() - 1;
            result = original.substring(tempLen);
            // 超过预订字节长度，则去掉一个字符(左截)。
            if (result != null && !"".equals(result.trim())
                    && result.getBytes(charsetName).length > byteLen)
                result = original.substring(tempLen + 1);
        }
        return result;
    }

    /**
     * 字符替换。用于渠道订购用户查询时显示用户手机号用
     * <p>
     * 字符的前后几位保持不变，其它字符用代替字符替换（先替换前几位字符，后替换后几位字符）
     * </p>
     *
     * @param args      原字符
     * @param prefix    前几位(0,表示不做替换）
     * @param postfix   后几位(0,表示不做替换）
     * @param character 替换字符(若替换字符为一位，则保持长度不变)
     * @return 替换后的字符
     */
    public static String stringSwitch(String args, int prefix, int postfix,
                                      String character) {
        if (prefix < 0 || postfix < 0) {
            return args;
        }
        if (prefix == 0 && postfix == 0) {
            return args;
        }
        if (args != null && args.trim().length() > 0) {
            StringBuffer buf = new StringBuffer();
            int argsLength = args.length();
            // 保证被替换的长度大于原字符长度
            if (argsLength > prefix + postfix) {
                if (prefix != 0) {
                    String stringPrefix = args.substring(0, prefix);
                    buf.append(stringPrefix);
                }
                for (int i = prefix; i < argsLength - postfix; i++) {
                    buf.append(character);
                }
                if (postfix != 0) {
                    String stringPostfix = args.substring(argsLength - postfix);
                    buf.append(stringPostfix);
                }
                return buf.toString();
            } else {
                return args;
            }
        }
        return null;
    }

    /**
     * 按字节长度截取字符串
     *
     * @param str     将要截取的字符串参数
     * @param toCount 截取的字节长度
     * @param more    字符串末尾补上的字符串
     * @return 返回截取后的字符串
     */
    public static String substring(String str, int toCount, String more) {
        int reInt = 0;
        String reStr = "";
        if (str == null)
            return "";
        char[] tempChar = str.toCharArray();
        for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = String.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            reStr += tempChar[kk];
        }
        if (toCount == reInt || (toCount == reInt - 1))
            reStr += more;
        return reStr;
    }

    /**
     * 按字节长度截取字符串
     *
     * @param str     将要截取的字符串参数
     * @param toCount 截取的字节长度
     * @param more    字符串末尾补上的字符串
     * @return 返回截取后的字符串
     */
    public static String subString(String str, int toCount, String more) {
        StringBuilder strResult = new StringBuilder();
        if (str.length() <= toCount) {
            strResult.append(str);
        } else {
            strResult.append(str.substring(0, toCount));
            strResult.append(more);
        }
        return strResult.toString();
    }

    /**
     * 传入一个整数num，将其转为长度为length的字符串，如果长度不够用0补齐
     * 如果长度超过就取后面的长度length
     *
     * @param num    整数
     * @param length 转为字符串的长度
     * @return
     */
    public static String int2DoubleString(int num, int length) {
        String str = String.valueOf(num);
        int len = str.length();
        StringBuilder builder = new StringBuilder();
        if (len == length) return str;
        if (len > length) {
            str = str.substring(len - length);
            return str;
        }

        for (int i = 0; i < length - len; i++) {
            builder.append("0");
        }
        builder.append(str);
        return builder.toString();


    }

    /**
     * <p>
     * 返回str1长度不超过maxlen的字符，每个汉字的长度为2
     * </P>
     *
     * @param str1
     * @param maxlen
     * @return
     */
    public static String getLengthString(String str1, int maxlen) {
        int currentLen = 0;
        String tempchar = "";
        char t;
        for (int n = 0; n < str1.length(); n++) {
            t = str1.charAt(n);
            if (t < 127) {
                currentLen = currentLen + 1;
            } else {
                currentLen = currentLen + 2;
            }
            if (currentLen <= maxlen) {
                tempchar = tempchar + String.valueOf(t);
            } else {
                break;
            }

        }
        return tempchar;
    }

    /**
     * <p>
     * 传入的null字符串，也认为是空字符串
     * </p>
     * <br>
     *
     * @param obj
     * @return
     */
    public static String object2Str(Object obj) {
        if (obj != null && !obj.toString().equalsIgnoreCase("null")) {
            return obj.toString().trim();
        }
        return "";
    }

    /**
     * 对象转Int
     *
     * @param obj
     * @param def
     * @return
     */
    public static int String2Int(Object obj, int def) {
        int result = def;
        if (!isEmpty(object2Str(obj))) {
            try {
                result = Integer.valueOf(object2Str(obj));
            } catch (Exception ex) {
                // ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * <p>
     * </p>
     * <br>
     *
     * @param newString
     * @return
     */
    public static String ISOtoGB(String newString) {
        String submit = newString;
        try {
            if (submit != null) {
                // submit=submit.replace('\'','`').replace('"','`');
                submit = new String(submit.getBytes("GBK"), "ISO8859_1");
            } else {
                submit = "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return submit;
    }

    /**
     * 字符串是否为数字组成。
     * <p>
     * 必须为数字,包括了null值判断,小数不是,负数不是,科学计数不是。
     * </p>
     *
     * @param num 要检验的字符串
     * @return
     * @author liwei
     */
    public static boolean isNum(String num) {
        if (isEmpty(num))
            return false;

        String regex = "[\\d]+";
        boolean b = false;
        try {
            b = num.matches(regex);
        } catch (Exception e) {
            b = false;
        }
        return b;
    }

    public static boolean isEng(String str) {
        String temp = new String(str);
        String repStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String space = " ";
        for (int i = 0; i < repStr.length(); i++) {
            temp = temp.replace(repStr.charAt(i), space.charAt(0));
        }
        temp = temp.trim();
        boolean re = false;
        if (temp.equals("")) {
            re = true;
        }
        return re;
    }

    /**
     * <p>
     * 密码只允许出现abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-
     * 这些符号
     * </P>
     *
     * @param ps
     * @return
     */
    public static boolean isPassword(String ps) {
        String temp = new String(ps);
        String repStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
        String space = " ";
        for (int i = 0; i < repStr.length(); i++) {
            temp = temp.replace(repStr.charAt(i), space.charAt(0));
        }
        temp = temp.trim();
        boolean re = false;
        if (temp.equals("")) {
            re = true;
        }
        return re;
    }

    /**
     * <p>
     * 判断strEmail是否为Email格式
     * </P>
     *
     * @param strEmail
     * @return 符合格式true，否则为false
     */
    public static boolean isEmailByChar(String strEmail) {
        String temp = new String(strEmail);
        boolean re = false;
        char at = '@';
        int m = 0;
        int n = 0;
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == at) {
                m = m + 1;
                n = i;
            }
        }
        String kk = ".";
        int k = 0;
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == kk.charAt(0)) {
                k = i;
            }
        }
        if ((m == 1) && n > 0 && (n < temp.length() - 1) && k > n + 1) {
            re = true;
        }
        return re;
    }

    /**
     * 判断一个IP地址是否合法
     *
     * @param ip
     * @return
     * @author liufengyu
     */
    public static boolean isIPAddressCorrect(String ip) {
        String[] ips = ip.split("\\.");
        if (ips.length != 4) {
            return false;
        }
        for (int i = 0; i < ips.length; i++) {
            try {
                if (Integer.parseInt(ips[i]) > 255) {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * 检查传入的手机号码 是否 属于 中国移动的手机号码
     * </P>
     *
     * @return
     * @throws Exception
     */
    public static boolean isCMCCmobile(String destmsisdn) throws Exception {
        // 中国移动手机号码 段正则表达式
        String regEx = "^1((3[4-9])|(4[7])|(5[0-2|4-7|7-9])|(8[8]))[0-9]{8}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(destmsisdn); // 拿当前的手机号码与正则表达式去匹配
        boolean CMCCmobile = m.find(); // 匹配成功 的为中国移动手机号码
        return CMCCmobile;
    }

    /**
     * <p>
     * 匹配手机号码
     * </p>
     *
     * @param number
     * @return
     */
    public static boolean isMobile(String number) {
        return number.matches("^1[3|4|5|8|7][0-9]\\d{4,8}$");
    }

    /**
     * 将小写的数字转成大写数字，只实现1到9的转换
     */
    public static String uppercaseNumber(int i) {
        if (i > 0 && i < 10) {
            String[] upper = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
            return upper[i];
        }
        return "";
    }

    /**
     * <p>
     * 将数组中重复的字符串 去除
     * </P>
     *
     * @param destmsisdns
     * @return
     */
    public static String removeSameDestmsisdn(String destmsisdns) {
        String tmp = null;
        StringBuffer sb_destmsisdn = new StringBuffer();
        String[] destmsisdnArray = destmsisdns.split(",");
        if (destmsisdnArray == null || destmsisdnArray.length == 0) {
            return null;
        }
        ArrayList<String> resultList = new ArrayList<String>();
        for (int i = 0; i < destmsisdnArray.length; i++) {
            if (resultList.contains(destmsisdnArray[i])) {
                continue;
            } else {
                resultList.add(destmsisdnArray[i]);
            }
        }
        for (int i = 0; i < resultList.size(); i++) {
            sb_destmsisdn.append(resultList.get(i));
            sb_destmsisdn.append(",");
        }
        if (sb_destmsisdn.length() > 0) {
            tmp = sb_destmsisdn.substring(0, sb_destmsisdn.length() - 1);
        } else {
            tmp = sb_destmsisdn.toString();
        }
        return tmp;
    }

    /**
     * add by gongtao
     * <p>
     * 字符串的转换
     * </p>
     *
     * @param str
     * @return
     */
    public static String StringGBK(String str) {
        String s = "";
        try {
            if (str == null) {
                return null;
            }
            s = new String(str.getBytes("ISO-8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 对字符串排序
     *
     * @param s
     * @return
     */
    public static String mysort(String s) {
        char arr[] = s.toCharArray();
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    char temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return new String(arr);
    }

    /**
     * <p>
     * 将字符串str按space分成数组
     * </P>
     *
     * @param str
     * @param space
     * @return
     */
    public static String[] split(String str, String space) {
        String str1 = str.trim();
        int i, s, k;
        k = space.length();
        for (i = 0; str1.indexOf(space) != -1; i++) {
            str1 = str1.substring(str1.indexOf(space) + k, str1.length());
        }
        int bound = i + 1;
        str1 = str;
        String array1[] = new String[bound];
        array1[0] = "";
        for (i = 0; str1 != ""; i++) {
            s = str1.indexOf(space);
            if (s != -1) {
                array1[i] = str1.substring(0, s);
                str1 = str1.substring(s + k, str1.length());
            } else {
                array1[i] = str1;
                str1 = "";
            }
        }
        return array1;
    }

    /**
     * <p>
     * 将String[]转换成以space为分隔符的String,即逆向Split
     * </P>
     *
     * @param strList
     * @param space
     * @return
     */
    public static String converseSplit(String[] strList, String space) {
        String outStr = "";
        if (strList != null) {
            for (int j = 0; j < strList.length; j++) {
                outStr = outStr + strList[j];
                if (j != strList.length - 1) {
                    outStr = outStr + space;
                }
            }
        }
        return outStr;
    }

    public static String getAllWapPushid(List<?> list) {
        // modified by zhangzg
        StringBuffer wapPushMessageids = new StringBuffer(128);

        if (list.size() > 0) {
            Object wapPushMessageid = list.get(0);
            wapPushMessageids.append(wapPushMessageid);
        }
        for (int i = 1; i < list.size(); i++) {
            Object wapPushMessageid = list.get(i);
            wapPushMessageids.append(",");
            wapPushMessageids.append(wapPushMessageid);
        }
        return wapPushMessageids.toString();
    }

    /**
     * 将特殊字符转换成实体字符
     *
     * @param message
     * @return
     */
    public static String encode(String message) {
        if (message == null)
            return null;
        if (message.trim().length() == 0)
            return message;

        char[] content = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '\\':
                    result.append("&#92;");
                    break;
                case '/':
                    result.append("&#47;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '%':
                    result.append("&#37;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                case ' ':
                    result.append("&nbsp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return result.toString();
    }

    /**
     * 将实体字符转换成特殊字符
     *
     * @param message
     * @return
     */
    public static String decode(String message) {
        if (message == null) {
            return null;
        }
        if (message.trim().length() == 0) {
            return message;
        }

        String decodeStr = message.replaceAll("&lt;", "<");
        decodeStr = message.replaceAll("&gt;", ">");
        decodeStr = message.replaceAll("&amp;", "&");
        decodeStr = message.replaceAll("&quot;", "\"");
        decodeStr = message.replaceAll("&#37;", "%");
        decodeStr = message.replaceAll("&#39;", "'");
        decodeStr = message.replaceAll("&#40;", "(");
        decodeStr = message.replaceAll("&#41;", ")");
        decodeStr = message.replaceAll("&#43;", "+");
        decodeStr = message.replaceAll("&#59;", ";");
        decodeStr = message.replaceAll("&#92;", "\\");
        decodeStr = message.replaceAll("&nbsp;", " ");

        return decodeStr;
    }

    /**
     * 统计给定字符串的字节个数
     *
     * @param str
     * @return
     */
    public static int getByteLength(String str) {
        int singleByteCount = singleByteCount(str);
        int doubleByteCount = doubleByteCount(str);

        return doubleByteCount * 2 + singleByteCount;
    }

    /**
     * 返回str1中未在str2中出现过的元素，并且元素不重复
     *
     * @param str1 源字符串,多个元素以英文,分割
     * @param str2 多个元素以英文,分割
     * @return
     */
    public static String getNotContainsStr(String str1, String str2) {

        if (isEmpty(str1)) {
            return "";
        } else if (isEmpty(str2)) {
            return str1;
        }
        String[] str1Arr = str1.split(",");// 这里为什么是","而不是""
        String[] str2Arr = str2.split(",");
        StringBuffer sb = new StringBuffer();
        Set<String> strSet = new HashSet<String>();
        int count = 0;
        for (String s1 : str1Arr) {
            count = 0;
            for (String s2 : str2Arr) {
                if (s1.equals(s2)) {
                    ++count;
                }
            }
            if (count == 0) {
                strSet.add(s1);
            }
        }
        if (strSet.size() == 0) {
            return "";
        }
        for (String s : strSet) {
            sb.append(s).append(",");
        }
        String result = sb.toString();
        return result.endsWith(",") ? result.substring(0, result.length() - 1)
                : result;
    }

    /**
     * 反转字符串
     *
     * @param str
     * @return
     */
    public static String strReverse(String str) {
        if (isEmpty(str)) {
            return "";
        }
        String[] sArr = str.split(" ");
        if (sArr == null) {
            return "";
        }
        List<String> list = new ArrayList<String>();
        list = Arrays.asList(sArr);
        Collections.reverse(list);
        StringBuffer sb = new StringBuffer();
        for (String word : list) {
            sb.append(word);
        }
        return sb.toString();
    }

    public static String htmlEncode(int i) {
        if (i == '&')
            return "&amp;";
        else if (i == '<')
            return "&lt;";
        else if (i == '>')
            return "&gt;";
        else if (i == '"')
            return "&quot;";
        else
            return "" + (char) i;
    }

    public static String htmlEncode(String st) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < st.length(); i++) {
            buf.append(htmlEncode(st.charAt(i)));
        }
        return buf.toString();
    }

    /**
     * 处理发送的消息，将表情转换为约定的 [:/num]的格式
     *
     * @param content 消息体
     * @return
     */
    public static String handlerSendMsg(String content) {
        String str = content;
        String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            String num = tempText.substring(
                    "#[face/png/f_static_".length(), tempText.length()
                            - ".png]#".length());
            str = str.replace(tempText, "[:/" + num + "]");
        }
        return str;
    }

    /**
     * 处理接收的消息，将表情转换为约定的 #[face/png/f_static_005.png]的格式
     *
     * @param gifTextView
     * @param content     消息体
     * @param mContext
     * @return
     */
    public static SpannableStringBuilder handlerReceiveMsg(final TextView gifTextView, String content, Context mContext) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "(\\[:/)\\d{3}(\\])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);

        int count = 0;
        while (m.find()) {
            count++;
            if (count > 5) {
                break;
            }
        }
        m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            String num = tempText.substring("[:/".length(), tempText.length() - "]".length());

            if (count < 5) {
                sb = addGifSpan(gifTextView, num, sb, mContext, m);
            } else {
                sb = addPngSpan(num, sb, mContext, m);
            }
        }
        return sb;
    }

    private static SpannableStringBuilder addGifSpan(final TextView gifTextView, String num, SpannableStringBuilder sb, Context mContext, Matcher m) {
        try {
            String gif = "face/gif/f" + num + ".gif";
            /**
             * 如果open这里不抛异常说明存在gif，则显示对应的gif
             * 否则说明gif找不到，则显示png
             * */
            InputStream is = mContext.getAssets().open(gif);
            sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(num, is,
                            new AnimatedGifDrawable.UpdateListener() {
                                @Override
                                public void update() {
                                    gifTextView.postInvalidate();
                                }
                            })), m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            is.close();
        } catch (Exception e) {
            addPngSpan(num, sb, mContext, m);
            e.printStackTrace();
        }
        return sb;
    }

    private static SpannableStringBuilder addPngSpan(String num, SpannableStringBuilder sb, Context mContext, Matcher m) {
        String png = "face/png/f_static_" + num + ".png";
        try {
            sb.setSpan(
                    new ImageSpan(mContext, BitmapFactory
                            .decodeStream(mContext.getAssets()
                                    .open(png))), m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return sb;
    }

    /**
     * @param c
     * @return
     */
    public static boolean isNickNameLegal(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return false;
        }

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION ||
//                ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
//                ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                ) { // 判断是否是汉字
            return true;

        } else { //判断是否是数字，大写英文字母，小写英文字母
            try {
                int i = c;
                if ((i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        return false;
    }

    public static String escapeString(String oriStr) {
        if (oriStr.length() > 3) {
            oriStr = oriStr.replaceAll("&lt;", "<");
        }
        if (oriStr.length() > 3) {
            oriStr = oriStr.replaceAll("&gt;", ">");
        }
        if (oriStr.length() > 4) {
            oriStr = oriStr.replaceAll("&amp;", "&");
        }

        return oriStr;
    }


}
