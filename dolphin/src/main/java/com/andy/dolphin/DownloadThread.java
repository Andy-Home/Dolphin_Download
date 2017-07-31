package com.andy.dolphin;

import com.andy.dolphin.task.Task;

/**
 * 下载线程
 * <p>
 * Created by andy on 17-7-31.
 */

public class DownloadThread implements Runnable {
    private DownloadManager.DolphinListener listener;

    public DownloadThread(Task task) {
        listener = task.getListener();
    }

    @Override
    public void run() {

    }
}
