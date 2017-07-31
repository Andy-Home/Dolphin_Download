package com.andy.dolphin.task;

import com.andy.dolphin.DownloadManager;

import java.net.URL;

/**
 * 下载任务实例
 * <p>
 * Created by andy on 17-7-31.
 */

public class Task {
    /**
     * 唯一标识
     */
    int key;

    /**
     * 下载状态
     */
    int status;

    /**
     * 状态值
     */
    static final int START = 1;
    static final int PAUSE = 2;
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
}
