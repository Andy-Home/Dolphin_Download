package com.andy.dolphin;

/**
 * 观察者
 * <p>
 * Created by andy on 17-8-2.
 */

public interface DolphinObserver {

    /**
     * 消息更新
     *
     * @param type 消息类型
     */
    void update(int type);
}
