package com.andy.dolphin.thread;

import android.util.Log;

import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.Task;
import com.andy.dolphin.task.TaskManager;

import java.io.BufferedOutputStream;
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
    private final String TAG = "DownloadThread";
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
                con.setRequestProperty("RANGE", "bytes=" + readLength + "-");
                Log.d(TAG, "RANGE:byte=" + readLength + "-");
            }
            con.connect();
            Log.d(TAG, "网络应答标志：" + con.getResponseCode());
            if (con.getResponseCode() == 200 || con.getResponseCode() == 206) {
                int total = con.getContentLength();
                if (total == -1) {
                    TaskManager.getInstance().notify(TaskManager.DOWNLOAD_FAILURE, task.getKey());
                } else {

                    //当初次创建文件时,获取文件名
                    if (fileName == null) {
                        fileName = con.getHeaderField("Content-Disposition");
                        if (fileName == null) {
                            fileName = con.getURL().getFile();
                        }
                        if (fileName.contains("filename=")) {
                            fileName = fileName.split("filename=")[1];
                        }

                        if (fileName.contains("/")) {
                            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                        }
                        if (fileName.contains("\"")) {
                            fileName = fileName.replace("\"", "");
                            Log.d(TAG, "替换“符号");
                        }

                        fileName = getFileName(fileName);
                        task.setFileName(fileName);

                        file = new File(DownloadManager.downloadDirectory + File.separator + fileName);
                    }
                    //写入数据
                    InputStream from = con.getInputStream();
                    OutputStream dest;
                    if (task.getFileLength() == -1) {
                        task.setFileLength(total);
                        dest = new BufferedOutputStream(new FileOutputStream(file));
                    } else {
                        //如果传输的内容为全部文件，重新开始下载文件内容
                        //如果传输的内容为文件未下载内容,传输内容接着后面写入
                        if (total == task.getFileLength()) {
                            readLength = 0;
                            dest = new BufferedOutputStream(new FileOutputStream(file, false));
                        } else {
                            total = task.getFileLength();
                            dest = new BufferedOutputStream(new FileOutputStream(file, true));
                        }
                    }

                    byte[] read = new byte[1024];
                    int length;
                    float percent = 0.0f;
                    Log.d(TAG, "正在下载：" + task.getKey());
                    while ((length = from.read(read)) != -1 && task.getStatus() != Task.PAUSE) {
                        dest.write(read, 0, length);
                        readLength += length;
                        if (((float) readLength / total) - percent >= 0.001 || ((float) readLength / total) == 1) {
                            percent = (float) readLength / total;
                            task.setPercent(percent);
                        }
                        TaskManager.getInstance().notify(TaskManager.PROGRESS, task.getKey());
                    }
                    dest.flush();
                    dest.close();
                    from.close();
                    if (task.getStatus() == Task.PAUSE) {
                        TaskManager.getInstance().notify(TaskManager.DOWNLOAD_PAUSE, task.getKey());
                    } else {
                        TaskManager.getInstance().notify(TaskManager.DOWNLOAD_SUCCESS, task.getKey());
                    }
                }
            } else {
                TaskManager.getInstance().notify(TaskManager.DOWNLOAD_FAILURE, task.getKey());
            }
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            TaskManager.getInstance().notify(TaskManager.DOWNLOAD_FAILURE, task.getKey());
        }
    }

    private String getFileName(String tempName) {
        String type;
        String fileName = "";
        boolean check;
        //获取临时文件名的名字与文件类型后缀
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
        //检索下载路径中是否已存在该路径名
        File dir = new File(DownloadManager.downloadDirectory);
        File[] files = dir.listFiles();

        int flag = 0;
        for (File f : files) {
            String name = f.getName();
            if (check) {
                if (name.contains(".")) {
                    String tempFileName = name.substring(0, name.lastIndexOf("."));
                    String tempFileType = name.substring(name.lastIndexOf(".") + 1);
                    if ((tempFileName.equals(fileName) || tempFileName.substring(0, tempFileName.length() - 3).equals(fileName))
                            && tempFileType.equals(type)) {
                        flag++;
                    }
                }
            } else {
                if (name.equals(fileName) || name.substring(0, name.length() - 3).equals(fileName)) {
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
