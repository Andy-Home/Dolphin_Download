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
        DownloadManager.getInstance(this);
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
                DownloadManager.download("http://services.gradle.org/distributions/gradle-4.1-rc-2-src.zip");
            }
        });
    }

    @Override
    public void update(int type, Task task) {
        if (type == TaskManager.PROGRESS) {
            int position = mTasks.indexOf(task);
            /**第一个可见的位置**/
            int firstVisiblePosition = mListView.getFirstVisiblePosition();
            /**最后一个可见的位置**/
            int lastVisiblePosition = mListView.getLastVisiblePosition();

            /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
            if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
                /**获取指定位置view对象**/
                View view = mListView.getChildAt(position - firstVisiblePosition);
                mAdapter.getView(position, view, mListView);
            }
        } else {
            mTasks.clear();
            mTasks.addAll(TaskManager.getInstance().getTaskList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskManager.getInstance().detach(this);
    }
}
