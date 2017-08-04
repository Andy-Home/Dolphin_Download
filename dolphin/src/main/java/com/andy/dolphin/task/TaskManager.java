package com.andy.dolphin.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.andy.dolphin.DolphinObserver;
import com.andy.dolphin.DolphinSubject;
import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.thread.ThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载任务管理
 * <p>
 * Created by andy on 17-7-31.
 */

public class TaskManager implements DolphinSubject {
    private final String TAG = "TaskManager";
    /**
     * 等待下载的任务集合
     */
    private List<Task> mWaitTaskList = new ArrayList<>();

    /**
     * 正在运行的任务集合
     */
    private List<Task> mRuningTaskList = new ArrayList<>();

    /**
     * 已完结任务集合
     */
    private List<Task> mFinishTaskList = new ArrayList<>();

    /**
     * 暂停任务集合
     */
    private List<Task> mPauseTaskList = new ArrayList<>();

    /**
     * 观察者集合
     */
    private List<DolphinObserver> mObservers = new ArrayList<>();

    //任务状态以及对任务的操作状态
    public static final int PROGRESS = 101;
    public static final int DOWNLOAD_SUCCESS = 102;
    public static final int DOWNLOAD_PAUSE = 103;
    public static final int DOWNLOAD_FAILURE = 104;
    public static final int ADD_TASK = 105;
    public static final int REMOVE_TASK = 106;
    public static final int RESTART_TASK = 107;
    public static final int PAUSE_TASK = 108;

    private static TaskManager mTaskManager;
    private ThreadPool mPool;

    /**
     * 最大同时下载任务数
     */
    private int downloadThreadNum = 3;

    private TaskManager() {
        mPool = ThreadPool.getInstance();
    }

    public static TaskManager getInstance() {
        if (mTaskManager == null) {
            mTaskManager = new TaskManager();
        }
        return mTaskManager;
    }

    @Override
    public void attach(DolphinObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void detach(DolphinObserver observer) {
        mObservers.remove(observer);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void notify(final int type, String key) {
        //检索对应任务
        Task task = null;
        for (Task t : mRuningTaskList) {
            if (t.key.equals(key)) {
                task = t;
            }
        }

        if (task == null) {
            for (Task t : mWaitTaskList) {
                if (t.key.equals(key)) {
                    task = t;
                }
            }
        }
        if (task == null) {
            for (Task t : mFinishTaskList) {
                if (t.key.equals(key)) {
                    task = t;
                }
            }
        }

        if (task == null) {
            for (Task t : mPauseTaskList) {
                if (t.key.equals(key)) {
                    task = t;
                }
            }
        }
        //根据type值,处理不同情况
        if (task != null) {
            switch (type) {
                case DOWNLOAD_SUCCESS:
                    Log.d(TAG, "下载成功：" + task.key);
                    task.status = Task.FINISH;
                    mFinishTaskList.add(task);
                    mRuningTaskList.remove(task);
                    startNewTask();
                    break;
                case DOWNLOAD_PAUSE:
                    Log.d(TAG, "下载暂停：" + task.key);
                    task.status = Task.PAUSE;
                    if (!mPauseTaskList.contains(task) && !mWaitTaskList.contains(task)) {
                        mPauseTaskList.add(task);
                        mRuningTaskList.remove(task);
                    }
                    startNewTask();
                    break;
                case DOWNLOAD_FAILURE:
                    Log.d(TAG, "下载失败：" + task.key);
                    task.status = Task.ERROR;
                    mPauseTaskList.add(task);
                    mRuningTaskList.remove(task);
                    startNewTask();
                    break;
                case ADD_TASK:
                    Log.d(TAG, "添加任务：" + task.key);
                    task.status = Task.PAUSE;
                    startNewTask();
                    break;
                case REMOVE_TASK:
                    Log.d(TAG, "移除任务：" + task.key);
                    if (mRuningTaskList.contains(task)) {
                        mRuningTaskList.remove(task);
                    } else if (mWaitTaskList.contains(task)) {
                        mWaitTaskList.remove(task);
                    } else if (mFinishTaskList.contains(task)) {
                        mFinishTaskList.remove(task);
                    } else if (mPauseTaskList.contains(task)) {
                        mPauseTaskList.remove(task);
                    }
                    clearTask(task);
                    break;
                case RESTART_TASK:
                    Log.d(TAG, "重启任务：" + task.key);
                    task.status = Task.RESTART;
                    if (mRuningTaskList.size() >= downloadThreadNum) {
                        Task t = mRuningTaskList.get(0);
                        t.status = Task.PAUSE;
                        mWaitTaskList.add(t);
                        mRuningTaskList.remove(0);
                    }
                    mRuningTaskList.add(task);
                    if (mPauseTaskList.contains(task)) {
                        mPauseTaskList.remove(task);
                    } else {
                        mWaitTaskList.remove(task);
                    }

                    mPool.execute(task.getDownloadThread());
                    break;
                case PAUSE_TASK:
                    Log.d(TAG, "暂停任务：" + task.key);
                    if (task.status == Task.START || task.status == Task.RESTART) {
                        task.status = Task.PAUSE;
                        mPauseTaskList.add(task);
                        mRuningTaskList.remove(task);
                    }
                    task.status = Task.PAUSE;
                    break;
            }
            final Task t = task;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DolphinObserver observer : mObservers) {
                        observer.update(type, t);
                    }
                }
            });
        }
    }

    /**
     * 开始下载任务
     */
    private void startNewTask() {
        if (mRuningTaskList.size() < downloadThreadNum) {
            if (mWaitTaskList.size() > 0) {
                Task task = mWaitTaskList.get(0);
                task.status = Task.START;
                mRuningTaskList.add(task);
                Log.d(TAG, "开始任务下载：" + task.key);
                mPool.execute(task.getDownloadThread());
                mWaitTaskList.remove(0);
            }
        }
    }

    /**
     * 清除任务
     */
    private void clearTask(Task task) {
        //改变状态,下载线程结束
        task.status = Task.PAUSE;
        if (task.getFileName() != null) {
            File file = new File(DownloadManager.downloadDirectory + File.separator + task.getFileName());
            if (file.delete()) {
                Log.d(TAG, task.getKey() + " 任务文件删除成功");
            }
        }

    }

    /**
     * 添加任务
     */
    public void addTask(Task task) {
        if (task != null) {
            mWaitTaskList.add(task);
            notify(ADD_TASK, task.key);
        }
    }

    /**
     * 移除任务
     */
    public void removeTask(Task task) {
        if (task != null) {
            notify(REMOVE_TASK, task.key);
        }
    }

    /**
     * 暂停任务
     */
    public void stopTask(Task task) {
        if (task != null) {
            notify(PAUSE_TASK, task.key);
        }
    }

    /**
     * 重启任务
     */
    public void restartTask(Task task) {
        if (task != null) {
            notify(RESTART_TASK, task.key);
        }
    }

    /**
     * 获取当前任务列表
     */
    public List<Task> getTaskList() {
        List<Task> allTask = new ArrayList<>();
        allTask.addAll(mRuningTaskList);
        allTask.addAll(mWaitTaskList);
        allTask.addAll(mFinishTaskList);
        allTask.addAll(mPauseTaskList);
        return allTask;
    }

    /**
     * 设置最大下载数量,最大为5
     *
     * @param num num < 5
     */
    public void setDownloadThreadNum(int num) {
        if (num > 5) {
            downloadThreadNum = 5;
        } else {
            downloadThreadNum = num;
        }
    }
}
