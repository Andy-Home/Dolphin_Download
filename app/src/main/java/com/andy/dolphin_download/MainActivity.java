package com.andy.dolphin_download;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private final List<Task> mTasks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadManager.getInstance();
        findView();

    }

    private void findView() {
        mListView = (ListView) findViewById(R.id.list);

        final DownloadAdapter mAdapter = new DownloadAdapter(mTasks);
        mListView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = DownloadManager.download("http://downloads.gradle.org/distributions/gradle-4.1-rc-1-bin.zip", new DownloadManager.DolphinListener() {
                    @Override
                    public void success(int type) {
                        Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(int type) {
                        Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void progress(float length, float download) {
                        Toast.makeText(getApplicationContext(), "下载进度:" + (download / length), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void stop(int type) {
                        Toast.makeText(getApplicationContext(), "暂停下载", Toast.LENGTH_LONG).show();
                    }
                });
                mTasks.add(task);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
