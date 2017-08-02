package com.andy.dolphin;

/**
 * 被观察者
 * <p>
 * Created by andy on 17-8-2.
 */

public interface DolphinSubject {
    /**
     * 增加订阅者
     *
     * @param observer
     */
    void attach(DolphinObserver observer);

    /**
     * 删除订阅者
     *
     * @param observer
     */
    void detach(DolphinObserver observer);

    /**
     * 通知订阅者更新消息
     *
     * @param type 消息类型
     */
    void notify(int type);
}
