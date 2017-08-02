package com.andy.dolphin.task;

import android.content.Context;
import android.os.Environment;

import java.net.URL;
import java.util.Random;

/**
 * 下载任务实例
 * <p>
 * Created by andy on 17-7-31.
 */

public class Task {
    /**
     * 唯一标识
     */
    String key;

    /**
     * 下载状态
     */
    int status;

    /**
     * 状态值
     */
    public static final int START = 1;
    public static final int PAUSE = 2;
    public static final int RESTART = 3;
    public static final int FINISH = 4;
    public static final int ERROR = 5;

    /**
     * URL
     */
    private URL url;

    /**
     * 下载的文件名
     */
    private String filePath = null;

    /**
     * 文件已下载内容百分比
     */
    private float percent;


    public Task(URL url, Context context) {
        this.url = url;
        status = START;
        Random random = new Random();
        int a = random.nextInt(1000);
        key = "andy" + a + System.currentTimeMillis();
        filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + getFileName();
    }

    /**
     * 获取文件名
     */
    private String getFileName() {
        String link = url.toString();
        //类似https://downloads.gradle.org/distributions/gradle-4.1-milestone-1-bin.zip链接的处理方式
        if (!link.contains("?")) {
            return link.substring(link.lastIndexOf("/") + 1);
        }
        return null;
    }

    public URL getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getKey() {
        return key;
    }
}
