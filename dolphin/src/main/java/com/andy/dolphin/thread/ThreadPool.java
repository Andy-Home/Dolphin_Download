package com.andy.dolphin.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载线程池
 * <p>
 * Created by andy on 17-8-3.
 */

public class ThreadPool {
    private static ThreadPool mThreadPool;
    private ExecutorService mPool;

    private ThreadPool() {
        mPool = Executors.newFixedThreadPool(5);
    }

    public static ThreadPool getInstance() {
        if (mThreadPool == null) {
            mThreadPool = new ThreadPool();
        }
        return mThreadPool;
    }

    /**
     * 执行线程
     */
    public void execute(Runnable command) {
        mPool.execute(command);
    }
}
