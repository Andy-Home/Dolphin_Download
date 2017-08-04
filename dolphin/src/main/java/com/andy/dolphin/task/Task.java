package com.andy.dolphin.task;

import android.util.Log;

import com.andy.dolphin.thread.DownloadThread;

import java.net.URL;
import java.util.Random;

/**
 * 下载任务实例
 * <p>
 * Created by andy on 17-7-31.
 */

public class Task {
    private final String TAG = "Task";
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
    private String fileName = null;

    /**
     * 文件已下载内容百分比
     */
    private float percent;

    /**
     * 下载线程
     */
    private DownloadThread mDownloadThread;

    public Task(URL url) {
        this.url = url;
        status = START;
        Random random = new Random();
        int a = random.nextInt(1000);
        key = "andy" + a + System.currentTimeMillis();
        mDownloadThread = new DownloadThread(this);
    }

    public URL getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        Log.d(TAG, "文件名:" + fileName);
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

    public DownloadThread getDownloadThread() {
        return mDownloadThread;
    }
}
