package com.andy.dolphin.task;

import android.util.Log;

import com.andy.dolphin.thread.DownloadThread;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Random;

/**
 * 下载任务实例
 * <p>
 * Created by andy on 17-7-31.
 */
@Entity(indexes = {
        @Index(value = "key", unique = true)
})
public class Task {
    private static final String TAG = "Task";
    /**
     * 状态值
     */
    public static final int START = 1;
    public static final int PAUSE = 2;
    public static final int WAIT = 3;
    public static final int FINISH = 4;
    public static final int ERROR = 5;

    @Id(autoincrement = true)
    private long id;

    /**
     * 唯一标识
     */
    @NotNull
    String key;

    /**
     * 下载状态
     */
    @NotNull
    int status;

    /**
     * URL
     */
    @NotNull
    private String url;

    /**
     * 下载的文件名
     */
    private String fileName = null;

    /**
     * 文件大小
     */
    private int fileLength = -1;

    /**
     * 文件已下载内容百分比
     */
    private float percent;

    /**
     * 下载线程
     */
    private static DownloadThread mDownloadThread;

    public Task(String url) {
        this.url = url;
        status = START;
        Random random = new Random();
        int a = random.nextInt(1000);
        key = "andy" + a + System.currentTimeMillis();
        mDownloadThread = new DownloadThread(this);
    }

    @Generated(hash = 212695871)
    public Task(long id, @NotNull String key, int status, @NotNull String url,
                String fileName, int fileLength, float percent) {
        this.id = id;
        this.key = key;
        this.status = status;
        this.url = url;
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.percent = percent;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        Log.d(TAG, "文件名:" + fileName);
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getKey() {
        return key;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    DownloadThread getDownloadThread() {
        return mDownloadThread;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
