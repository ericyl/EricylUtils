package com.ericyl.example.util;

import java.io.File;
import java.text.DecimalFormat;

public class FileUtil {
    public static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat(
            "0.##");
    private static final double GB_2_BYTE = Math.pow(1024, 3);
    private static final double MB_2_BYTE = Math.pow(1024, 2);
    private static final double KB_2_BYTE = 1024;

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 转换文件大小
     *
     * @param size
     * @return B/KB/MB/GB
     */
    public static CharSequence formatSize(long size) {
        if (size <= 0)
            return "0 MB";
        if (size >= GB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format(size / MB_2_BYTE))
                    .append(" G");
        } else if (size >= MB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format(size / MB_2_BYTE))
                    .append(" MB");
        } else if (size >= KB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format(size / KB_2_BYTE))
                    .append(" KB");
        } else {
            return new StringBuilder(16).append(size).append("B");
        }
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile())
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            else if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                    return;
                }
                for (File childFile : childFiles) {
                    deleteFile(childFile);
                }
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }
}