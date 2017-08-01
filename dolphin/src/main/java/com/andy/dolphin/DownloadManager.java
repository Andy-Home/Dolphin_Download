package com.andy.dolphin;

import android.util.Log;

import com.andy.dolphin.task.Task;
import com.andy.dolphin.task.TaskManager;
import com.andy.dolphin.thread.DownloadThread;

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
    private static TaskManager mTaskManager;
    private static DownloadManager mDownloadManager;

    private DownloadManager() {
        mTaskManager = TaskManager.getInstance();
    }

    public static DownloadManager getInstance() {
        if (mDownloadManager == null) {
            mDownloadManager = new DownloadManager();
        }
        return mDownloadManager;
    }

    /**
     * 下载功能
     *
     * @param url
     * @param listener
     */
    public static Task download(URL url, DolphinListener listener) {
        Task task = new Task(url, listener);
        mTaskManager.addTask(task);
        //TODO:线程管理
        new Thread(new DownloadThread(task)).start();
        return task;
    }

    public static Task download(String link, DolphinListener listener) {
        Task task = null;
        try {
            URL url = new URL(link);
            task = download(url, listener);
        } catch (MalformedURLException e) {
            Log.d(TAG, "上传链接错误");
            e.printStackTrace();
        }
        return task;
    }

    public static boolean stop(Task task) {
        return mTaskManager.stopTask(task);
    }

    public static boolean restart(Task task) {
        return mTaskManager.restartTask(task);
    }

    public static boolean remove(Task task) {
        return mTaskManager.removeTask(task);
    }

    public interface DolphinListener {
        /**
         * 下载成功
         *
         * @param type 结果
         */
        void success(int type);

        /**
         * 下载失败
         *
         * @param type 结果
         */
        void failure(int type);

        /**
         * 下载进度
         *
         * @param length   文件大小
         * @param download 已下载大小
         */
        void progress(float length, float download);

        /**
         * 暂停任务
         *
         * @param type 结果
         */
        void stop(int type);
    }
}
