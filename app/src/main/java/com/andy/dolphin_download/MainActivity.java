package com.andy.dolphin_download;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.andy.dolphin.DolphinObserver;
import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.Task;
import com.andy.dolphin.task.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DolphinObserver {
    private ListView mListView;
    private List<Task> mTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadManager.getInstance();
        TaskManager.getInstance().attach(this);
        findView();

    }

    private DownloadAdapter mAdapter;
    private void findView() {

        mListView = (ListView) findViewById(R.id.list);

        mAdapter = new DownloadAdapter(mTasks);
        mListView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.download(getApplicationContext(), "https://github.com/Andy-Home/Rabbit_Log/archive/master.zip");
            }
        });
    }

    @Override
    public void update(int type) {
        mTasks.clear();
        mTasks.addAll(TaskManager.getInstance().getTaskList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskManager.getInstance().detach(this);
    }
}
