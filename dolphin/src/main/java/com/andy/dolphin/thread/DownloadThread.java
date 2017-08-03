package com.andy.dolphin.thread;

import android.util.Log;

import com.andy.dolphin.DownloadManager;
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

            long readLength = 0;
            File file = null;
            String fileName = task.getFileName();
            if (fileName != null) {
                file = new File(DownloadManager.downloadDirectory + File.separator + fileName);
                
                readLength = file.length();
                con.setRequestProperty("RANGE", "bytes=" + file.length() + "-");
            }
            con.connect();
            if (con.getResponseCode() == 200) {
                int total = con.getContentLength();
                Log.d("文件大小？", "" + total);
                if (fileName == null) {
                    fileName = con.getHeaderField("Content-Disposition");
                    if (fileName == null) {
                        fileName = con.getURL().getFile();
                    }
                    if (fileName.contains("/")) {
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    }
                    fileName = getFileName(fileName);
                    task.setFileName(fileName);

                    file = new File(DownloadManager.downloadDirectory + File.separator + fileName);
                }
                InputStream from = con.getInputStream();
                OutputStream dest = new FileOutputStream(file);
                byte[] read = new byte[1024];
                long length;
                while ((length = from.read(read)) != -1 && task.getStatus() != Task.PAUSE) {
                    dest.write(read);
                    readLength += length;
                    task.setPercent((float) readLength / total);
                    TaskManager.getInstance().notify(TaskManager.PROGRESS, task.getKey());
                }
                dest.flush();
                dest.close();
                from.close();
                if (task.getStatus() == Task.PAUSE) {
                    TaskManager.getInstance().notify(TaskManager.DOWNLOAD_PAUSE, task.getKey());
                } else {
                    task.setStatus(Task.FINISH);
                    TaskManager.getInstance().notify(TaskManager.DOWNLOAD_SUCCESS, task.getKey());
                }

            } else {
                task.setStatus(Task.ERROR);
                TaskManager.getInstance().notify(TaskManager.DOWNLOAD_FAILURE, task.getKey());
            }
        } catch (IOException e) {
            e.printStackTrace();
            task.setStatus(Task.ERROR);
            TaskManager.getInstance().notify(TaskManager.DOWNLOAD_FAILURE, task.getKey());
        }
    }

    private String getFileName(String tempName) {
        //检索下载路径中是否已存在该路径名
        File dir = new File(DownloadManager.downloadDirectory);
        File[] files = dir.listFiles();

        String type;
        String fileName = "";
        boolean check;
        if (tempName.contains(".")) {
            String[] temp = tempName.split("\\.");
            if (temp.length == 2) {
                fileName = tempName.split("\\.")[0];
            } else if (temp.length > 2) {
                for (int i = 0; i < temp.length - 2; i++) {
                    fileName += temp[i] + ".";
                }
                fileName += temp[temp.length - 2];
            }
            type = temp[temp.length - 1];
            check = true;
        } else {
            fileName = tempName;
            type = "";
            check = false;
        }
        int flag = 0;
        for (File f : files) {
            String name = f.getName();
            if (check) {
                if (name.contains(".")) {
                    String[] temp = name.split("\\.");
                    if (temp[0].equals(fileName) && temp[1].equals(type)) {
                        flag++;
                    }
                }
            } else {
                if (name.equals(fileName)) {
                    flag++;
                }
            }
        }
        if (flag > 0) {
            fileName += "(" + flag + ")";
        }
        if (check) {
            return fileName + "." + type;
        }
        return fileName;
    }
}
