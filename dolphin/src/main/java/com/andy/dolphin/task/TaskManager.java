package com.andy.dolphin.task;

import android.os.Handler;
import android.os.Looper;

import com.andy.dolphin.DolphinObserver;
import com.andy.dolphin.DolphinSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载任务管理
 * <p>
 * Created by andy on 17-7-31.
 */

public class TaskManager implements DolphinSubject {
    /**
     * 任务集合
     */
    private List<Task> mTaskList = new ArrayList<>();

    /**
     * 观察者集合
     */
    private List<DolphinObserver> mObservers = new ArrayList<>();

    //任务状态以及对任务的操作状态
    public static final int PROGRESS = 1;
    public static final int DOWNLOAD_SUCCESS = 2;
    public static final int DOWNLOAD_PAUSE = 3;
    public static final int DOWNLOAD_FAILURE = 4;
    public static final int ADD_TASK = 5;
    public static final int REMOVE_TASK = 6;
    public static final int RESTART_TASK = 7;

    private static TaskManager mTaskManager;

    private TaskManager() {
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
    public void notify(final int type) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (DolphinObserver observer : mObservers) {
                    observer.update(type);
                }
            }
        });

    }

    /**
     * 添加任务
     */
    public void addTask(Task task) {
        if (task != null) {
            task.status = Task.START;
            mTaskList.add(task);
            notify(ADD_TASK);
        }
    }

    /**
     * 移除任务
     */
    public boolean removeTask(Task task) {
        if (task != null) {
            for (Task t : mTaskList) {
                if (t.key.equals(task.key)) {
                    mTaskList.remove(t);
                    notify(REMOVE_TASK);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 暂停任务
     */
    public boolean stopTask(Task task) {
        if (task != null) {
            for (Task t : mTaskList) {
                if (t.key.equals(task.key)) {
                    t.status = Task.PAUSE;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 重启任务
     */
    public boolean restartTask(Task task) {
        if (task != null) {
            for (Task t : mTaskList) {
                if (t.key.equals(task.key)) {
                    t.status = Task.RESTART;
                    notify(RESTART_TASK);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前任务列表
     */
    public List<Task> getTaskList() {
        return mTaskList;
    }
}
