package com.andy.dolphin.task;

import android.util.Log;

import com.andy.dolphin.database.DaoSession;
import com.andy.dolphin.database.TaskDao;
import com.andy.dolphin.thread.DownloadThread;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Random;

/**
 * 下载任务实例
 * <p>
 * Created by andy on 17-7-31.
 */
@Entity(indexes = {@Index(value = "key", unique = true)},
        active = true,
        nameInDb = "task_list")
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
    public static final int REMOVE = 6;

    /**
     * 唯一标识
     */
    @Id
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
    @Transient
    private DownloadThread mDownloadThread;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    public Task(String url) {
        this.url = url;
        status = START;
        Random random = new Random();
        int a = random.nextInt(1000);
        key = "andy" + a + System.currentTimeMillis();
        mDownloadThread = new DownloadThread(this);
    }

    @Generated(hash = 1367132294)
    public Task(String key, int status, @NotNull String url, String fileName, int fileLength,
                float percent) {
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

    public void setDownloadThread(DownloadThread mDownloadThread) {
        this.mDownloadThread = mDownloadThread;
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

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }
}
