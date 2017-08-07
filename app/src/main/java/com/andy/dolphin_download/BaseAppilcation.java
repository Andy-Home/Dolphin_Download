package com.andy.dolphin_download;

import android.app.Application;

import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.TaskManager;

public class BaseAppilcation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DownloadManager.getInstance(this);
        TaskManager.getInstance();
    }

}
