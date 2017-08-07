package com.andy.dolphin_download;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.andy.dolphin.DolphinObserver;
import com.andy.dolphin.DownloadManager;
import com.andy.dolphin.task.Task;
import com.andy.dolphin.task.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DolphinObserver {
    private final String TAG = "MainActivity";
    private ListView mListView;
    private List<Task> mTasks = new ArrayList<>();
    private String[] urlList = {"http://downloads.gradle.org/distributions/gradle-4.0.2-src.zip",
            "https://github.com/Andy-Home/Rabbit_Log/archive/master.zip",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501954103056&di=ddf8af86453492d129d51a7c05298349&imgtype=0&src=http%3A%2F%2Fdynamic-image.yesky.com%2F740x-%2FuploadImages%2F2015%2F154%2F20%2F6YL92HH61434.jpg"};
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
            int flag = 0;
            @Override
            public void onClick(View view) {
                DownloadManager.download(urlList[(flag++) % 3]);
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
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        TaskManager.getInstance().detach(this);
        TaskManager.getInstance().onDestory();
    }
}
