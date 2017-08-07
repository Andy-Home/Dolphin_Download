package com.andy.dolphin.task;

import android.util.Log;

import com.andy.dolphin.thread.DownloadThread;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * 下载任务实例
 * <p>
 * Created by andy on 17-7-31.
 */

public class Task {
    private final String TAG = "Task";
    /**
     * 唯一标识
     */
    String key;

    /**
     * 下载状态
     */
    int status;

    /**
     * 状态值
     */
    public static final int START = 1;
    public static final int PAUSE = 2;
    public static final int FINISH = 3;
    public static final int ERROR = 4;
    public static final int WAIT = 5;

    /**
     * URL
     */
    private URL url;

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
    private float percent = 0;

    /**
     * 下载线程
     */
    private DownloadThread mDownloadThread;

    public Task(URL url) {
        this.url = url;
        status = START;
        Random random = new Random();
        int a = random.nextInt(1000);
        key = "andy" + a + System.currentTimeMillis();
        mDownloadThread = new DownloadThread(this);
    }

    //构造函数
    Task() {
    }

    public URL getUrl() {
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

    public DownloadThread getDownloadThread() {
        return mDownloadThread;
    }

    public String getFileFormatData() {
        String result = "";
        result += "key=" + key + " ";
        result += "status=" + status + " ";
        result += "url=" + url.toString() + " ";
        if (fileName != null) {
            result += "fileName=" + fileName + " ";
        }

        if (fileLength != -1) {
            result += "fileLength=" + fileLength + " ";
        }

        result += "percent=" + percent + " ";

        return result;
    }

    public void parseFileFormatData(String data) {
        String[] split = data.split(" ");
        key = split[0].substring(5);
        status = Integer.parseInt(split[1].substring(8));
        String url = split[2].substring(5);
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            Log.d(TAG, "数据解析异常");
            e.printStackTrace();
        }
        if (split.length > 3) {
            fileName = split[3].substring(10);
            fileLength = Integer.parseInt(split[4].substring(12));
            percent = Float.parseFloat(split[5].substring(9));
        }
    }
}
