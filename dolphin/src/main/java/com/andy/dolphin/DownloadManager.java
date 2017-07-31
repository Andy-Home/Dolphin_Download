package com.andy.dolphin;

import android.util.Log;

import com.andy.dolphin.task.Task;
import com.andy.dolphin.task.TaskManager;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 下载管理类
 * <p>
 * 提供相应的下载功能接口
 * <p>
 * Created by andy on 17-7-31.
 */

public class DownloadManager {
    private final static String TAG = "DownloadManager";
    private TaskManager mTaskManager;

    public DownloadManager() {
        mTaskManager = TaskManager.getInstance();
    }

    public Task download(URL url) {

        return null;
    }

    public Task download(String link) {
        Task task = null;
        try {
            URL url = new URL(link);
            task = download(url);
        } catch (MalformedURLException e) {
            Log.d(TAG, "上传链接错误");
            e.printStackTrace();
        }
        return task;
    }

    public boolean stop(Task task) {

        return false;
    }

    public boolean restart(Task task) {
        return false;
    }

    public boolean remove(Task task) {
        return false;
    }

    public interface DolphinListener {
        void success(String result);

        void failure(String error);

        void progress(float length, float download);

        void cancle(int type);
    }
}
