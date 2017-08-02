package com.andy.dolphin;

import android.content.Context;
import android.util.Log;

import com.andy.dolphin.task.Task;
import com.andy.dolphin.task.TaskManager;
import com.andy.dolphin.thread.DownloadThread;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
     * @param context   {@link Context}
     * @param url   {@link URL}
     * @return {@link Task}
     */
    public static Task download(Context context, URL url) {
        Task task = new Task(url, context);
        mTaskManager.addTask(task);
        new Thread(new DownloadThread(task)).start();
        return task;
    }

    /**
     * 下载功能
     *
     * @param context {@link Context}
     * @param link    下载链接字符串
     */
    public static Task download(Context context, String link) {
        Task task = null;
        try {
            URL url = new URL(link);
            task = download(context, url);
        } catch (MalformedURLException e) {
            Log.d(TAG, "上传链接错误");
            e.printStackTrace();
        }
        return task;
    }

    /**
     * 暂停下载功能
     *
     * @param task {@link TaskManager} 管理的任务
     * @return true表示暂停成功, false表示暂停失败
     */
    public static boolean stop(Task task) {
        return mTaskManager.stopTask(task);
    }

    /**
     * 重启下载功能
     *
     * @param task {@link TaskManager} 管理的任务
     * @return true表示重启成功, false表示重启失败
     */
    public static boolean restart(Task task) {
        boolean flag = mTaskManager.restartTask(task);
        new Thread(new DownloadThread(task)).start();
        return flag;
    }

    /**
     * 移除下载任务功能
     *
     * @param task {@link TaskManager} 管理的任务
     * @return true表示移除成功, false表示移除失败
     */
    public static boolean remove(Task task) {
        return mTaskManager.removeTask(task);
    }

    /**
     * 获取当前任务列表
     *
     * @return 任务列表
     */
    public static List<Task> getTaskList() {
        return mTaskManager.getTaskList();
    }
}
