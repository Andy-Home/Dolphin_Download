package com.andy.dolphin.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载任务管理
 * <p>
 * Created by andy on 17-7-31.
 */

public class TaskManager {
    /**
     * 任务集合
     */
    private List<Task> mTaskList = new ArrayList<>();

    private static TaskManager mTaskManager;

    private TaskManager() {

    }

    public static TaskManager getInstance() {
        if (mTaskManager == null) {
            mTaskManager = new TaskManager();
        }
        return mTaskManager;
    }

    public void addTask(Task task) {
        if (task != null) {
            task.status = Task.START;
            mTaskManager.addTask(task);
        }
    }

    public boolean removeTask(Task task) {
        if (task != null) {
            for (Task t : mTaskList) {
                if (t.key == task.key) {
                    mTaskList.remove(t);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean stopTask(Task task) {
        if (task != null) {
            for (Task t : mTaskList) {
                if (t.key == task.key) {
                    t.status = Task.PAUSE;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean restartTask(Task task) {
        if (task != null) {
            for (Task t : mTaskList) {
                if (t.key == task.key) {
                    t.status = Task.RESTART;
                    return true;
                }
            }
        }
        return false;
    }
}
