package com.andy.dolphin.thread;

import android.os.Environment;

import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载线程
 * <p>
 * Created by andy on 17-7-31.
 */

public class DownloadThread implements Runnable {
    public static int DOWNLOAD_SUCCESS = 1;
    public static int DOWNLOAD_FAILURE_NETWORK = 2;
    public static int DOWNLOAD_FAILURE_EXCEPTION = 3;
    public static int DOWNLOAD_PAUSE = 4;

    private static final String PATH = Environment.getExternalStorageDirectory() + Environment.DIRECTORY_DOWNLOADS;
    private DownloadManager.DolphinListener listener;
    private URL url;
    private Task task;

    public DownloadThread(Task task) {
        listener = task.getListener();
        url = task.getUrl();

        if (task.getFileName() == null) {
            task.setFileName(getFileName());
        }
        this.task = task;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
            con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Connection", "keep-alive");
            File file = new File(PATH, task.getFileName());
            if (file.exists()) {
                con.setRequestProperty("RANGE", "bytes=" + file.length() + "-");
            }
            con.connect();
            if (con.getResponseCode() == 200) {
                int total = con.getContentLength();
                InputStream from = con.getInputStream();
                OutputStream dest = new FileOutputStream(file);
                byte[] read = new byte[1024];
                int readLength = 0, length;
                while ((length = from.read(read)) != -1 && task.getStatus() != Task.PAUSE) {
                    dest.write(read);
                    readLength += length;
                    listener.progress(total, readLength);
                }
                dest.flush();
                dest.close();
                from.close();
                if (task.getStatus() == Task.PAUSE) {
                    listener.stop(DOWNLOAD_PAUSE);
                } else {
                    listener.success(DOWNLOAD_SUCCESS);
                }
            } else {
                listener.failure(DOWNLOAD_FAILURE_NETWORK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            listener.failure(DOWNLOAD_FAILURE_EXCEPTION);
        }
    }

    /**
     * 获取文件名
     */
    private String getFileName() {
        String link = url.toString();
        //类似https://downloads.gradle.org/distributions/gradle-4.1-milestone-1-bin.zip链接的处理方式
        if (!link.contains("?")) {
            return link.substring(link.lastIndexOf("/") + 1);
        }
        return null;
    }
}
