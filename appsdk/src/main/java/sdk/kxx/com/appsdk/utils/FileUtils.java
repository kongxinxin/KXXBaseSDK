package sdk.kxx.com.appsdk.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import sdk.kxx.com.appsdk.app.AppConfig;

/**
 * @author : kongxx
 * @Created Date : 2019/3/7 11:16
 * @Description : 文件处理工具类
 */
public class FileUtils {
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!" + e.getMessage());
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        if (BeanUtils.isNotEmpty(flist)) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    //获取sd卡根目录
    public static String getSDPath() {

        return Environment.getExternalStorageDirectory().getAbsolutePath();

    }

    public static boolean isExternalSdcardWriteable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) && Environment.getExternalStorageDirectory().canWrite()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否连接了sdcard
     *
     * @return boolean
     */
    public static boolean externalMemoryAvailable() {

        if (getExternalStoragePath() != null) {
            return true;
        }
        return false;
    }

    private static String getExternalStoragePath() {
        // 获取SdCard状态
        String state = Environment.getExternalStorageState();

        // 判断SdCard是否存在并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Environment.getExternalStorageDirectory().canWrite()) {
                return Environment.getExternalStorageDirectory().getPath();
            }
        }
        LogUtils.w("sdcard", "SD can not be used");
        return null;
    }

    /**
     * getAvailableExternalMemorySize
     * 获取内置sd卡的可用空间
     *
     * @return long
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 保存序列对象
     */
    public static synchronized void saveObject(Object obj, String path) {
        ObjectOutputStream ost = null;
        try {
            ost = new ObjectOutputStream(new FileOutputStream(path));
            ost.writeObject(obj);
            ost.flush();
        } catch (Exception e) {
        } finally {
            close(ost);
        }
    }

    /**
     * 异步的方式进行存储
     */
    public static void aysncSaveObject(final Object obj, final String path) {
        new Thread() {
            public void run() {
                saveObject(obj, path);
            }
        }.start();
    }

    /**
     * 获取对象
     */
    public static Object getSaveObject(String path) {
        ObjectInputStream ost = null;
        Object obj = null;
        try {
            ost = new ObjectInputStream(new FileInputStream(path));
            obj = ost.readObject();
        } catch (Exception e) {
            deleteFile(path);
        } finally {
            close(ost);
        }
        return obj;
    }

    /**
     * 获取文件内容
     *
     * @param in
     * @param encode
     * @return
     */
    public static String getContent(InputStream in, String encode) {
        String mesage = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = in.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            mesage = new String(outputStream.toByteArray(), encode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BeanUtils.close(in);
        }
        return mesage;
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        boolean hasFile = false;
        try {
            File file = new File(filePath);
            hasFile = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasFile;
    }


    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                if (isFileExist(filePath)) {
                    file.delete();
                }
            } else if (file.isDirectory()) {
                File[] tempFiles = file.listFiles();
                if (!BeanUtils.isEmpty(tempFiles)) {
                    for (File tempFile : tempFiles) {
                        tempFile.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹以及下面文件
     *
     * @param root
     */
    public static void deleteAllFiles(File root) {


        File files[] = root.listFiles();
        if (BeanUtils.isNotEmpty(files)) {

            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();

                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();

                        } catch (Exception e) {

                        }
                    }
                }
            }
        } else {
            root.delete();
        }


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
     * 创建文件夹
     *
     * @param directory directory
     * @return boolean
     */
    public static boolean createDir(String directory) {
        if (StringUtils.isEmpty(directory)) {
            return false;
        }

        File destDir = new File(directory);
        if (destDir.isDirectory()) {
            return true;
        } else {
            if (isExistSDcard()) {
                return destDir.mkdirs();
            } else {
                ToastHelper.showDefaultToast("SD卡错误");
            }
        }

        return false;
    }

    /**
     * 是否有sdcard
     */
    public static boolean isExistSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    public static void writeLog(String p_Message) {
        String _SDCardPath = getExternalStoragePath();
        if (_SDCardPath != null) {
            SimpleDateFormat _FileFormat = new SimpleDateFormat("yyyy_MM_dd");
            String _FileName = _FileFormat.format(new Date()) + ".txt";
            String path = AppConfig.SystemConstants.LOG + "/";
            String _LogFilePath = path + _FileName;
            try {
                createFile(_LogFilePath);
                FileOutputStream fout = new FileOutputStream(_LogFilePath, true);
                p_Message += "\r\n";
                byte[] bytes = p_Message.getBytes();
                fout.write(bytes);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void saveFileData(String fileName, String p_Message) {
        String _SDCardPath = getExternalStoragePath();
        if (_SDCardPath != null) {
            String path = AppConfig.SystemConstants.LOG + "/";
            String _LogFilePath = path + fileName;
            try {
                createFile(_LogFilePath);
                FileOutputStream fout = new FileOutputStream(_LogFilePath, true);
                p_Message += "\r\n";
                byte[] bytes = p_Message.getBytes();
                fout.write(bytes);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param p_Path
     * @throws IOException
     */
    private static void createFile(String p_Path) throws IOException {
        File _LogFile = new File(p_Path);
        if (!_LogFile.exists()) {
            _LogFile.createNewFile();
        }
    }

    /**
     * 获取单个文件的MD5值！

     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return String.format("%032x", bigInt);
    }

    /**
     * 获取文件类型
     * @param path
     * @return
     */
    public static String getMediaTypeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    /**
     * 对byte类型的数组进行MD5加密
     *
     */
    public static String getMD5String(byte[] bytes) {
        MessageDigest messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
            char c1 = hexDigits[bytes[l] & 0xf];
            stringbuffer.append(c0);
            stringbuffer.append(c1);
        }
        return stringbuffer.toString();
    }
}
