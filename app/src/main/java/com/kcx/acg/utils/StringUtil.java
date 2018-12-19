package com.kcx.acg.utils;

import java.text.DecimalFormat;

/**
 * Created by jb on 2018/9/5.
 */
public class StringUtil {

    /**
     * 计算文件大小，如果大于512KB就压缩
     *
     * @param filePath
     * @param size
     * @return
     */
    public static boolean isCompress(String filePath, int size) {
        double fileOrFilesSize = FileSizeUtil.getFileOrFilesSize(filePath, FileSizeUtil.SIZETYPE_KB);
        LogUtil.e("FileSize", "FileSize=" + fileOrFilesSize + "KB");
        int i = (new Double(fileOrFilesSize)).intValue();
        if (i > size) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取整百
     *
     * @param i
     * @return
     */
    public static int roundNumbers(Double i) {
        double temp = i / 100;
        double floor = Math.floor(temp);
        int c = (int) floor * 100;
        return c;
    }


    /**
     * 正则表达式（校验密码必须为6--12）
     *
     * @param value
     * @return
     */
    public static Boolean regexText(String value) {
        if (value.length() >= 6 && value.length() <= 12) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将null替换为""
     *
     * @param str
     * @return
     */
    public static String replaceNULL(String str) {
        return null == str || "null".equals(str.trim()) ? "" : str.trim();
    }

    /**
     * 金额格式转换
     *
     * @param s
     * @return
     */
    public static String toDecimalFormat(String s) {
        if ("0".equals(s)) {
            return "0.00";
        } else {

            double d = Double.parseDouble(s);
            DecimalFormat df = new DecimalFormat("#,###.00");
            String format = df.format(d);
            return format;
        }
    }

    /**
     * 金额格式转换
     *
     * @param i
     * @return
     */
    public static String toDecimalFormat(int i) {
        DecimalFormat df = new DecimalFormat("#,###");
        String format = df.format(i);
        return format;
    }

    /**
     * 替换指定位置元素为str
     *
     * @param str
     * @return
     */
    public static String replaceStr(int i, int k, String str, String data) {
        if (!"".equals(str)) {
            StringBuffer buffer = new StringBuffer(str);
            buffer.replace(i, k, data);
            return buffer.toString();
        } else {
            return "";
        }
    }


}
