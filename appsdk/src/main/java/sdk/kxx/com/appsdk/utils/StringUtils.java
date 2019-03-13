package sdk.kxx.com.appsdk.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author : kongxx
 * @Created Date : 2019/2/28 14:45
 * @Description : 字符处理工具类
 */
public class StringUtils {
    /**
     * 判断字符串不为空
     *
     * @param string
     * @return
     */
    public static boolean isNotEmpty(String string) {
        return !TextUtils.isEmpty(string);
    }

    /**
     * 判断字符串为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string);
    }


    /**
     * 只允许汉字
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[^\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 不允许汉字
     */
    public static String stringFilterWord(String str) throws PatternSyntaxException {
        String regEx = "[\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 将字符串全角化
     */
    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
}
