package com.andy.dolphin.task;

import com.andy.dolphin.DownloadManager;

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
    static final int START = 1;
    public static final int PAUSE = 2;
    static final int RESTART = 3;

    /**
     * URL
     */
    private URL url;
    /**
     * 回调函数
     */
    private DownloadManager.DolphinListener listener;

    /**
     * 下载的文件名
     */
    private String fileName = null;


    public Task(URL url, DownloadManager.DolphinListener listener) {
        this.url = url;
        this.listener = listener;
        status = START;
        Random random = new Random();
        int a = random.nextInt(1000);
        key = "andy" + a + System.currentTimeMillis();
    }

    public DownloadManager.DolphinListener getListener() {
        return listener;
    }

    public URL getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStatus() {
        return status;
    }
}
