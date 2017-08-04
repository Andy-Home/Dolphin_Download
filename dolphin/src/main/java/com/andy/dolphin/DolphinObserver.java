package com.andy.dolphin;

import com.andy.dolphin.task.Task;

/**
 * 观察者
 * <p>
 * Created by andy on 17-8-2.
 */

public interface DolphinObserver {

    /**
     * 消息更新
     *
     * @param type  消息类型
     * @param task  任务
     */
    void update(int type, Task task);
}
