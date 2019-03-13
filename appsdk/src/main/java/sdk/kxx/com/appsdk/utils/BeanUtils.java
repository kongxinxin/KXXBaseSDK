package sdk.kxx.com.appsdk.utils;

import android.os.Looper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author : kongxx
 * @Created Date : 2019/3/2 17:45
 * @Description : 对象处理工具类
 */
public class BeanUtils {
    private static final int SPLIT = 100;
    /**
     * 判断数据是否为空
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("all")
    public static boolean isEmpty(Object obj) {
        boolean flag = true;
        if (obj != null) {
            if (obj instanceof String) {
                flag = (obj.toString().trim().length() == 0);
                String tmp = obj.toString();
                flag = "".equals(tmp.trim());

            } else if (obj instanceof Collection<?>) {
                flag = ((Collection) obj).size() == 0;

            } else if (obj instanceof Map) {
                flag = ((Map) obj).size() == 0;

            } else if (obj instanceof Object[]) {
                flag = ((Object[]) obj).length == 0;

            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 判断数据不为空
     * @param obj
     * @return
     */
    @SuppressWarnings("all")
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
    /**
     * 字符串转换日期
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date parseDate(String dateStr, String formatStr) {
        Date result = null;
        try {
            if (dateStr.length() < formatStr.length()) {
                dateStr = "0" + dateStr;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            result = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URl的拼接
     *
     * @param url
     * @param parts
     * @return
     */
    public static String urlAppend(String url, String parts) {
        url = url.trim();
        if (!BeanUtils.isEmpty(parts)) {
            if (url.indexOf("?") < 0) {
                url += "?";
            } else {
                url += "&";
            }
        }
        return url + parts;
    }

    /**
     * 生成32位的MD5加密方式
     *
     * @param source
     * @return
     */
    public static String md532(String source) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = source.getBytes();
            //使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                //将每个数(int)b进行双字节加密
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 认证的加密算法
     */
    public static String encrypt(String message, String key) {
        byte[] values = message.getBytes();
        byte[] keyValues = getBytes(key);
        int klength = keyValues.length;
        for (int i = 0, length = values.length; i < length; i++) {
            values[i] = (byte) (values[i] ^ keyValues[i % klength]);
        }
        return bytesToHexString(values);
    }
    /**
     * 自定义的getBtyes
     */
    protected static byte[] getBytes(String message) {
        byte[] values = new byte[message.length()];
        for (int i = 0; i < message.length(); i++) {
            values[i] = (byte) message.charAt(i);
        }
        return values;
    }

    /**
     * byte转成16进制
     */
    public static String bytesToHexString(byte[] src) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < src.length; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

    /**
     * 日期转换
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 拆解数据获取具体的大小和具体的数据
     *
     * @param data
     * @return
     */
    public static byte[] decryptZip(byte[] data) {
        int length = data.length;
        if (length < SPLIT + 1) {
            return data;
        } else {
            int count = length / (SPLIT + 1);
            int mode = length % (SPLIT + 1);
            byte[] source = new byte[count * SPLIT + mode];
            for (int i = 0; i < count; i++) {
                int startIndex = i == 0 ? 0 : (i * SPLIT + i);
                System.arraycopy(data, startIndex, source, i * SPLIT, SPLIT);
            }
            if (mode > 0) {
                int endIndex = (count) * (SPLIT + 1);
                System.arraycopy(data, endIndex, source, count * SPLIT, mode);
            }
            return source;
        }
    }
    /*************************************加密处理*******************************************/


    /***************************************************************************************/
    /**
     * 解压服务器的数据
     *
     * @param inputStream
     * @return
     */
    public static String unGzipContent(InputStream inputStream) {
        String mesage = "";
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            int i = 0;
            while ((i = inputStream.read()) != -1) {
                out.write(i);
            }
            out.flush();
            mesage = unGzipDataSource(decryptZip(out.toByteArray()), 2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BeanUtils.close(out);
            BeanUtils.close(inputStream);
        }
        return mesage.toString();
    }

    /**
     * 根据数据偏移根据解压
     *
     * @param data
     * @return
     */
    public static String unGzipDataSource(byte[] data, int len) {
        String result = "";
        try {
            byte[] lenbtyes = new byte[len];
            //真是数据的长度
            byte[] databytes = new byte[data.length - len];
            //获取压缩的长度
            System.arraycopy(data, 0, lenbtyes, 0, lenbtyes.length);
            //重新生成zlib压缩后的数据
            System.arraycopy(data, len, databytes, 0, databytes.length);
            //进行数据的解压操作
            byte[] zipbtyes = unGzipData(databytes);
            result = new String(zipbtyes, 0, zipbtyes.length, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return result;
    }
    /**
     * 真实的数据解压
     *
     * @param data
     * @return
     */
    public static byte[] unGzipData(byte[] data) {
        byte[] b = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gzip = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream((InputStream) bis);
            int num = -1;
            byte[] buf = new byte[1024];
            baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            baos.flush();
            b = baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(baos);
            close(gzip);
            close(bis);
        }
        return b;
    }
    /**
     * 根据流获取文本内容
     */
    public static String getContent(InputStream in, String encode) {
        StringBuffer mesage = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, encode));
            int i = 0;
            while ((i = reader.read()) != -1) {
                mesage.append((char) i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BeanUtils.close(in);
        }
        return mesage.toString();
    }

    /***************************************************************************************/
    /**
     * 判断是否在主线程
     * @return
     */
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    /**
     * 通用资源的关闭操作
     *
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
