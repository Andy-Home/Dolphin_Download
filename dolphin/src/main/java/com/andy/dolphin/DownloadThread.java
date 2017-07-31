package com.andy.dolphin;

import com.andy.dolphin.task.Task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载线程
 * <p>
 * Created by andy on 17-7-31.
 */

public class DownloadThread implements Runnable {
    private DownloadManager.DolphinListener listener;
    private URL url;
    private Task task;

    public DownloadThread(Task task) {
        listener = task.getListener();
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
        } catch (IOException e) {
            e.printStackTrace();
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
        //TODO: 如果不是上述方法，等待请求链接后获取文件后缀名
        return null;
    }
}
