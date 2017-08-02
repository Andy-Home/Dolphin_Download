package com.andy.dolphin.thread;

import android.util.Log;

import com.andy.dolphin.task.Task;
import com.andy.dolphin.task.TaskManager;

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
    private URL url;
    private Task task;

    public DownloadThread(Task task) {
        url = task.getUrl();
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
            File file = new File(task.getFilePath());
            long readLength = 0;
            if (file.exists()) {
                Log.d("文件是否存在？", "Yes");
                readLength = file.length();
                con.setRequestProperty("RANGE", "bytes=" + file.length() + "-");
            } else {
                Log.d("文件是否存在？", "No");
            }
            Log.d("request", con.getRequestProperties().toString());
            con.connect();
            if (con.getResponseCode() == 200) {
                int total = con.getContentLength();
                Log.d("文件大小？", "" + total);
                Log.d("header", con.getHeaderFields().toString());
                InputStream from = con.getInputStream();
                OutputStream dest = new FileOutputStream(file);
                byte[] read = new byte[1024];
                long length;
                while ((length = from.read(read)) != -1 && task.getStatus() != Task.PAUSE) {
                    dest.write(read);
                    readLength += length;
                    task.setPercent((float) readLength / total);
                    TaskManager.getInstance().notify(TaskManager.PROGRESS);
                }
                dest.flush();
                dest.close();
                from.close();
                if (task.getStatus() == Task.PAUSE) {
                    task.setStatus(Task.PAUSE);
                    TaskManager.getInstance().notify(TaskManager.DOWNLOAD_PAUSE);
                } else {
                    task.setStatus(Task.FINISH);
                    TaskManager.getInstance().notify(TaskManager.DOWNLOAD_SUCCESS);
                }

            } else {
                task.setStatus(Task.ERROR);
                TaskManager.getInstance().notify(TaskManager.DOWNLOAD_FAILURE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            task.setStatus(Task.ERROR);
            TaskManager.getInstance().notify(TaskManager.DOWNLOAD_FAILURE);
        }
    }
}
